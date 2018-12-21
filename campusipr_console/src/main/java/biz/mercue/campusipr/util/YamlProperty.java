package biz.mercue.campusipr.util;

import java.util.Map;

public class YamlProperty {

	private String ip;
	private Map<String, String> jdbc;
	private Map<String, String> hibernate;
	private Map<String, String> path;
	private Map<String, String> url;
	private Map<String, String> jieba;
	private Map<String, String> duckling;
	private Map<String, String> elasticsearch;
	private Map<String, String> patent_api;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public Map<String, String> getJdbc() {
		return jdbc;
	}
	public void setJdbc(Map<String, String> jdbc) {
		this.jdbc = jdbc;
	}
	
	public Map<String, String> getHibernate() {
		return hibernate;
	}
	public void setHibernate(Map<String, String> hibernate) {
		this.hibernate = hibernate;
	}
	
	public Map<String, String> getPath() {
		return path;
	}
	public void setPath(Map<String, String> path) {
		this.path = path;
	}

	public Map<String, String> getUrl() {
		return url;
	}
	public void setUrl(Map<String, String> url) {
		this.url = url;
	}
	public Map<String, String> getJieba() {
		return jieba;
	}
	public void setJieba(Map<String, String> jieba) {
		this.jieba = jieba;
	}
	public Map<String, String> getDuckling() {
		return duckling;
	}
	public void setDuckling(Map<String, String> duckling) {
		this.duckling = duckling;
	}
	public Map<String, String> getElasticsearch() {
		return elasticsearch;
	}
	public void setElasticsearch(Map<String, String> elasticsearch) {
		this.elasticsearch = elasticsearch;
	}
	public Map<String, String> getPatent_api() {
		return patent_api;
	}
	public void setPatent_api(Map<String, String> patent_api) {
		this.patent_api = patent_api;
	}
	
}
