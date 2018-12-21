package biz.mercue.campusipr.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

public class HttpRequestUtils {
	
	
	private final static String USER_AGENT = "Mozilla/5.0";
	private static  Logger log = Logger.getLogger(HttpRequestUtils.class.getName());
	
	// HTTP POST request
	public static String sendPost(String url,String postParams) throws Exception {
			log.info("sendPost");
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			con.setRequestProperty("Content-Length", Integer.toString(postParams.getBytes("UTF-8").length));
			//add request header
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setReadTimeout(60000);
			con.setConnectTimeout(60000);
			con.setUseCaches (false);
			con.setDoInput(true);
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(postParams);
			wr.flush();
			wr.close();
			int responseCode = con.getResponseCode();
			// check data
			log.info("responseCode:"+responseCode);
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream(), "UTF-8"));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			//print result
			log.info(response.toString());

			return response.toString();
	}
		
		
		
	// HTTP GET request
	public static String sendGet(String url) throws Exception {

			URL obj = new URL(url);
			if("https".equalsIgnoreCase(obj.getProtocol())){
	            SslUtils.ignoreSsl();
	        }
			
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			//add request header
			con.setRequestProperty("User-Agent", USER_AGENT);

			int responseCode = con.getResponseCode();
			// check data
			log.info("\nSending 'GET' request to URL : " + url);
			log.info("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(
					new InputStreamReader(con.getInputStream(), "UTF-8"));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			//print result
			log.info(response.toString());

			return response.toString();
	}

}
