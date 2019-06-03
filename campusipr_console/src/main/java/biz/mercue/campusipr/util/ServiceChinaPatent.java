package biz.mercue.campusipr.util;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
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

import biz.mercue.campusipr.model.Applicant;
import biz.mercue.campusipr.model.Assignee;
import biz.mercue.campusipr.model.IPCClass;
import biz.mercue.campusipr.model.Inventor;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentAbstract;
import biz.mercue.campusipr.service.PatentService;

public class ServiceChinaPatent {
	
	private static  Logger log = Logger.getLogger(ServiceChinaPatent.class.getName());
	
	public static List<Patent> getPatentRightByAssignee(List<String> assigneeNames, List<String> dupucateStr) {
		//同義詞字串列表進入
		List<Patent> list = new ArrayList<>();
		for (String assignee:assigneeNames) {
			if (!StringUtils.hasChinese(assignee)) {
				String url = Constants.PATENT_WEB_SERVICE_EU+"/rest-services/published-data/search?q=%s";
				try {
					url = String.format(url,URLEncoder.encode("pa="+assignee, "UTF-8"));
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				try {
					String token = generateToken("Basic "+Constants.PATENT_TOKEN_EU);
					if (token != null) {
						String context = (HttpRequestUtils.sendGetByToken(url, generateToken("Basic "+Constants.PATENT_TOKEN_EU)));
						if (!StringUtils.isNULL(context)) {
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
								is.setCharacterStream(new StringReader(context));
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
										if (!dupucateStr.contains(countryId+docId)) {
											Patent patent = new Patent();
											patent.setPatent_publish_no(countryId+docId);
											patent.setPatent_appl_country(countryId.toLowerCase());
											parserBilbo(patent, formatType);
											list.add(patent);
											dupucateStr.add(countryId+docId);
										}
									}
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
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
			}
		}
		return list;
	}
	
	public static void getPatentRightByApplicantNo(Patent patent) {
		String url = Constants.PATENT_WEB_SERVICE_EU+"/rest-services/published-data/search?q=%s";
		try {
			url = String.format(url,URLEncoder.encode("ap=", "UTF-8")+patent.getPatent_appl_no());
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			String content = (HttpRequestUtils.sendGetByToken(url, generateToken("Basic "+Constants.PATENT_TOKEN_EU)));
			if (!StringUtils.isNULL(content)) {
				convertPatentIdXml(patent, content);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void convertPatentIdXml(Patent patent, String content) {
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
					patent.setPatent_appl_country(countryId.toLowerCase());
					patent.setPatent_publish_no(countryId+docId);
					parserBilbo(patent, formatType);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void parseBilbo_byApplication(Patent patent) {
		String formatType = "docdb";
		String url = Constants.PATENT_WEB_SERVICE_EU+"/rest-services/published-data/application/%s/%s/biblio";
		url = String.format(url, formatType, patent.getPatent_appl_no());
		log.info("url parse: " + url);
		
		try {
			String content = (HttpRequestUtils.sendGetByToken(url, generateToken("Basic "+Constants.PATENT_TOKEN_EU)));
			if (!StringUtils.isNULL(content)) {
				convertPatentInfoXml(patent ,content);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void parserBilbo(Patent patent, String formatType) {
		String url = Constants.PATENT_WEB_SERVICE_EU+"/rest-services/published-data/publication/%s/%s/biblio";
		url = String.format(url, formatType, patent.getPatent_appl_country().toUpperCase() + patent.getPatent_publish_no());
		
		try {
			String content = (HttpRequestUtils.sendGetByToken(url, generateToken("Basic "+Constants.PATENT_TOKEN_EU)));
			if (!StringUtils.isNULL(content)) {
				convertPatentInfoXml(patent ,content);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void convertPatentInfoXml(Patent patent, String content) {
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
									if (!StringUtils.isNULL(publishDateStr)) {
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
								patent.setPatent_appl_no(applNo);
								patent.setPatent_no(applNo);
								try {
									String publishDateStr =applDateNode.getTextContent();
									if (!StringUtils.isNULL(publishDateStr)) {
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
			
			NodeList ipcrList = doc.getElementsByTagName("classification-ipcr");
			List<IPCClass> listIPC = new ArrayList<IPCClass>();
			List<String> duplicateIpc = new ArrayList<>();
			for (int temp = 0; temp < ipcrList.getLength(); temp++) {
				Node nNode = ipcrList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) { 
					Element eElement = (Element) nNode;

					String ipcId = eElement.getTextContent().replaceAll("\\s+","")
							.substring(0, eElement.getTextContent().replaceAll("\\s+","").length()-2);
					ipcId = ipcId.substring(0,4)+" "+ipcId.substring(4,ipcId.length());
					if (!duplicateIpc.contains(ipcId)) {
						IPCClass ipc = new IPCClass();
						ipc.setIpc_class_id(ipcId);
						int year = Calendar.getInstance().get(Calendar.YEAR);
						ipc.setIpc_version(Integer.toString(year)+"01");
						listIPC.add(ipc);
						duplicateIpc.add(ipcId);
					}
				}
			}
			patent.setListIPC(listIPC);
			
			NodeList inventorsList = doc.getElementsByTagName("inventors");
			for (int temp = 0; temp < inventorsList.getLength(); temp++) {
				List<Inventor> listInventor = new ArrayList<Inventor>();
				List<String> duplicateInventor = new ArrayList<>();
				Node nNode = inventorsList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) { 
					Element nElement = (Element) nNode;
					NodeList inventorList = nElement.getElementsByTagName("inventor");
					for (int temp1 = 0; temp1 < inventorList.getLength(); temp1++) {
						Element inventor = (Element) inventorList.item(temp1);
						NodeList nameList = inventor.getElementsByTagName("name");
						if (!duplicateInventor.contains(nameList.item(0).getTextContent().replace(",", ""))) {
							Inventor inv = new Inventor();
							inv.setInventor_name(nameList.item(0).getTextContent().replace(",", ""));
							inv.setInventor_order(Integer.parseInt(inventor.getAttribute("sequence")));
							inv.setPatent(patent);
							listInventor.add(inv);
							duplicateInventor.add(nameList.item(0).getTextContent().replace(",", ""));
						}
					}
				}
				patent.setListInventor(listInventor);
			}
			
			NodeList applicantsList = doc.getElementsByTagName("applicants");
			for (int temp = 0; temp < applicantsList.getLength(); temp++) {
				List<Assignee> listAssignee = new ArrayList<Assignee>();
				List<Applicant> listAppl = new ArrayList<Applicant>();
				List<String> duplicateAppl = new ArrayList<>();
				Node nNode = applicantsList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) { 
					Element nElement = (Element) nNode;
					NodeList applicantList = nElement.getElementsByTagName("applicant");
					for (int temp1 = 0; temp1 < applicantList.getLength(); temp1++) {
						Element applicant = (Element) applicantList.item(temp1);
						NodeList nameList = applicant.getElementsByTagName("name");
						if (!duplicateAppl.contains(nameList.item(0).getTextContent())) {
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
							duplicateAppl.add(nameList.item(0).getTextContent());
						}
					}
				}
				patent.setListApplicant(listAppl);
				patent.setListAssignee(listAssignee);
			}
			
			PatentAbstract patentAbstract = new PatentAbstract();
			NodeList abstractList = doc.getElementsByTagName("abstract");
			for (int temp = 0; temp < abstractList.getLength(); temp++) {
				Node nNode = abstractList.item(temp);
				patentAbstract.setContext_abstract(nNode.getTextContent());
			}
			if (!StringUtils.isNULL(patentAbstract.getContext_abstract())) {
				patentAbstract.setPatent(patent);
				patent.setPatentAbstract(patentAbstract);
			}
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
