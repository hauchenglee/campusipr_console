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
	
	public static int parseBilbo_byApplication(Patent patent) {
		String formatType = "docdb";
		String url = Constants.PATENT_WEB_SERVICE_EU+"/rest-services/published-data/application/%s/%s/biblio";
		url = String.format(url, formatType, patent.getPatent_appl_no());
		log.info("url parse: " + url);
		
		try {
			String content = (HttpRequestUtils.sendGetByToken(url, generateToken("Basic "+Constants.PATENT_TOKEN_EU)));
			if (!StringUtils.isNULL(content)) {
				convertPatentInfoXml(patent ,content);
				return Constants.INT_SUCCESS;
			} else {
				return Constants.INT_CANNOT_FIND_DATA;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Constants.INT_SYSTEM_PROBLEM;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Constants.INT_SYSTEM_PROBLEM;
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
			
			//Patent_appl_no & Patent_appl_date
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
//								patent.setPatent_no("ZL"+applNo);
//								log.info("中國證書號"+patent.getPatent_no());
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
			
			//Patent_notice_no & Patent_publish_no
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
								Node pubCountryNode = cElement.getElementsByTagName("country").item(0);
								Node pubNode = cElement.getElementsByTagName("doc-number").item(0);
								Node pubKindNode = cElement.getElementsByTagName("kind").item(0);
								Node pubDateNode = cElement.getElementsByTagName("date").item(0);
								String countryId = pubCountryNode.getTextContent().toString().toUpperCase();
								String pubKindNo = pubKindNode.getTextContent().toString().toUpperCase();
								String publicateNo = pubNode.getTextContent().substring(0, pubNode.getTextContent().length());
								
								log.info(pubKindNo);
								try {
									String publishDateStr = pubDateNode.getTextContent();
									// 發明類型。註:1985-1992 kind B是審定號非公告號，但調整後類同，請查詢中國申請號體系
									if ("A".equals(pubKindNo)) {
										log.info("發明類型Kind A 公開號: " + publicateNo);
										patent.setPatent_notice_no(countryId + publicateNo);
									}
									if ("B".equals(pubKindNo)) {
										log.info("發明類型Kind B 公告號: " + publicateNo);
										if (publicateNo.length() > 9) {
											publicateNo = publicateNo.substring(0, publicateNo.length() - 1);
										}
										patent.setPatent_publish_no(countryId + publicateNo + pubKindNo);
									}
									// 實用新型只需放公告號/日
									if ("U".equals(pubKindNo)) {
										log.info("實用新型 kind U 公告號: " + publicateNo);
										if (publicateNo.length() > 9) {
											publicateNo = publicateNo.substring(0, publicateNo.length() - 1);
										}
										patent.setPatent_publish_no(countryId + publicateNo + pubKindNo);
										log.info(patent.getPatent_publish_no());
									}
									//1993-20040630的發明類型的公告號/日
									if ("C".equals(pubKindNo)) {
										log.info("實用新型 kind C 公告號: " + publicateNo);
										if (publicateNo.length() > 9) {
											publicateNo = publicateNo.substring(0, publicateNo.length() - 1);
										}
										patent.setPatent_publish_no(countryId + publicateNo + pubKindNo);
									}
									//1993-20040630的實用新型的公告號/日
									if ("Y".equals(pubKindNo)) {
										log.info("實用新型 kind Y 公告號: " + publicateNo);
										if (publicateNo.length() > 9) {
											publicateNo = publicateNo.substring(0, publicateNo.length() - 1);
										}
										patent.setPatent_publish_no(countryId + publicateNo + pubKindNo);
									}
									//1993-20040630的外觀設計的公告號/日
									if ("D".equals(pubKindNo)) {
										log.info("實用新型 kind D 公告號: " + publicateNo);
										if (publicateNo.length() > 9) {
											publicateNo = publicateNo.substring(0, publicateNo.length() - 1);
										}
										patent.setPatent_publish_no(countryId + publicateNo + pubKindNo);
									}
									//20040701迄今的外觀設計的公告號/日
									if ("S".equals(pubKindNo)) {
										log.info("實用新型 kind S 公告號: " + publicateNo);
										if (publicateNo.length() > 9) {
											publicateNo = publicateNo.substring(0, publicateNo.length() - 1);
										}
										patent.setPatent_publish_no(countryId + publicateNo + pubKindNo);
									}
									
									Calendar calendar = Calendar.getInstance();
									calendar.setTime(patent.getPatent_appl_date());
									
									if (!StringUtils.isNULL(publishDateStr)) {
										Date publishDate = DateUtils.parserSimpleDateFormatDate(publishDateStr);
										if ("A".equals(pubKindNo)) {
											log.info("發明類型Kind A 公開日: " + publishDate);
											patent.setPatent_notice_date(publishDate);
										}
										if ("B".equals(pubKindNo)) {
											log.info("發明類型Kind B 公告日: " + publishDate);
											patent.setPatent_publish_date(publishDate);
											
											calendar.add(Calendar.DATE, -1);
											calendar.add(Calendar.YEAR, 20);
											Date edate=calendar.getTime();
											patent.setPatent_edate(edate);
											log.info("edate: "+patent.getPatent_edate());
										}
										if ("U".equals(pubKindNo)) {
											log.info("實用新型 kind U 公告日: " + publishDate);
											patent.setPatent_publish_date(publishDate);
											
											calendar.add(Calendar.DATE, -1);
											calendar.add(Calendar.YEAR, 10);
											Date edate=calendar.getTime();
											patent.setPatent_edate(edate);
											log.info("edate: "+patent.getPatent_edate());
										}
										if ("C".equals(pubKindNo)) {
											log.info("實用新型 kind C 公告日: " + publishDate);
											patent.setPatent_publish_date(publishDate);
											
											calendar.add(Calendar.DATE, -1);
											calendar.add(Calendar.YEAR, 20);
											Date edate=calendar.getTime();
											patent.setPatent_edate(edate);
											log.info("edate: "+patent.getPatent_edate());
										}
										if ("Y".equals(pubKindNo)) {
											log.info("實用新型 kind Y 公告日: " + publishDate);
											patent.setPatent_publish_date(publishDate);
											
											calendar.add(Calendar.DATE, -1);
											calendar.add(Calendar.YEAR, 10);
											Date edate=calendar.getTime();
											patent.setPatent_edate(edate);
											log.info("edate: "+patent.getPatent_edate());
										}
										if ("D".equals(pubKindNo)) {
											log.info("實用新型 kind D 公告日: " + publishDate);
											patent.setPatent_publish_date(publishDate);
											
											calendar.add(Calendar.DATE, -1);
											calendar.add(Calendar.YEAR, 10);
											Date edate=calendar.getTime();
											patent.setPatent_edate(edate);
											log.info("edate: "+patent.getPatent_edate());
										}
										if ("S".equals(pubKindNo)) {
											log.info("實用新型 kind S 公告日: " + publishDate);
											patent.setPatent_publish_date(publishDate);
											
											calendar.add(Calendar.DATE, -1);
											calendar.add(Calendar.YEAR, 10);
											Date edate=calendar.getTime();
											patent.setPatent_edate(edate);
											log.info("edate: "+patent.getPatent_edate());
										}
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
			
			//inventor
				NodeList inventorsList = doc.getElementsByTagName("inventors");
				int epodocNameEnCount = 0;
				String inventorNameEn = null;
					List<Inventor> listInventor = new ArrayList<Inventor>();
					List<String> listInventorEn = new ArrayList<String>();
					List<String> duplicateInventor = new ArrayList<>();
					//符合NodeList條件通常有1至2筆，以有data-format="epodoc"的List為基準，該List通常為第一筆，故為item(0)
					Node nNode = inventorsList.item(0);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element nElement = (Element) nNode;
						NodeList inventorList = nElement.getElementsByTagName("inventor");
						for (int epodocCount = 0; epodocCount < inventorList.getLength(); epodocCount++) {
							Element inventor = (Element) inventorList.item(epodocCount);
							if ("epodoc".equals(inventor.getAttribute("data-format"))) {
								epodocNameEnCount++;
								NodeList nameEnList = inventor.getElementsByTagName("name");
								inventorNameEn = nameEnList.item(0).getTextContent().replace(",", "");
								listInventorEn.add(inventorNameEn);
//								log.info("epodocCount" + epodocCount+": " +listInventorEn.get(epodocCount));
							}
						}
						
						// 第一種epodoc=original，epodoc全英文、original全中文的情況
						// 如果以後出現中英數量不相等的情況，就要修正
						int originalCount = 0;
						if((epodocNameEnCount*2)==inventorList.getLength()) {
							log.info("inventor: epodoc==original");
							for(int inventorCount = 0;inventorCount < inventorList.getLength(); inventorCount++) {
								Element inventor = (Element) inventorList.item(inventorCount);
								String inventorName = null;	
								Inventor inv = new Inventor();
								if("original".equals(inventor.getAttribute("data-format"))) {
//									log.info("originalCount" + inventorCount +": " +listInventorEn.get(originalCount));
									inventorNameEn = listInventorEn.get(originalCount);
									NodeList nameList = inventor.getElementsByTagName("name");
									inventorName = nameList.item(0).getTextContent().replace(",", "");
									inv.setInventor_name(inventorName);
									inv.setInventor_name_en(inventorNameEn);
									inv.setInventor_order(Integer.parseInt(inventor.getAttribute("sequence")));
									listInventor.add(inv);
									inv.setPatent(patent);
									originalCount++;
								}
							}
						}
						
						//第二種epodoc*2=original，epodoc全英文、original中文、英文加在一起的情況
						if(epodocNameEnCount!=0) {
							for (int inventorCount = epodocNameEnCount; inventorCount < inventorList.getLength(); inventorCount++) {
								Element inventor = (Element) inventorList.item(inventorCount);
								Inventor inv = new Inventor();
								if ("original".equals(inventor.getAttribute("data-format"))) {
									NodeList nameEnList = inventor.getElementsByTagName("name");
									if((inventorCount+epodocNameEnCount)<inventorList.getLength()) {
										log.info("inventor: epodoc*2==original");
										NodeList nameList = ((Element) inventorList.item(inventorCount+epodocNameEnCount)).getElementsByTagName("name");
										if (!duplicateInventor.contains(nameList.item(0).getTextContent().replace(",", ""))) {
											if (Integer.parseInt(inventor.getAttribute("sequence")) <= epodocNameEnCount) {
												inv.setInventor_name_en(nameEnList.item(0).getTextContent().replace(",", ""));
												inv.setInventor_name(nameList.item(0).getTextContent().replace(",", ""));
												inv.setInventor_order(Integer.parseInt(inventor.getAttribute("sequence")));
												listInventor.add(inv);
												inv.setPatent(patent);
												duplicateInventor.add(nameList.item(0).getTextContent().replace(",", ""));
											}
										}
									}
								}
							}
						}
						//第三種沒有epodoc
						if(epodocNameEnCount==0) {
							for (int inventorCount = 0; inventorCount < inventorList.getLength(); inventorCount++) {
								Element inventor = (Element) inventorList.item(inventorCount);
								String inventorName = null;	
								Inventor inv = new Inventor();
								if ("original".equals(inventor.getAttribute("data-format"))) {
									NodeList nameList = inventor.getElementsByTagName("name");
									inventorName = nameList.item(0).getTextContent().replace(",", "");
									inv.setInventor_name(inventorName);
									listInventor.add(inv);
									inv.setPatent(patent);
								}
							}
						}
						
						patent.setListInventor(listInventor);
					}

			//applicant & Assignee
			NodeList applicantsList = doc.getElementsByTagName("applicants");
			int epodocApplCount = 0;
			String applicantNameEn= null;
			String applicantName = null;
				List<Assignee> listAssignee = new ArrayList<Assignee>();
				List<Applicant> listAppl = new ArrayList<Applicant>();
				List<String> listApplNameEn = new ArrayList<>();
				List<String> duplicateAppl = new ArrayList<>();
				Node aNode = applicantsList.item(0);
				if (aNode.getNodeType() == Node.ELEMENT_NODE) {
					Element nElement = (Element) aNode;
					NodeList applicantList = nElement.getElementsByTagName("applicant");
					for (int epodocCount = 0; epodocCount < applicantList.getLength(); epodocCount++) {
						Element applicant = (Element) applicantList.item(epodocCount);
						if ("epodoc".equals(applicant.getAttribute("data-format"))) {
							epodocApplCount++;
							NodeList nameEnList = applicant.getElementsByTagName("name");
							applicantNameEn = nameEnList.item(0).getTextContent().replace(",", "");
							listApplNameEn.add(applicantNameEn);
						}
					}
					// 第一種epodoc=original，epodoc全英文、original全中文的情況
					// 如果以後出現中英數量不相等的情況，就要修正
					int originalApplCount = 0;
					if((epodocApplCount*2)==applicantList.getLength()) {
						log.info("applicant: epodoc==original");
						for(int applCount = 0; applCount < applicantList.getLength(); applCount++) {
							Element applicant = (Element) applicantList.item(applCount);
							if ("original".equals(applicant.getAttribute("data-format"))) {
								NodeList nameList = applicant.getElementsByTagName("name");
								applicantName = nameList.item(0).getTextContent().replace(",", "");
								applicantNameEn = listApplNameEn.get(originalApplCount);
								Applicant appl = new Applicant();
								Assignee assign = new Assignee();
								
								assign.setAssignee_name(applicantName);
								assign.setAssignee_name_en(applicantNameEn);
								assign.setAssignee_order(Integer.parseInt(applicant.getAttribute("sequence")));
								assign.setPatent(patent);
								
								appl.setApplicant_name(applicantName);
								appl.setApplicant_name_en(applicantNameEn);
								appl.setApplicant_order(Integer.parseInt(applicant.getAttribute("sequence")));
								appl.setPatent(patent);
								
//								log.info("applicantName"+applicantName);
//								log.info("applicantNameEn"+applicantNameEn);
								
								listAppl.add(appl);
								listAssignee.add(assign);
								originalApplCount++;
							}
						}
					}
					//第二種epodoc*2=original，epodoc全英文、original中文、英文加在一起的情況
					for (int applCount = epodocApplCount; applCount < applicantList.getLength(); applCount++) {
						Element applicant = (Element) applicantList.item(applCount);
						if((applCount+epodocApplCount)<applicantList.getLength()) {
							log.info("applicant: epodoc*2==original");
							NodeList nameList = ((Element) applicantList.item(applCount + epodocApplCount)).getElementsByTagName("name");
							NodeList nameEnList = applicant.getElementsByTagName("name");
							if (!duplicateAppl.contains(nameList.item(0).getTextContent())) {
								Assignee assign = new Assignee();
								Applicant appl = new Applicant();
								assign.setAssignee_name(nameList.item(0).getTextContent().replace(",", ""));
								assign.setAssignee_name_en(nameEnList.item(0).getTextContent().replace(",", ""));
								assign.setAssignee_order(Integer.parseInt(applicant.getAttribute("sequence")));
								assign.setPatent(patent);

								appl.setApplicant_name(nameList.item(0).getTextContent().replace(",", ""));
								appl.setApplicant_name_en(nameEnList.item(0).getTextContent().replace(",", ""));
								appl.setApplicant_order(Integer.parseInt(applicant.getAttribute("sequence")));
								appl.setPatent(patent);
//								log.info("applicantName"+nameList.item(0).getTextContent().replace(",", ""));
//								log.info("applicantNameEn"+nameEnList.item(0).getTextContent().replace(",", ""));
								
								
								listAssignee.add(assign);
								listAppl.add(appl);
								duplicateAppl.add(nameList.item(0).getTextContent());
							}
						}
					}
					patent.setListAssignee(listAssignee);
					patent.setListApplicant(listAppl);
				}


			PatentAbstract patentAbstract = new PatentAbstract();
			NodeList abstractList = doc.getElementsByTagName("abstract");
			for (int temp = 0; temp < abstractList.getLength(); temp++) {
				Node abNode = abstractList.item(temp);
				patentAbstract.setContext_abstract(abNode.getTextContent());
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
