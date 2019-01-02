package biz.mercue.campusipr.util;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import biz.mercue.campusipr.model.Assignee;
import biz.mercue.campusipr.model.Inventor;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentContext;
import biz.mercue.campusipr.model.View;

public class ServiceUSPatent {
	
	private static  Logger log = Logger.getLogger(ServiceUSPatent.class.getName());
	
	
	public static List<Patent> getPatentRightByapplNo(String applNo) {
		String url = Constants.PATENT_WEB_SERVICE_US+"?applicationNumber=%s";
		url = String.format(url, applNo);
		
		List<Patent> patentList = new ArrayList<Patent>();
		try {
			JSONObject getObject = new JSONObject(HttpRequestUtils.sendGet(url));
			patentList = convertPatentInfoUS(getObject);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return patentList;
	}
	
	public static List<Patent> getPatentRightByAssigneeName(String assigneeName, int row) {
		String url = Constants.PATENT_WEB_SERVICE_US+"?assignee=%s&rows=%s";
		try {
			url = String.format(url, URLEncoder.encode(assigneeName, "UTF-8"), row);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		List<Patent> patentList = new ArrayList<Patent>();
		try {
			JSONObject getObject = new JSONObject(HttpRequestUtils.sendGet(url));
			patentList = convertPatentInfoUS(getObject);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return patentList;
	}
	
	
	private static List<Patent> convertPatentInfoUS(JSONObject obj) {
		
		List<Patent> patentList = new ArrayList<Patent>();
		
		JSONArray patentDocsObj = obj.optJSONObject("response").optJSONArray("docs");
		
		List<String> detectDuplicateapplNoList = new ArrayList<>();
		for (int index = 0; index < patentDocsObj.length(); index++) {
			JSONObject patentObj = patentDocsObj.optJSONObject(index);
			if (!detectDuplicateapplNoList.contains(patentObj.optString("applicationNumber"))) {
				Patent patent = new Patent();
				patent.setPatent_name_en(patentObj.optString("title"));
				patent.setPatent_appl_country("US");
				
				try {
					String applDateStr = patentObj.optString("applicationDate");
					if (StringUtils.isNULL(applDateStr) == false) {
						Date applDate = DateUtils.parserDateTimeUTCString(applDateStr);
						patent.setPatent_appl_date(applDate);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				patent.setPatent_appl_no(patentObj.optString("applicationNumber"));
				
				try {
					String publishDateStr = patentObj.optString("publicationDate");
					if (StringUtils.isNULL(publishDateStr) == false) {
						Date publishDate = DateUtils.parserDateTimeUTCString(publishDateStr);
						patent.setPatent_publish_date(publishDate);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				getPatentNoticeAndPublish(patent);
				
				getPatentInventors(patent);
				
				List<Assignee> listAssignee = new ArrayList<Assignee>();
				JSONArray assignee = patentObj.optJSONArray("assignee");
				int orderA = 1;
				if (assignee != null) {
					for (int assigneeIndex = 0; assigneeIndex < assignee.length(); assigneeIndex++) {
						String assigneeName = assignee.optString(assigneeIndex);
						Assignee assign = new Assignee();
						assign.setAssignee_name_en(assigneeName);
						assign.setAssignee_order(orderA);
						assign.setPatent(patent);
						listAssignee.add(assign);
						orderA++;
					}
				}
				
				patent.setListAssignee(listAssignee);
				
				String patentNo = patentObj.optString("patentNumber");
				if (StringUtils.isNULL(patentNo)==false) {
					getContext(patent);
				}
				
				patentList.add(patent);
				detectDuplicateapplNoList.add(patentObj.optString("applicationNumber"));
			}
		}
		
		return patentList;
	}
	
	private static void getPatentInventors(Patent patent) {
		String url = Constants.PATENT_INVENTOR_WEB_SERVICE_US;
		JSONObject obj = new JSONObject();
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
					if (StringUtils.isNULL(inventorObj.optString("nameLineTwo"))) {
						inv.setInventor_name_en(inventorObj.optString("nameLineOne"));
					} else {
						inv.setInventor_name_en(inventorObj.optString("nameLineOne") + " " +
								inventorObj.optString("nameLineTwo"));
					}
					inv.setCountry_id(inventorObj.optString("country"));
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
						String noticeDateStr = patentObj.optString("applicationDate");
						if (StringUtils.isNULL(noticeDateStr) == false) {
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
						String noticeDateStr = patentObj.optString("applicationDate");
						if (StringUtils.isNULL(noticeDateStr) == false) {
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
						String publishDateStr = patentObj.optString("applicationDate");
						if (StringUtils.isNULL(publishDateStr) == false) {
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
						String publishDateStr = patentObj.optString("applicationDate");
						if (StringUtils.isNULL(publishDateStr) == false) {
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
		PatentContext patentContext = new PatentContext();
		Elements tables = doc.select("center");
		
		for (Element table:tables) {
			Iterator<Element> ite = table.select("b").iterator();
			while(ite.hasNext())
	          {
				  String title = ite.next().text();
	              if ("abstract".equals(title.toLowerCase())) {
	            	  Element abstractElement = table.nextElementSibling();
	            	  patentContext.setContext_abstract(abstractElement.text());
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
		
		
		patentContext.setContext_claim(claimStr);
		patentContext.setContext_desc(descStr);
		patentContext.setPatent(patent);
		patent.setPatentContext(patentContext);
	}

}
