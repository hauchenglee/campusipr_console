package biz.mercue.campusipr.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import biz.mercue.campusipr.model.Assignee;
import biz.mercue.campusipr.model.Inventor;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentContext;

public class ServiceTaiwanPatent {
		
	private static  Logger log = Logger.getLogger(ServiceTaiwanPatent.class.getName());
	
	public static List<Patent> getPatentRightByApplNo(String applNo) {
		String url = Constants.PATENT_WEB_SERVICE_TW+"/PatentRights?format=json&tk=%s&applno=%s";
		url = String.format(url, Constants.PATENT_KEY_TW ,applNo);
		
		List<Patent> patentList = new ArrayList<Patent>();
		try {
			JSONObject getObject = new JSONObject(HttpRequestUtils.sendGet(url));
			List<Patent> patentListI = convertPatentInfoCh(getObject.optJSONObject("tw-patent-rightsI"));
			List<Patent> patentListM = convertPatentInfoCh(getObject.optJSONObject("tw-patent-rightsM"));
			List<Patent> patentListD = convertPatentInfoCh(getObject.optJSONObject("tw-patent-rightsD"));
			patentList.addAll(patentListI);
			patentList.addAll(patentListM);
			patentList.addAll(patentListD);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return patentList;
	}
	
	public static List<Patent> getPatentRightByAssigneeNameEn(String assigneeName, int row) {
		String url = Constants.PATENT_WEB_SERVICE_TW+"/PatentRights?format=json&tk=%s&top=%s&applnamee=%s";
		try {
			url = String.format(url, Constants.PATENT_KEY_TW, row, URLEncoder.encode(assigneeName, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		List<Patent> patentList = new ArrayList<Patent>();
		try {
			JSONObject getObject = new JSONObject(HttpRequestUtils.sendGet(url));
			List<Patent> patentListI = convertPatentInfoCh(getObject.optJSONObject("tw-patent-rightsI"));
			List<Patent> patentListM = convertPatentInfoCh(getObject.optJSONObject("tw-patent-rightsM"));
			List<Patent> patentListD = convertPatentInfoCh(getObject.optJSONObject("tw-patent-rightsD"));
			patentList.addAll(patentListI);
			patentList.addAll(patentListM);
			patentList.addAll(patentListD);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return patentList;
	}
	
	public static List<Patent> getPatentRightByAssigneeNameCh(String assigneeName, int row) {
		String url = Constants.PATENT_WEB_SERVICE_TW+"/PatentRights?format=json&tk=%s&top=%s&applnamec=%s";
		try {
			url = String.format(url, Constants.PATENT_KEY_TW, row, URLEncoder.encode(assigneeName, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		List<Patent> patentList = new ArrayList<Patent>();
		try {
			JSONObject getObject = new JSONObject(HttpRequestUtils.sendGet(url));
			List<Patent> patentListI = convertPatentInfoCh(getObject.optJSONObject("tw-patent-rightsI"));
			List<Patent> patentListM = convertPatentInfoCh(getObject.optJSONObject("tw-patent-rightsM"));
			List<Patent> patentListD = convertPatentInfoCh(getObject.optJSONObject("tw-patent-rightsD"));
			patentList.addAll(patentListI);
			patentList.addAll(patentListM);
			patentList.addAll(patentListD);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return patentList;
	}
	
	private static List<Patent> convertPatentInfoCh(JSONObject obj) {
		
		List<Patent> patentList = new ArrayList<Patent>();
		
		if (obj != null) {
			JSONArray patentContentObj = obj.optJSONArray("patentcontent");
			if (patentContentObj != null) {
				for (int index = 0; index < patentContentObj.length(); index++) {
					JSONObject patentObj = patentContentObj.optJSONObject(index);
					Patent patent = new Patent();
					patent.setPatent_name(patentObj.optJSONObject("patent-title").optString("patent-name-chinese"));
					patent.setPatent_name_en(patentObj.optJSONObject("patent-title").optString("patent-name-english"));
					patent.setPatent_appl_country("TW");
					try {
						String applDateStr = patentObj.optJSONObject("application-reference").optString("appl-date");
						if (StringUtils.isNULL(applDateStr) == false) {
							Date applDate = DateUtils.parserSimpleDateSlashFormatDate(applDateStr);
							patent.setPatent_appl_date(applDate);
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					patent.setPatent_appl_no(patentObj.optJSONObject("application-reference").optString("appl-no"));
					
					patent.setPatent_notice_no(patentObj.optJSONObject("publication-reference").optString("notice-no"));
					try {
						String noticeDateStr = patentObj.optJSONObject("publication-reference").optString("notice-date");
						if (StringUtils.isNULL(noticeDateStr) == false) {
							Date noticeDate = DateUtils.parserSimpleDateSlashFormatDate(noticeDateStr);
							patent.setPatent_notice_date(noticeDate);
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					patent.setPatent_publish_no(patentObj.optJSONObject("publication-reference").optString("publish-no"));
					try {
						String publishDateStr = patentObj.optJSONObject("publication-reference").optString("publish-date");
						if (StringUtils.isNULL(publishDateStr) == false) {
							Date publishDate = DateUtils.parserSimpleDateSlashFormatDate(publishDateStr);
							patent.setPatent_publish_date(publishDate);
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					patent.setPatent_no(patentObj.optJSONObject("patent-right").optString("patent-no"));
					try {
						String patentBeginDateStr = patentObj.optJSONObject("patent-right").optString("patent-bdate");
						if (StringUtils.isNULL(patentBeginDateStr) == false) {
							Date patentBeginDate = DateUtils.parserSimpleDateSlashFormatDate(patentBeginDateStr);
							patent.setPatent_bdate(patentBeginDate);
						}
						
						String patentEndDateStr = patentObj.optJSONObject("patent-right").optString("patent-edate");
						if (StringUtils.isNULL(patentEndDateStr) == false) {
							Date patentEndDate = DateUtils.parserSimpleDateSlashFormatDate(patentEndDateStr);
							patent.setPatent_edate(patentEndDate);
						}
						
						String patentCancelDateStr = patentObj.optJSONObject("patent-right").optString("cancel-date");
						if (StringUtils.isNULL(patentCancelDateStr) == false) {
							Date patentCancelDate = DateUtils.parserSimpleDateSlashFormatDate(patentCancelDateStr);
							patent.setPatent_cancel_date(patentCancelDate);
						}
						
						String patentExpireDateStr = patentObj.optJSONObject("patent-right").optString("charge-expir-date");
						if (StringUtils.isNULL(patentExpireDateStr) == false) {
							Date patentExpireDate = DateUtils.parserSimpleDateSlashFormatDate(patentExpireDateStr);
							patent.setPatent_charge_expire_date(patentExpireDate);
						}
						
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					String expireYear = patentObj.optJSONObject("patent-right").optString("charge-expir-year");
					patent.setPatent_charge_duration_year(Integer.parseInt(expireYear));
					
					String contextUrl = patentObj.optJSONObject("link").optString("patentpubxml-url");
					if (StringUtils.isNULL(contextUrl)==false) {
						PatentContext patentContext = getContext(contextUrl);
						if (!StringUtils.isNULL(patentContext.getContext_claim()) 
								|| !StringUtils.isNULL(patentContext.getContext_abstract())
								|| !StringUtils.isNULL(patentContext.getContext_desc())) {
							patentContext.setPatent(patent);
							patent.setPatentContext(patentContext);
						}
					}
					
					List<Inventor> listInventor = new ArrayList<Inventor>();
					JSONArray inventors = patentObj.optJSONObject("parties").optJSONArray("inventors");
					
					for (int inventorIndex = 0; inventorIndex < inventors.length(); inventorIndex++) {
						JSONObject invObj = inventors.optJSONObject(inventorIndex);
						Inventor inv = new Inventor();
						inv.setInventor_name(invObj.optString("chinese-name"));
						inv.setInventor_name_en(invObj.optString("english-name"));
						inv.setCountry_id(invObj.optString("english-country"));
						inv.setCountry_name(invObj.optString("chinese-country"));
						inv.setInventor_order(invObj.optInt("-sequence"));
						inv.setPatent(patent);
						listInventor.add(inv);
					}
					
					patent.setListInventor(listInventor);
					
					List<Assignee> listAssignee = new ArrayList<Assignee>();
					JSONArray assignee = patentObj.optJSONObject("parties").optJSONArray("applicants");
					
					for (int assigneeIndex = 0; assigneeIndex < assignee.length(); assigneeIndex++) {
						JSONObject assigneeObj = assignee.optJSONObject(assigneeIndex);
						Assignee assign = new Assignee();
						assign.setAssignee_name(assigneeObj.optString("chinese-name"));
						assign.setAssignee_name_en(assigneeObj.optString("english-name"));
						assign.setCountry_id(assigneeObj.optString("english-country"));
						assign.setCountry_name(assigneeObj.optString("chinese-country"));
						assign.setAssignee_order(assigneeObj.optInt("-sequence"));
						assign.setPatent(patent);
						listAssignee.add(assign);
					}
					
					patent.setListAssignee(listAssignee);
					
					patentList.add(patent);
				}
			}
		}
		
		return patentList;
		
	}
	
	private static PatentContext convertPatentContextXmlCh(String content) {
		PatentContext patentContext = new PatentContext();
		try{
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
			NodeList abstractList = doc.getElementsByTagName("abstract-dtext");
			String abstractStr = "";
			for (int temp = 0; temp < abstractList.getLength(); temp++) {
				Node nNode = abstractList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) { 
					Element eElement = (Element) nNode;
					NodeList childNodeList = eElement.getChildNodes();
					for (int childIndex = 0; childIndex < childNodeList.getLength(); childIndex++) {
						Node cNode = childNodeList.item(childIndex);
						abstractStr += cNode.getTextContent() + "\n";
					}
				}
			}
			patentContext.setContext_abstract(abstractStr);
			
			String descStr = "";
			NodeList technicalList = doc.getElementsByTagName("technical-field");
			for (int temp = 0; temp < technicalList.getLength(); temp++) {
				Node nNode = technicalList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) { 
					Element eElement = (Element) nNode;
					NodeList childNodeList = eElement.getChildNodes();
					for (int childIndex = 0; childIndex < childNodeList.getLength(); childIndex++) {
						Node cNode = childNodeList.item(childIndex);
						descStr += cNode.getTextContent() + "\n";
					}
				}
			}
			
			NodeList backgroundList = doc.getElementsByTagName("background-art");
			for (int temp = 0; temp < backgroundList.getLength(); temp++) {
				Node nNode = backgroundList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) { 
					Element eElement = (Element) nNode;
					NodeList childNodeList = eElement.getChildNodes();
					for (int childIndex = 0; childIndex < childNodeList.getLength(); childIndex++) {
						Node cNode = childNodeList.item(childIndex);
						descStr += cNode.getTextContent() + "\n";
					}
				}
			}
			
			NodeList disclosureList = doc.getElementsByTagName("disclosure");
			for (int temp = 0; temp < disclosureList.getLength(); temp++) {
				Node nNode = disclosureList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) { 
					Element eElement = (Element) nNode;
					NodeList childNodeList = eElement.getChildNodes();
					for (int childIndex = 0; childIndex < childNodeList.getLength(); childIndex++) {
						Node cNode = childNodeList.item(childIndex);
						descStr += cNode.getTextContent() + "\n";
					}
				}
			}
			
			NodeList inventionList = doc.getElementsByTagName("mode-for-invention");
			for (int temp = 0; temp < inventionList.getLength(); temp++) {
				Node nNode = inventionList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) { 
					Element eElement = (Element) nNode;
					NodeList childNodeList = eElement.getChildNodes();
					for (int childIndex = 0; childIndex < childNodeList.getLength(); childIndex++) {
						Node cNode = childNodeList.item(childIndex);
						descStr += cNode.getTextContent() + "\n";
					}
				}
			}
			patentContext.setContext_desc(descStr);
			
			String claimStr = "";
			NodeList claimList = doc.getElementsByTagName("claim");
			for (int temp = 0; temp < claimList.getLength(); temp++) {
				Node nNode = claimList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) { 
					Element eElement = (Element) nNode;
					NodeList childNodeList = eElement.getChildNodes();
					for (int childIndex = 0; childIndex < childNodeList.getLength(); childIndex++) {
						Node cNode = childNodeList.item(childIndex);
						claimStr += cNode.getTextContent() + "\n";
					}
				}
			}
			
			patentContext.setContext_claim(claimStr);
			
			
		}catch(Exception e){
			log.error(e.getMessage());
		}
		
		return patentContext;
	}
	
	private static PatentContext getContext(String link) {
		PatentContext patentContext = new PatentContext();
		try {
			String content = FtpRequestUtils.sendGet(link);
			if ("XML".equals(getCtxType(content))) {
				patentContext = convertPatentContextXmlCh(content);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return patentContext;
	}
	
	
	private static String getCtxType(String context) {
	    if (context.startsWith("{")) {
		   log.info("Message is valid JSON.");
		   return "JSON";
	    }else {
	    	log.info("Message is valid XML.");
	        return "XML";
	    }
	}
	
}
