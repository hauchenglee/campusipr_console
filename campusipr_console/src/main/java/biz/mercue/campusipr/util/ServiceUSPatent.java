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

import biz.mercue.campusipr.model.Applicant;
import biz.mercue.campusipr.model.Assignee;
import biz.mercue.campusipr.model.Inventor;
import biz.mercue.campusipr.model.Patent;
import biz.mercue.campusipr.model.PatentAbstract;
import biz.mercue.campusipr.model.PatentClaim;
import biz.mercue.campusipr.model.PatentDescription;
import biz.mercue.campusipr.model.View;

public class ServiceUSPatent {
	
	private static  Logger log = Logger.getLogger(ServiceUSPatent.class.getName());
	
	
	public static void getPatentRightByapplNo(Patent patent) {
		String url = Constants.PATENT_WEB_SERVICE_US+"?applicationNumber=%s";
		url = String.format(url, patent.getPatent_appl_no());
		
		try {
			JSONObject getObject = new JSONObject(HttpRequestUtils.sendGet(url));
			convertPatentInfoUS(patent, getObject);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void convertPatentInfoUS(Patent patent, JSONObject obj) {
		
		JSONArray patentDocsObj = obj.optJSONObject("response").optJSONArray("docs");
		
		List<String> detectDuplicateapplNoList = new ArrayList<>();
		for (int index = 0; index < patentDocsObj.length(); index++) {
			JSONObject patentObj = patentDocsObj.optJSONObject(index);
			if (!detectDuplicateapplNoList.contains(patentObj.optString("applicationNumber"))) {
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
				
				JSONArray patentAssignee = patentObj.optJSONArray("applicant");
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
				
				log.info("0");
				if (!StringUtils.isNULL(patent.getPatent_no())) {
					log.info("1");
					getContext(patent);
				}
				
				detectDuplicateapplNoList.add(patentObj.optString("applicationNumber"));
			}
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

}
