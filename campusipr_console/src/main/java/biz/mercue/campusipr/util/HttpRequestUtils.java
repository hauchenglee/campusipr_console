package biz.mercue.campusipr.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.Logger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpRequestUtils {
	
	
	private final static String USER_AGENT = "Mozilla/5.0";
	private static  Logger log = Logger.getLogger(HttpRequestUtils.class.getName());
	
	// HTTP POST request
	public static String sendPost(String url,String postParams) throws Exception {
			log.info("sendPost");
			OkHttpClient client = new OkHttpClient();
			
			MediaType mediaType = MediaType.parse("application/json");
			
			RequestBody body = RequestBody.create(mediaType,postParams);
			
			Request request = new Request.Builder()
					  .url(url)
					  .post(body)
					  .addHeader("content-type", "application/json")
					  .build();
			
			String responseStr = null;
			
			Call call = client.newCall(request);
			try {
			    Response response = call.execute();
			    responseStr = response.body().string();
			} catch (IOException e) {
			    e.printStackTrace();
			}
			
			//print result
			log.info(responseStr);

			return responseStr;
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
			
			BufferedReader in = null;
			if (responseCode == 200) {
				if ("gzip".equals(con.getContentEncoding())) {
					in = new BufferedReader(new InputStreamReader(new GZIPInputStream(con.getInputStream())));
			    }else {
			    	  in = new BufferedReader(
								new InputStreamReader(con.getInputStream(), "UTF-8"));
			    }
			}
			
			String inputLine;
			StringBuffer response = new StringBuffer();
			if (in != null) {
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
			}
			//print result
			log.info(response.toString());

			return response.toString();
	}
	
	// HTTP POST request
	public static String sendPostByToken(String url,String postParams,String token) throws Exception {
				log.info("sendPost");
				OkHttpClient client = new OkHttpClient();
				
				MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
				
				RequestBody body = RequestBody.create(mediaType,postParams);
				
				Request request = new Request.Builder()
						  .url(url)
						  .post(body)
						  .addHeader("authorization", token)
						  .addHeader("content-type", "application/x-www-form-urlencoded")
						  .build();
				
				String responseStr = null;
				
				Call call = client.newCall(request);
				try {
				    Response response = call.execute();
				    responseStr = response.body().string();
				} catch (IOException e) {
				    e.printStackTrace();
				}
				
				//print result
				log.info(responseStr);

				return responseStr;
	}
	
	
	// HTTP GET token request
	public static String sendGetByToken(String url,String token) throws Exception {
		URL obj = new URL(url);
		if("https".equalsIgnoreCase(obj.getProtocol())){
            SslUtils.ignoreSsl();
        }
		
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Authorization", token);

		int responseCode = con.getResponseCode();
		// check data
		log.info("\nSending 'GET' request to URL : " + url);
		log.info("Response Code : " + responseCode);
		
		BufferedReader in = null;
		if (responseCode == 200) {
			if ("gzip".equals(con.getContentEncoding())) {
				in = new BufferedReader(new InputStreamReader(new GZIPInputStream(con.getInputStream())));
		    }else {
		    	in = new BufferedReader(
							new InputStreamReader(con.getInputStream(), "UTF-8"));
		    }
		}
		
		String inputLine;
		StringBuffer response = new StringBuffer();
		if (in != null) {
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		}
		//print result
		log.info(response.toString());

		return response.toString();
			
	}

}
