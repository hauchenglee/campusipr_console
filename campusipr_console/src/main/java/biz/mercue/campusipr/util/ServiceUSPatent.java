package biz.mercue.campusipr.util;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import biz.mercue.campusipr.model.Applicant;
import biz.mercue.campusipr.model.Assignee;
import biz.mercue.campusipr.model.IPCClass;
import biz.mercue.campusipr.model.Inventor;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentAbstract;
import biz.mercue.campusipr.model.PatentClaim;
import biz.mercue.campusipr.model.PatentDescription;
import biz.mercue.campusipr.model.View;

public class ServiceUSPatent {
	
	private static  Logger log = Logger.getLogger(ServiceUSPatent.class.getName());
	
	public static List<Patent> getPatentRightByAssignee(List<String> applicantNames, List<String> dupucateStr) {
		//同義詞字串列表進入
		List<Patent> list = new ArrayList<>();
		for (String applicant:applicantNames) {
			if (!StringUtils.hasChinese(applicant)) {
				String url = Constants.PATENT_WEB_SERVICE_US+"?applicant=%s";
				url = String.format(url, URLEncoder.encode(applicant));
				try {
					String context = HttpRequestUtils.sendGet(url);
					if (!StringUtils.isNULL(context)) {
						JSONObject getObject = new JSONObject(context);
						JSONArray patentDocsObj = getObject.optJSONObject("response").optJSONArray("docs");
						if (patentDocsObj.length() != 0) {
							for (int index = 0; index < patentDocsObj.length(); index++) {
								JSONObject patentObj = patentDocsObj.optJSONObject(index);
								//check applicant is right
								boolean checkApplicant = false;
								JSONArray patentApplicants = patentObj.optJSONArray("applicant");
								if (patentApplicants != null) {
									for (int applIndex = 0; applIndex < patentApplicants.length(); applIndex++) {
										String appl = patentApplicants.optString(applIndex);
										if (appl.contains(applicant)) {
											checkApplicant = true;
										}
									}
								}
								if (checkApplicant) {
									if (!dupucateStr.contains(patentObj.optString("applicationNumber"))) {
										Patent patent = new Patent();
										convertPatentInfoUS(patent, patentObj);
										parserBilbo(patent);
										list.add(patent);
										dupucateStr.add(patent.getPatent_appl_no());
									}
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
		}
		return list;
	}
	
	public static void getPatentRightByapplNo(Patent patent) {
		String url = Constants.PATENT_WEB_SERVICE_US+"?applicationNumber=%s";
		url = String.format(url, patent.getPatent_appl_no());
		
		try {
			String context = HttpRequestUtils.sendGet(url);
			if (!StringUtils.isNULL(context)) {
				JSONObject getObject = new JSONObject(context);
				JSONArray patentDocsObj = getObject.optJSONObject("response").optJSONArray("docs");
				
				//check newer data
				Date temp = null;
				int indexPoint = 0;
				for (int index = 0; index < patentDocsObj.length(); index++) {
					JSONObject patentObj = patentDocsObj.optJSONObject(index);
					try {
						String compareDateStr = patentObj.optString("publicationDate");
						if (!StringUtils.isNULL(compareDateStr)) {
							Date compareDate = DateUtils.parserDateTimeUTCString(compareDateStr);
							if (compareDate != null && temp != null) {
								if (temp.before(compareDate)) {
									indexPoint = index;
								}
							}
							temp = compareDate;
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				log.info("indexPoint:"+indexPoint);
				JSONObject patentObj = patentDocsObj.optJSONObject(indexPoint);
				convertPatentInfoUS(patent, patentObj);
				parserBilbo(patent);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void parserBilbo(Patent patent) {
		String url = Constants.PATENT_WEB_SERVICE_EU+"/rest-services/published-data/publication/DOCDB/%s/biblio";
		url = String.format(url, patent.getPatent_publish_no());
		
		try {
			String token = generateToken("Basic "+Constants.PATENT_TOKEN_EU);
			if (token != null) {
				String content = (HttpRequestUtils.sendGetByToken(url, token));
				if (!StringUtils.isNULL(content)) {
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
						org.w3c.dom.Document doc = db.parse(is);
						doc.getDocumentElement().normalize();
						
						NodeList ipcrList = doc.getElementsByTagName("classification-ipcr");
						List<IPCClass> listIPC = new ArrayList<IPCClass>();
						for (int temp = 0; temp < ipcrList.getLength(); temp++) {
							org.w3c.dom.Node nNode = ipcrList.item(temp);
							if (nNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) { 
								org.w3c.dom.Element eElement = (org.w3c.dom.Element) nNode;
								IPCClass ipc = new IPCClass();
								String ipcId = eElement.getTextContent().replaceAll("\\s+","")
										.substring(0, eElement.getTextContent().replaceAll("\\s+","").length()-2);
								ipcId = ipcId.substring(0,4)+" "+ipcId.substring(4,ipcId.length());
								ipc.setIpc_class_id(ipcId);
								int year = Calendar.getInstance().get(Calendar.YEAR);
								ipc.setIpc_version(Integer.toString(year)+"01");
								listIPC.add(ipc);
							}
						}
						patent.setListIPC(listIPC);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					log.error("token must not null");
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
	
	private static void convertPatentInfoUS(Patent patent, JSONObject patentObj) {
		
			patent.setPatent_name_en(patentObj.optString("title"));
			patent.setPatent_appl_country("US");
			
			if (patentObj.has("applicationNumber")) {
				patent.setPatent_appl_no(patentObj.optString("applicationNumber"));
			}
				
			try {
				String applDateStr = patentObj.optString("applicationDate");
				if (!StringUtils.isNULL(applDateStr)) {
					Date applDate = DateUtils.parserDateTimeUTCString(applDateStr);
					patent.setPatent_appl_date(applDate);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			getPatentNoticeAndPublish(patent);
				
			getPatentInventorsApplcant(patent);
				
			JSONArray patentApplicants = patentObj.optJSONArray("applicant");
			List<Applicant> listAppl = new ArrayList<Applicant>();
			int orderAp = 1;
			if (patentApplicants != null) {
				for (int applIndex = 0; applIndex < patentApplicants.length(); applIndex++) {
					String appl = patentApplicants.optString(applIndex);
					Applicant applicant = new Applicant();
					applicant.setApplicant_name_en(appl);
					applicant.setApplicant_order(orderAp);
					applicant.setPatent(patent);
					listAppl.add(applicant);
					orderAp++;
				}
			}
				
			JSONArray patentAssignee = patentObj.optJSONArray("assignee");
			List<Assignee> listAssignee = new ArrayList<Assignee>();
			int orderAs = 1;
			if (patentAssignee != null) {
				for (int asslIndex = 0; asslIndex < patentAssignee.length(); asslIndex++) {
					String assignee = patentApplicants.optString(asslIndex);
					Assignee assign = new Assignee();
					assign.setAssignee_name_en(assignee);
					assign.setPatent(patent);
					assign.setAssignee_order(orderAs);
					listAssignee.add(assign);
					orderAs++;
				}
			}
				
			patent.setListAssignee(listAssignee);
			patent.setListApplicant(listAppl);
				
			if (!StringUtils.isNULL(patent.getPatent_no())) {
				getContext(patent);
			}
		
	}
	
	private static void getPatentInventorsApplcant(Patent patent) {
		String url = Constants.PATENT_INVENTOR_WEB_SERVICE_US;
		JSONObject obj = new JSONObject();
		if (patent.getPatent_appl_no() != null) {
			obj.put("searchText", "applId:"+patent.getPatent_appl_no().substring(2));
			obj.put("mm", "100%");
			obj.put("qf", "applId");
			log.info(obj.toString());
			try {
				JSONObject getObject = new JSONObject(HttpRequestUtils.sendPost(url, obj.toString()));
				JSONArray patentDocsObj = getObject.optJSONObject("queryResults").optJSONObject("searchResponse")
													.optJSONObject("response").optJSONArray("docs");
				for (int index = 0; index < patentDocsObj.length(); index++) {
					JSONObject patentObj = patentDocsObj.optJSONObject(index);
					JSONArray patentInventors = patentObj.optJSONArray("inventors");
					List<Inventor> listInventor = new ArrayList<Inventor>();
					for (int indexInventors = 0; indexInventors < patentInventors.length(); indexInventors++) {
						JSONObject inventorObj = patentInventors.optJSONObject(indexInventors);
						Inventor inv = new Inventor();
						if (StringUtils.isNULL(inventorObj.optString("nameLineTwo"))||
								" ".equals(inventorObj.optString("nameLineTwo"))) {
							inv.setInventor_name_en(inventorObj.optString("nameLineOne"));
						} else {
							inv.setInventor_name_en(inventorObj.optString("nameLineTwo")+
									inventorObj.optString("nameLineOne"));
						}
						inv.setCountry_id(inventorObj.optString("country").replace("(", "").replace(")", ""));
						inv.setInventor_order(Integer.parseInt(inventorObj.optString("rankNo")));
						inv.setPatent(patent);
						listInventor.add(inv);
					}
					patent.setListInventor(listInventor);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private static void getPatentNoticeAndPublish(Patent patent) {
		String url = Constants.PATENT_WEB_SERVICE_US+"?applicationNumber=%s";
		url = String.format(url, patent.getPatent_appl_no());
	
		
		try {
			JSONObject getObject = new JSONObject(HttpRequestUtils.sendGet(url));
			JSONArray patentDocsObj = getObject.optJSONObject("response").optJSONArray("docs");
			for (int index = 0; index < patentDocsObj.length(); index++) {
				JSONObject patentObj = patentDocsObj.optJSONObject(index);
				if (patentObj.has("patentNumber")) {
					patent.setPatent_no(patentObj.optString("patentNumber"));
				}
				
				if (patentObj.optString("documentId").endsWith("A1")) {
					patent.setPatent_notice_no(patentObj.optString("documentId")
							.substring(0, patentObj.optString("documentId").indexOf("A1")));
					try {
						String noticeDateStr = patentObj.optString("publicationDate");
						if (!StringUtils.isNULL(noticeDateStr)) {
							Date noticeDate = DateUtils.parserDateTimeUTCString(noticeDateStr);
							patent.setPatent_notice_date(noticeDate);
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (patentObj.optString("documentId").endsWith("A2")) {
					patent.setPatent_notice_no(patentObj.optString("documentId")
							.substring(0, patentObj.optString("documentId").indexOf("A2")));
					try {
						String noticeDateStr = patentObj.optString("publicationDate");
						if (!StringUtils.isNULL(noticeDateStr)) {
							Date noticeDate = DateUtils.parserDateTimeUTCString(noticeDateStr);
							patent.setPatent_notice_date(noticeDate);
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (patentObj.optString("documentId").endsWith("A9")) {
					patent.setPatent_notice_no(patentObj.optString("documentId")
							.substring(0, patentObj.optString("documentId").indexOf("A9")));
					try {
						String noticeDateStr = patentObj.optString("publicationDate");
						if (!StringUtils.isNULL(noticeDateStr)) {
							Date noticeDate = DateUtils.parserDateTimeUTCString(noticeDateStr);
							patent.setPatent_notice_date(noticeDate);
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if (patentObj.optString("documentId").endsWith("B1")) {
					patent.setPatent_publish_no(patentObj.optString("documentId")
							.substring(0, patentObj.optString("documentId").indexOf("B1")));
					try {
						String publishDateStr = patentObj.optString("publicationDate");
						if (!StringUtils.isNULL(publishDateStr)) {
							Date publishDate = DateUtils.parserDateTimeUTCString(publishDateStr);
							patent.setPatent_publish_date(publishDate);
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				if (patentObj.optString("documentId").endsWith("B2")) {
					patent.setPatent_publish_no(patentObj.optString("documentId")
							.substring(0, patentObj.optString("documentId").indexOf("B2")));
					try {
						String publishDateStr = patentObj.optString("publicationDate");
						if (!StringUtils.isNULL(publishDateStr)) {
							Date publishDate = DateUtils.parserDateTimeUTCString(publishDateStr);
							patent.setPatent_publish_date(publishDate);
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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
	
	private static void getContext(Patent patent) {
		String url = Constants.PATENT_CONTEXT_WEB_SERVICE_US+patent.getPatent_no();
		
		try {
			Document doc = Jsoup.parse(new URL(url), 3000);
			
			convertPatentContextHtml(doc, patent);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	private static void convertPatentContextHtml(Document doc, Patent patent) {
		PatentAbstract pa = new PatentAbstract();
		PatentClaim pc = new PatentClaim();
		PatentDescription pd = new PatentDescription();
		Elements tables = doc.select("center");
		
		for (Element table:tables) {
			Iterator<Element> ite = table.select("b").iterator();
			while(ite.hasNext())
	          {
				  String title = ite.next().text();
	              if ("abstract".equals(title.toLowerCase())) {
	            	  Element abstractElement = table.nextElementSibling();
	            	  pa.setContext_abstract(abstractElement.text());
	              }
	          }   
		}
		
		String claimStr = "";
		String descStr = "";
		Elements centers = doc.getAllElements();
		for (Element center:centers) {
			List<Node> cNodes = center.childNodes();
			boolean claim = false;
			boolean desc = false;
			for (Node cNode:cNodes) {
				String tag = cNode.nodeName().toString();
				if (!"center".equals(tag)) {
					if (claim == true) {
						String context = cNode.toString().replace("<br>", "").replace("<hr>", "");
						if(!StringUtils.isNULL(context)) {
							claimStr += context + "\n";
		            	}
		            }
					
					if (desc == true) {
						String context = cNode.toString().replace("<br>", "").replace("<hr>", "");
		            	if(!StringUtils.isNULL(context)) {
		            		descStr += context + "\n";
		            	}
		            }
				}
				if ("center".equals(tag)) {
					if (cNode.toString().toLowerCase().contains("claims")) {
						claim = true;
		            	desc = false;
					}
		            if (cNode.toString().toLowerCase().contains("description")) {
		            	claim = false;
		            	desc = true;
		            }
				}
			}
		}
		
		pc.setContext_claim(claimStr);
		pd.setContext_desc(descStr);
		
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
