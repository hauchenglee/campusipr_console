package biz.mercue.campusipr.util;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import biz.mercue.campusipr.model.Assignee;
import biz.mercue.campusipr.model.Inventor;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentStatus;
import biz.mercue.campusipr.model.PatentStatusId;
import biz.mercue.campusipr.model.Status;
import biz.mercue.campusipr.model.View;
import biz.mercue.campusipr.service.PatentService;

public class ServiceStatusPatent {
	
	private static  Logger log = Logger.getLogger(ServiceStatusPatent.class.getName());
	
	public static void syncListPatentStatus(List<Patent> list) {
		for (Patent patent:list) {
			getPatentStatus(patent);
		}
	}
	
	public static void getPatentStatus(Patent patent) {
		if (!patent.getPatent_appl_country().equals(Constants.APPL_COUNTRY_US)) {
			String url = Constants.PATENT_WEB_SERVICE_EU+"/rest-services/family/publication/EPODOC/%s/legal";
			if (patent.getPatent_appl_country().equals(Constants.APPL_COUNTRY_TW)) {
				url = String.format(url, patent.getPatent_appl_country().toUpperCase()+patent.getPatent_notice_no());
			}else {
				url = String.format(url, patent.getPatent_appl_country().toUpperCase()+patent.getPatent_appl_no());
			}
			
			try {
				String token = generateToken("Basic "+Constants.PATENT_TOKEN_EU);
				if (token != null) {
					String content = (HttpRequestUtils.sendGetByToken(url, token));
					if (!StringUtils.isNULL(content)) {
						convertPatentStatusInfoEPOXml(patent, content);
					}
				} else {
					log.error("token must not null");
				}
			} catch (JSONException e) {
				log.error(e);
			} catch (Exception e) {
				log.error(e);
			}
		} else {
			String url = Constants.PATENT_INVENTOR_WEB_SERVICE_US;
			JSONObject obj = new JSONObject();
			if (patent.getPatent_appl_no() != null) {
				obj.put("searchText", "applId:"+patent.getPatent_appl_no());
				obj.put("mm", "100%");
				obj.put("qf", "applId");
				log.info(obj.toString());
				
				try {
					JSONObject getObject = new JSONObject(HttpRequestUtils.sendPost(url, obj.toString()));
					if (getObject != null) {
						convertPatentStatusInfoUSTPOXml(patent, getObject);
					}
				} catch (Exception e) {
					log.error(e);
				}
			}
		}
	}
	
	private static void convertPatentStatusInfoUSTPOXml(Patent patent, JSONObject getObject) {
		JSONArray patentDocsObj = getObject.optJSONObject("queryResults").optJSONObject("searchResponse")
				.optJSONObject("response").optJSONArray("docs");
		List<String> duplicateData = new ArrayList<>();
		for (int index = 0; index < patentDocsObj.length(); index++) {
			JSONObject patentObj = patentDocsObj.optJSONObject(index);
			JSONArray patentTransaction = patentObj.optJSONArray("transactions");
			for (int indexTransaction = 0; indexTransaction < patentTransaction.length(); indexTransaction++) {
				JSONObject transactionObj = patentTransaction.optJSONObject(indexTransaction);
				Date psCreateDate = null;
				try {
					String psCreateDateStr = transactionObj.optString("recordDate");
					if (StringUtils.isNULL(psCreateDateStr) == false) {
						psCreateDate = DateUtils.parserDateTimeString(psCreateDateStr);
					}
				} catch (ParseException e) {
					log.error(e);
				}
				if (!duplicateData.contains(transactionObj.optString("code")+"-"+DateUtils.getDashFormatDate(psCreateDate))) {
					Status status = new Status();
					status.setCountry_id("us");
					status.setEvent_code(transactionObj.optString("code"));
					status.setEvent_code_desc(transactionObj.optString("description"));
					status.setStatus_from(Constants.STATUS_FROM_USPTO);
					patent.addStatus(status, psCreateDate);
					duplicateData.add(transactionObj.optString("code")+"-"+DateUtils.getDashFormatDate(psCreateDate));
				}
			}
		}
	}
	
	private static void convertPatentStatusInfoEPOXml(Patent patent, String content) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(false);
			dbf.setNamespaceAware(true);
			dbf.setFeature("http://xml.org/sax/features/namespaces", false);
			dbf.setFeature("http://xml.org/sax/features/validation", false);
			dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
			dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(content));
			Document doc = db.parse(is);
			doc.getDocumentElement().normalize();
			NodeList documentList = doc.getElementsByTagName("ops:legal");
			List<String> duplicateData = new ArrayList<>();
			for (int temp = 0; temp < documentList.getLength(); temp++) {
				Status status = new Status();
				Node nNode = documentList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) { 
					Element eElement = (Element) nNode;
					String countryId = eElement.getElementsByTagName("ops:L001EP").item(0).getTextContent().toLowerCase();
					if (countryId.equals(patent.getPatent_appl_country().toLowerCase())) {
						String eventCode = eElement.getAttribute("code");
						String eventDesc = eElement.getAttribute("desc");
						String eventClass = eElement.getElementsByTagName("ops:L004EP").item(0).getTextContent();

						Date psCreateDate = null;
						try {
							String psCreateDateStr = eElement.getElementsByTagName("ops:L019EP").item(0).getTextContent();
							if (StringUtils.isNULL(psCreateDateStr) == false) {
								psCreateDate = DateUtils.parserSimpleDateHyphenFormatDate(psCreateDateStr);
							}
						} catch (ParseException e) {
							log.error("ParseException:"+e.getMessage());
						}
						if (!duplicateData.contains(eventCode+"-"+DateUtils.getDashFormatDate(psCreateDate))) {
							status.setCountry_id(countryId);
							status.setEvent_code(eventCode);
							status.setEvent_code_desc(eventDesc);
							status.setEvent_class(eventClass);
							status.setStatus_from(Constants.STATUS_FROM_EPO);
							patent.addStatus(status, psCreateDate);
							duplicateData.add(eventCode+"-"+DateUtils.getDashFormatDate(psCreateDate));
						}
						
					}
				}
			}
			
		} catch (Exception e) {
			log.error("Exception:"+e.getMessage());
		}
	}
	
	private static String generateToken(String token) {
		String url = Constants.PATENT_WEB_SERVICE_EU+"/auth/accesstoken";
		String authToken = null;
		try {
			String param = "grant_type=client_credentials";
			String context = HttpRequestUtils.sendPostByToken(url, param, token);
			if (!StringUtils.isNULL(context)) {
				JSONObject contentObj = new JSONObject(context);
				authToken = "Bearer "+contentObj.optString("access_token");
				log.info("AuthTokn:"+authToken);
			}
		} catch (Exception e) {
			log.error("Exception:"+e.getMessage());
		}
		
		return authToken;
	}

}
