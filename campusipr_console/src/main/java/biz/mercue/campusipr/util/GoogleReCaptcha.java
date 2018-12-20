package biz.mercue.campusipr.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.json.JSONObject;

public class GoogleReCaptcha {

	private static Logger log = Logger.getLogger(GoogleReCaptcha.class.getName());
	
	public static boolean getReCaptchaResult(String payloadStr) {
		
		HttpURLConnection con = null;
		BufferedReader reader = null;
		boolean result = false;
		try {
			
			JSONObject data = new JSONObject();
//			data.put("secret", Constants.RECAPTCHA_SECRET_TOKEN);
//			data.put("response", payloadStr);
			
			URL url = new URL("https://www.google.com/recaptcha/api/siteverify?secret=" + 
			Constants.RECAPTCHA_SECRET_TOKEN + "&response=" + payloadStr);
			con = (HttpURLConnection) url.openConnection();
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			con.setRequestProperty("Accept", "application/json; charset=UTF-8");
			con.setRequestMethod("POST");
			
			OutputStream os = con.getOutputStream();
			log.info(data.toString());
			os.write(data.toString().getBytes("UTF-8"));
			os.close();
			
			int responseCode = con.getResponseCode();
			Object line = null;
			StringBuilder builder = new StringBuilder();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				JSONObject json = new JSONObject(builder.toString());
				log.info("get response:" + json);
				if (json.optBoolean("success")) {
					result = true;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			try {
				if (con != null) {
					con.disconnect();
					con = null;
				}
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				log.info("close IO error: " + e);
			}
		}
		return result;
	}
}
