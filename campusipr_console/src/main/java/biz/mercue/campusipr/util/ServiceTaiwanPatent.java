package biz.mercue.campusipr.util;

import java.io.StringReader;
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

import biz.mercue.campusipr.model.Applicant;
import biz.mercue.campusipr.model.Assignee;
import biz.mercue.campusipr.model.IPCClass;
import biz.mercue.campusipr.model.Inventor;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentAbstract;
import biz.mercue.campusipr.model.PatentClaim;
import biz.mercue.campusipr.model.PatentDescription;

public class ServiceTaiwanPatent {
		
	private static  Logger log = Logger.getLogger(ServiceTaiwanPatent.class.getName());
	
	public static List<Patent> getPatentRightByAssignee(List<String> applicantNames) {
		//同義詞字串進入
		List<Patent> list = new ArrayList<>();
		for (String applicant:applicantNames) {
			boolean isSync = false;
			Patent patent = new Patent();
			String url = Constants.PATENT_WEB_SERVICE_TW+"/PatentRights?format=json&tk=%s&applnamee=%s&applclass=%s";
			if (StringUtils.hasChinese(applicant)) {
				url = Constants.PATENT_WEB_SERVICE_TW+"/PatentRights?format=json&tk=%s&applnamec=%s&applclass=%s";
			}
			url = String.format(url, Constants.PATENT_KEY_TW ,applicant, 1);
			try {
				String context = HttpRequestUtils.sendGet(url);
				if (!StringUtils.isNULL(context)) {
					JSONObject getObject = new JSONObject(context);
					JSONObject obj = getObject.optJSONObject("tw-patent-rightsI");
					if (obj != null) {
						JSONArray contentArray = obj.optJSONArray("patentcontent");
						if (contentArray != null) {
							for (int index = 0; index < contentArray.length(); index++) {
								JSONObject contentObj = contentArray.getJSONObject(index);
								convertPatentInfoChS(patent, contentObj);
								list.add(patent);
							}
							isSync = true;
						}
					}
				}
				if (isSync == false) {
					url = String.format(url, Constants.PATENT_KEY_TW ,applicant,2);
					context = HttpRequestUtils.sendGet(url);
					if (!StringUtils.isNULL(context)) {
						JSONObject getObject = new JSONObject(context);
						JSONObject obj = getObject.optJSONObject("tw-patent-rightsM");
						if (obj != null) {
							JSONArray contentArray = obj.optJSONArray("patentcontent");
							if (contentArray != null) {
								for (int index = 0; index < contentArray.length(); index++) {
									JSONObject contentObj = contentArray.getJSONObject(index);
									convertPatentInfoChS(patent, contentObj);
									list.add(patent);
								}
								isSync = true;
							}
						}
					}
				}
				if (isSync == false) {
					url = String.format(url, Constants.PATENT_KEY_TW ,applicant,3);
					context = HttpRequestUtils.sendGet(url);
					if (!StringUtils.isNULL(context)) {
						JSONObject getObject = new JSONObject(context);
						JSONObject obj = getObject.optJSONObject("tw-patent-rightsD");
						if (obj != null) {
							JSONArray contentArray = obj.optJSONArray("patentcontent");
							if (contentArray != null) {
								for (int index = 0; index < contentArray.length(); index++) {
									JSONObject contentObj = contentArray.getJSONObject(index);
									convertPatentInfoChS(patent, contentObj);
									list.add(patent);
								}
								isSync = true;
							}
						}
					}
				}
				if (isSync == false) {
					url = Constants.PATENT_WEB_SERVICE_TW+"/PatentPub?format=json&tk=%s&applnamee=%s";
					if (StringUtils.hasChinese(applicant)) {
						url = Constants.PATENT_WEB_SERVICE_TW+"/PatentPub?format=json&tk=%s&applnamec=%s";
					}
					url = String.format(url, Constants.PATENT_KEY_TW ,applicant);
					context = HttpRequestUtils.sendGet(url);
					if (!StringUtils.isNULL(context)) {
						JSONObject getObject = new JSONObject(context);
						JSONObject obj = getObject.optJSONObject("tw-patent-pub");
						if (obj != null) {
							JSONArray contentArray = obj.optJSONArray("patentcontent");
							if (contentArray != null) {
								for (int index = 0; index < contentArray.length(); index++) {
									JSONObject contentObj = contentArray.getJSONObject(index);
									convertPatentInfoChS(patent, contentObj);
									list.add(patent);
								}
								isSync = true;
							}
						}
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public static void getPatentRightByApplNo(Patent patent) {
		boolean isSync = false;
		String url = Constants.PATENT_WEB_SERVICE_TW+"/PatentRights?format=json&tk=%s&applno=%s&applclass=%s";
		url = String.format(url, Constants.PATENT_KEY_TW ,patent.getPatent_appl_no(),1);
		
		try {
			String context = HttpRequestUtils.sendGet(url);
			if (!StringUtils.isNULL(context)) {
				JSONObject getObject = new JSONObject(context);
				JSONObject obj = getObject.optJSONObject("tw-patent-rightsI");
				if (obj != null) {
					JSONArray contentArray = obj.optJSONArray("patentcontent");
					if (contentArray != null) {
						JSONObject contentObj = contentArray.optJSONObject(0);
						convertPatentInfoChS(patent, contentObj);
						isSync = true;
					}
				}
			}
			if (isSync == false) {
				url = String.format(url, Constants.PATENT_KEY_TW ,patent.getPatent_appl_no(),2);
				context = HttpRequestUtils.sendGet(url);
				if (!StringUtils.isNULL(context)) {
					JSONObject getObject = new JSONObject(context);
					JSONObject obj = getObject.optJSONObject("tw-patent-rightsM");
					if (obj != null) {
						JSONArray contentArray = obj.optJSONArray("patentcontent");
						if (contentArray != null) {
							JSONObject contentObj = contentArray.optJSONObject(0);
							convertPatentInfoChS(patent, contentObj);
							isSync = true;
						}
					}
				}
			}
			if (isSync == false) {
				url = String.format(url, Constants.PATENT_KEY_TW ,patent.getPatent_appl_no(),3);
				context = HttpRequestUtils.sendGet(url);
				if (!StringUtils.isNULL(context)) {
					JSONObject getObject = new JSONObject(context);
					JSONObject obj = getObject.optJSONObject("tw-patent-rightsD");
					if (obj != null) {
						JSONArray contentArray = obj.optJSONArray("patentcontent");
						if (contentArray != null) {
							JSONObject contentObj = contentArray.optJSONObject(0);
							convertPatentInfoChS(patent, contentObj);
							isSync = true;
						}
					}
				}
			}
			if (isSync == false) {
				url = Constants.PATENT_WEB_SERVICE_TW+"/PatentPub?format=json&tk=%s&applno=%s";
				url = String.format(url, Constants.PATENT_KEY_TW ,patent.getPatent_appl_no());
				context = HttpRequestUtils.sendGet(url);
				if (!StringUtils.isNULL(context)) {
					JSONObject getObject = new JSONObject(context);
					JSONObject obj = getObject.optJSONObject("tw-patent-pub");
					if (obj != null) {
						JSONArray contentArray = obj.optJSONArray("patentcontent");
						if (contentArray != null) {
							JSONObject contentObj = contentArray.optJSONObject(0);
							convertPatentInfoChS(patent, contentObj);
							isSync = true;
						}
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	private static void convertPatentInfoChS(Patent patent, JSONObject obj) {
		
		if (obj != null) {
			JSONObject patentObj = obj;
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
			
			if (patentObj.optJSONObject("patent-right") != null) {
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
			}	
			String contextUrl = patentObj.optJSONObject("link").optString("patentpubxml-url");
			if (StringUtils.isNULL(contextUrl)==false) {
				getContext(patent, contextUrl);
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
					
			getPatentAlteration(patent);
					
			if (patent.getListApplicant().isEmpty()) {
				List<Applicant> listAppl = new ArrayList<Applicant>();
				JSONArray appl = patentObj.optJSONObject("parties").optJSONArray("applicants");
						
				for (int applIndex = 0; applIndex < appl.length(); applIndex++) {
					JSONObject applObj = appl.optJSONObject(applIndex);
					Applicant applicant = new Applicant();
					applicant.setApplicant_name(applObj.optString("chinese-name"));
					applicant.setApplicant_name_en(applObj.optString("english-name"));
					applicant.setCountry_id(applObj.optString("english-country"));
					applicant.setCountry_name(applObj.optString("chinese-country"));
					applicant.setApplicant_address(applObj.optString("address"));
					applicant.setApplicant_address_en(applObj.optString("english-address"));
					applicant.setApplicant_order(applObj.optInt("-sequence"));
					applicant.setPatent(patent);
					listAppl.add(applicant);
				}
				patent.setListApplicant(listAppl);
			}
					
			//get ipc
			List<IPCClass> listIPCClass = new ArrayList<IPCClass>();
			JSONArray ipcs = patentObj.optJSONArray("classification-ipc");
			for (int ipcIndex = 0; ipcIndex < ipcs.length(); ipcIndex++) {
				JSONObject ipcObj = ipcs.optJSONObject(ipcIndex);
				if (!StringUtils.isNULL(ipcObj.optString("ipc-full"))) {
					IPCClass ipc = new IPCClass();
					ipc.setIpc_class_id(ipcObj.optString("ipc-section")+
									ipcObj.optString("ipc-class") + ipcObj.optString("ipc-subclass") +
									" " + ipcObj.optString("ipc-main-group") + "/" + ipcObj.optString("ipc-group"));
					if (ipcObj.optString("ipc-version").startsWith("0")) {
						ipc.setIpc_version("20"+ipcObj.optString("ipc-version")+"01");
					} else {
						ipc.setIpc_version(ipcObj.optString("ipc-version"));
					}
					listIPCClass.add(ipc);
				}
			}
			patent.setListIPC(listIPCClass);
		}
	}
	
	private static void getPatentAlteration(Patent patent) {
		String url = Constants.PATENT_WEB_SERVICE_TW+"/PatentAlteration?format=json&tk=%s&applno=%s";
		url = String.format(url, Constants.PATENT_KEY_TW ,patent.getPatent_appl_no());
		
		try {
			String context = HttpRequestUtils.sendGet(url);

			List<Applicant> listAppl = new ArrayList<Applicant>();
			if (!StringUtils.isNULL(context)) {
				JSONObject getObject = new JSONObject(HttpRequestUtils.sendGet(url));
				if (getObject.has("patentcontent")) {
					JSONArray patentContent = getObject.optJSONArray("patentcontent");
					JSONObject alterationObj = patentContent.optJSONObject(0);
					JSONArray appls = alterationObj.optJSONArray("alteration-id1s");
					for (int index = 0; index < appls.length(); index++) {
						JSONObject appl = appls.optJSONObject(index);
						Applicant applicant = new Applicant();
						applicant.setApplicant_name(appl.optString("alteration-name1"));
						applicant.setApplicant_order(appl.optInt("-sequence"));
						applicant.setPatent(patent);
						listAppl.add(applicant);
					}
				}
			}
			patent.setListApplicant(listAppl);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static void convertPatentContextXmlCh(Patent patent, String content) {
		PatentAbstract pa = new PatentAbstract();
		PatentClaim pc = new PatentClaim();
		PatentDescription pd = new PatentDescription();
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
			pa.setContext_abstract(abstractStr);
			
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
			pd.setContext_desc(descStr);
			
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
			
			pc.setContext_claim(claimStr);
			
			if (!StringUtils.isNULL(pa.getContext_abstract())) {
				pa.setPatent(patent);
				patent.setPatentAbstract(pa);
			}
			
			if (!StringUtils.isNULL(pc.getContext_claim())) {
				pc.setPatent(patent);
				patent.setPatentClaim(pc);
			}
			
			if (!StringUtils.isNULL(pd.getContext_desc())) {
				pd.setPatent(patent);
				patent.setPatentDesc(pd);
			}
			
		}catch(Exception e){
			log.error(e.getMessage());
		}
	}
	
	private static void getContext(Patent patent, String link) {
		try {
			String content = FtpRequestUtils.sendGet(link);
			if ("XML".equals(getCtxType(content))) {
				convertPatentContextXmlCh(patent, content);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
