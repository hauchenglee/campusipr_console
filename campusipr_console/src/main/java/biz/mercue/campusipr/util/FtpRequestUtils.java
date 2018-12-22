package biz.mercue.campusipr.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

public class FtpRequestUtils {
	
	
	private final static String USER_AGENT = "Mozilla/5.0";
	private static  Logger log = Logger.getLogger(FtpRequestUtils.class.getName());
	
	// FTP GET request
	public static String sendGet(String url) throws Exception {
		BufferedReader in = null;
		try {
			URL obj = new URL(url);
			URLConnection con = (URLConnection) obj.openConnection();

			in = new BufferedReader(
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
		} finally {
	        if (in != null) {
	            try {
	            	in.close();
	            } catch (IOException logOrIgnore) {
	            }
	        }
	    }
	}

}
