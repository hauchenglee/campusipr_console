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
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import biz.mercue.campusipr.model.Applicant;
import biz.mercue.campusipr.model.Assignee;
import biz.mercue.campusipr.model.Inventor;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentContext;
import biz.mercue.campusipr.service.PatentService;

public class ServiceChinaPatent {
	
	private static  Logger log = Logger.getLogger(ServiceChinaPatent.class.getName());
	
	public static Patent getPatentRightByApplicantNo(String applNo) {
		String url = Constants.PATENT_WEB_SERVICE_EU+"/rest-services/published-data/search?q=%s";
		try {
			url = String.format(url,URLEncoder.encode("ap="+applNo, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		List<Patent> patentList = new ArrayList<Patent>();
		try {
			String content = (HttpRequestUtils.sendGetByToken(url, generateToken("Basic "+Constants.PATENT_TOKEN_EU)));
			if (StringUtils.isNULL(content) == false) {
				patentList = convertPatentIdXml(content);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (patentList.size() > 0) {
			return patentList.get(patentList.size()-1);
		} else {
			return null;
		}
	}
	
	public static List<Patent> getPatentRightByAssigneeName(String assigneeName) {
		String url = Constants.PATENT_WEB_SERVICE_EU+"/rest-services/published-data/search?q=%s";
		try {
			url = String.format(url,URLEncoder.encode("pa="+assigneeName, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		List<Patent> patentList = new ArrayList<Patent>();
		try {
			String content = (HttpRequestUtils.sendGetByToken(url, generateToken("Basic "+Constants.PATENT_TOKEN_EU)));
			if (StringUtils.isNULL(content) == false) {
				patentList = convertPatentIdXml(content);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return patentList;
	}
	
	private static List<Patent> convertPatentIdXml(String content) {
		List<Patent> patentList = new ArrayList<Patent>();
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
			NodeList documentList = doc.getElementsByTagName("document-id");
			for (int temp = 0; temp < documentList.getLength(); temp++) {
				Node nNode = documentList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) { 
					Element eElement = (Element) nNode;
					String formatType = eElement.getAttribute("document-id-type");
					String countryId = null;
					String docId = null;
					NodeList cNodes = eElement.getChildNodes();
					for (int childIndex = 0; childIndex < cNodes.getLength(); childIndex++) {
						Node cNode = cNodes.item(childIndex);
						if (cNode.getNodeType() == Node.ELEMENT_NODE) { 
							Element cElement = (Element) cNode;
							if ("country".equals(cElement.getNodeName())) {
								countryId = cElement.getTextContent();
							}
							if ("doc-number".equals(cElement.getNodeName())) {
								docId = cElement.getTextContent();
							}
						}
					}
					Patent patent = parserBilbo(countryId+docId, formatType);
					patentList.add(patent);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return patentList;
	}
	
	private static Patent parserBilbo(String patentNo, String formatType) {
		String url = Constants.PATENT_WEB_SERVICE_EU+"/rest-services/published-data/publication/%s/%s/biblio";
		url = String.format(url, formatType, patentNo);
		
		Patent patent = new Patent();
		try {
			String content = (HttpRequestUtils.sendGetByToken(url, generateToken("Basic "+Constants.PATENT_TOKEN_EU)));
			patent = convertPatentInfoXml(content);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return patent;
	}
	
	private static Patent convertPatentInfoXml(String content) {
		Patent patent = new Patent();
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
			
			patent.setPatent_appl_country("CN");
			
			NodeList titleList = doc.getElementsByTagName("invention-title");
			for (int temp = 0; temp < titleList.getLength(); temp++) {
				Node nNode = titleList.item(temp);
				patent.setPatent_name(nNode.getTextContent());
			}
			
			NodeList publicationList = doc.getElementsByTagName("publication-reference");
			for (int temp = 0; temp < publicationList.getLength(); temp++) {
				Node nNode = publicationList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) { 
					NodeList cNodeList = nNode.getChildNodes();
					for (int tempC = 0; tempC < cNodeList.getLength(); tempC++) {
						Node cNode = cNodeList.item(tempC);
						if (cNode.getNodeType() == Node.ELEMENT_NODE) { 
							Element cElement = (Element) cNode;
							if ("docdb".equals(cElement.getAttribute("document-id-type"))) {
								Node pubNode = cElement.getElementsByTagName("doc-number").item(0);
								Node pubDateNode = cElement.getElementsByTagName("date").item(0);
								String publicateNo = pubNode.getTextContent().substring(0, pubNode.getTextContent().length());
								if (publicateNo.length() > 9) {
									publicateNo = publicateNo.substring(0, publicateNo.length()-1);
								}
								patent.setPatent_publish_no(publicateNo);
								patent.setPatent_notice_no(publicateNo);
								try {
									String publishDateStr =pubDateNode.getTextContent();
									if (StringUtils.isNULL(publishDateStr) == false) {
										Date publishDate = DateUtils.parserSimpleDateFormatDate(publishDateStr);
										patent.setPatent_publish_date(publishDate);
										patent.setPatent_notice_date(publishDate);
									}
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
			
			NodeList applicationList = doc.getElementsByTagName("application-reference");
			for (int temp = 0; temp < applicationList.getLength(); temp++) {
				Node nNode = applicationList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) { 
					NodeList cNodeList = nNode.getChildNodes();
					for (int tempC = 0; tempC < cNodeList.getLength(); tempC++) {
						Node cNode = cNodeList.item(tempC);
						if (cNode.getNodeType() == Node.ELEMENT_NODE) { 
							Element cElement = (Element) cNode;
							if ("epodoc".equals(cElement.getAttribute("document-id-type"))) {
								Node applNode = cElement.getElementsByTagName("doc-number").item(0);
								Node applDateNode = cElement.getElementsByTagName("date").item(0);
								String applNo = applNode.getTextContent().substring(0, applNode.getTextContent().length());
								if (applNo.length() > 12) {
									applNo = applNo.substring(0, applNo.length()-1);
								}
								patent.setPatent_appl_no(applNo);
								patent.setPatent_no(applNo);
								try {
									String publishDateStr =applDateNode.getTextContent();
									if (StringUtils.isNULL(publishDateStr) == false) {
										Date publishDate = DateUtils.parserSimpleDateFormatDate(publishDateStr);
										patent.setPatent_appl_date(publishDate);
									}
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
			
			
			NodeList inventorsList = doc.getElementsByTagName("inventors");
			for (int temp = 0; temp < inventorsList.getLength(); temp++) {
				List<Inventor> listInventor = new ArrayList<Inventor>();
				Node nNode = inventorsList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) { 
					Element nElement = (Element) nNode;
					NodeList inventorList = nElement.getElementsByTagName("inventor");
					for (int temp1 = 0; temp1 < inventorList.getLength(); temp1++) {
						Element inventor = (Element) inventorList.item(temp1);
						NodeList nameList = inventor.getElementsByTagName("name");
						Inventor inv = new Inventor();
						inv.setInventor_name(nameList.item(0).getTextContent().replace(",", ""));
						inv.setInventor_order(Integer.parseInt(inventor.getAttribute("sequence")));
						inv.setPatent(patent);
						listInventor.add(inv);
					}
				}
				patent.setListInventor(listInventor);
			}
			
			NodeList applicantsList = doc.getElementsByTagName("applicants");
			for (int temp = 0; temp < applicantsList.getLength(); temp++) {
				List<Assignee> listAssignee = new ArrayList<Assignee>();
				List<Applicant> listAppl = new ArrayList<Applicant>();
				Node nNode = applicantsList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) { 
					Element nElement = (Element) nNode;
					NodeList applicantList = nElement.getElementsByTagName("applicant");
					for (int temp1 = 0; temp1 < applicantList.getLength(); temp1++) {
						Element applicant = (Element) applicantList.item(temp1);
						NodeList nameList = applicant.getElementsByTagName("name");
						Assignee assign = new Assignee();
						Applicant appl = new Applicant();
						assign.setAssignee_name(nameList.item(0).getTextContent());
						assign.setAssignee_order(Integer.parseInt(applicant.getAttribute("sequence")));
						assign.setPatent(patent);
						appl.setApplicant_name_en(nameList.item(0).getTextContent());
						appl.setApplicant_order(Integer.parseInt(applicant.getAttribute("sequence")));
						appl.setPatent(patent);
						listAssignee.add(assign);
						listAppl.add(appl);
					}
				}
				patent.setListApplicant(listAppl);
				patent.setListAssignee(listAssignee);
			}
			
			PatentContext patentContext = new PatentContext();
			NodeList abstractList = doc.getElementsByTagName("abstract");
			for (int temp = 0; temp < abstractList.getLength(); temp++) {
				Node nNode = abstractList.item(temp);
				patentContext.setContext_abstract(nNode.getTextContent());
			}
			if (!StringUtils.isNULL(patentContext.getContext_claim()) 
					|| !StringUtils.isNULL(patentContext.getContext_abstract())
					|| !StringUtils.isNULL(patentContext.getContext_desc())) {
				patentContext.setPatent(patent);
				patent.setPatentContext(patentContext);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return patent;
	}
	
	private static String generateToken(String token) {
		String url = Constants.PATENT_WEB_SERVICE_EU+"/auth/accesstoken";
		String authToken = null;
		try {
			String param = "grant_type=client_credentials";
			JSONObject contentObj = new JSONObject(HttpRequestUtils.sendPostByToken(url, param, token));
			authToken = "Bearer "+contentObj.optString("access_token");
			log.info("AuthTokn:"+authToken);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return authToken;
	}

}
