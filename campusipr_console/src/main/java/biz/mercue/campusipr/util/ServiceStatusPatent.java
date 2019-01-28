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
import biz.mercue.campusipr.model.Status;
import biz.mercue.campusipr.model.View;
import biz.mercue.campusipr.service.PatentService;

public class ServiceStatusPatent {
	
	private static  Logger log = Logger.getLogger(ServiceStatusPatent.class.getName());
	
	public static void getPatentStatus(Patent patent) {
		if (!patent.getPatent_appl_country().equals(Constants.APPL_COUNTRY_US)) {
			String url = Constants.PATENT_WEB_SERVICE_EU+"/rest-services/family/publication/EPODOC/%s/legal";
			if (patent.getPatent_appl_country().equals(Constants.APPL_COUNTRY_TW)) {
				url = String.format(url, "TW"+patent.getPatent_notice_no());
			}else {
				url = String.format(url, patent.getPatent_appl_no());
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			String url = Constants.PATENT_INVENTOR_WEB_SERVICE_US;
			JSONObject obj = new JSONObject();
			if (patent.getPatent_appl_no() != null) {
				obj.put("searchText", "applId:"+patent.getPatent_appl_no().substring(2));
				obj.put("mm", "100%");
				obj.put("qf", "applId");
				log.info(obj.toString());
				
				try {
					JSONObject getObject = new JSONObject(HttpRequestUtils.sendPost(url, obj.toString()));
					if (getObject != null) {
						convertPatentStatusInfoUSTPOXml(patent, getObject);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private static void convertPatentStatusInfoUSTPOXml(Patent patent, JSONObject getObject) {
		JSONArray patentDocsObj = getObject.optJSONObject("queryResults").optJSONObject("searchResponse")
				.optJSONObject("response").optJSONArray("docs");
		List<Status> statusList = new ArrayList<>();
		for (int index = 0; index < patentDocsObj.length(); index++) {
			JSONObject patentObj = patentDocsObj.optJSONObject(index);
			JSONArray patentTransaction = patentObj.optJSONArray("transactions");
			for (int indexTransaction = 0; indexTransaction < patentTransaction.length(); indexTransaction++) {
				JSONObject transactionObj = patentTransaction.optJSONObject(indexTransaction);
				PatentStatus ps = new PatentStatus();
				ps.setPatent_id(patent.getPatent_id());
				Status status = new Status();
				status.setCountry_id("us");
				status.setEvent_code(transactionObj.optString("code"));
				status.setEvent_code_desc(transactionObj.optString("description"));
				try {
					String psCreateDateStr = transactionObj.optString("recordDate");
					if (StringUtils.isNULL(psCreateDateStr) == false) {
						Date psCreateDate = DateUtils.parserDateTimeString(psCreateDateStr);
						ps.setCreate_date(psCreateDate);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				status.setStatus_from(Constants.STATUS_FROM_USPTO);
				status.setPatentStatus(ps);
				statusList.add(status);
			}
		}
		patent.setListStatus(statusList);
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
			List<Status> statusList = new ArrayList<>();
			NodeList documentList = doc.getElementsByTagName("ops:legal");
			for (int temp = 0; temp < documentList.getLength(); temp++) {
				PatentStatus ps = new PatentStatus();
				ps.setPatent_id(patent.getPatent_id());
				Status status = new Status();
				Node nNode = documentList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) { 
					Element eElement = (Element) nNode;
					String countryId = eElement.getElementsByTagName("ops:L001EP").item(0).getTextContent().toLowerCase();
					if (countryId.equals(patent.getPatent_appl_country().toLowerCase())) {
						status.setCountry_id(countryId);
						String eventCode = eElement.getAttribute("code");
						status.setEvent_code(eventCode);
						String eventDesc = eElement.getAttribute("desc");
						status.setEvent_code_desc(eventDesc);
						String eventClass = eElement.getElementsByTagName("ops:L004EP").item(0).getTextContent();
						status.setEvent_class(eventClass);
						try {
							String psCreateDateStr = eElement.getElementsByTagName("ops:L019EP").item(0).getTextContent();
							if (StringUtils.isNULL(psCreateDateStr) == false) {
								Date psCreateDate = DateUtils.parserSimpleDateHyphenFormatDate(psCreateDateStr);
								ps.setCreate_date(psCreateDate);
							}
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						status.setStatus_from(Constants.STATUS_FROM_EPO);
						status.setPatentStatus(ps);
						statusList.add(status);
					}
				}
			}
			patent.setListStatus(statusList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return authToken;
	}

}
