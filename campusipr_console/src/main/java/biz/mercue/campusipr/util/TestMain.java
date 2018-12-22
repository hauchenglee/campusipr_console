package biz.mercue.campusipr.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;




public class TestMain {

//	public static void main(String[] args) throws Exception {
//		
	public static void testmain() {
////
//		File fXmlFile = new File("/Users/leo/Desktop/Project/Web/JavaTest/src/erp_point.xml");
//        
//		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//		Document doc = dBuilder.parse(fXmlFile);
//		doc.getDocumentElement().normalize();
//		
//		String soapMessage = DocToString(doc);
//		
////		TIPTOPServiceGateWayBindingStub stub = new TIPTOPServiceGateWayBindingStub();
////		CreateAppCouponRequest_CreateAppCouponRequest request = new CreateAppCouponRequest_CreateAppCouponRequest();
////		request.setRequest(soapMessage);
////		stub.createAppCoupon(request);
//		
//        String response = soapRequest(soapMessage);
//        System.out.println(response);
    }
	
	public static void main(String[] args) throws Exception {
		
		
	int i =0 ;
	while( i < 10 ){
		String id =  KeyGeneratorUtils.generateRandomString();
		//String hasPwd = generatePasswordHash("abc123456");
		System.out.println("id :"+id);
		//System.out.println("hasPwd :"+hasPwd);
		i++;
	}
	}
	
	public boolean validatePassword(String password,String storedPassword)throws NoSuchAlgorithmException, InvalidKeySpecException{
	    //System.out.println("validatePassword");   
	    String[] parts = storedPassword.split(":");
        int iterations = 1000;
        byte[] salt = fromHex(parts[0]);
        byte[] hash = fromHex(parts[1]);
         
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();
         
        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++)
        {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
	}
	private static byte[] fromHex(String hex) throws NoSuchAlgorithmException{
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i<bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
	}
	
	//output salt:hashPassword
	public static String generatePasswordHash(String password)throws NoSuchAlgorithmException, InvalidKeySpecException{
		int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = generateSalt().getBytes();
         
        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        
        return toHex(salt) + ":" + toHex(hash);
	}
	
	private static String toHex(byte[] array) throws NoSuchAlgorithmException{
		BigInteger bi = new BigInteger(1, array);
		System.out.println( "hex :"+bi.toString());
		String hex = bi.toString(16);
		int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0){
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }
        else{
            return hex;
        }
    }
	
	private static String generateSalt() throws NoSuchAlgorithmException{
		 SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
	     byte[] salt = new byte[16];
	     sr.nextBytes(salt);
	     return salt.toString();
	}
	
	public static void testERP() throws Exception {
	
//		String redeemItems = "<coupon_list><coupon><coupon_name>贈品券300元</coupon_name><amount>5</amount><valid_startdate>20170301</valid_startdate><valid_enddate>20170501</valid_enddate></coupon><coupon><coupon_name>贈品券500元</coupon_name><amount>10</amount><valid_startdate>20170301</valid_startdate><valid_enddate>20170501</valid_enddate></coupon></coupon_list>";
//		StringBuilder xmlStringBuilder = new StringBuilder();
//		DocumentBuilderFactory factory =DocumentBuilderFactory.newInstance();
//		DocumentBuilder builder = factory.newDocumentBuilder();
//		xmlStringBuilder.append(redeemItems);
//		ByteArrayInputStream input =  new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));
//		Document doc = builder.parse(input);
//	
//		NodeList couponList = doc.getElementsByTagName("coupon");
//		
//		 for (int index = 0; index < couponList.getLength(); index++) {
//			 Node node = couponList.item(index);
//			 Element eElement = (Element)  couponList.item(index);
//			  NodeList childList = node.getChildNodes();
//			  for (int x = 0; x < childList.getLength(); x++) {
//				  Element ch = (Element)  childList.item(x);
//				  System.out.println("1:"+ ch.getNodeName() );
//				  System.out.println("2:"+ ch.getTextContent() );
//			  }
//			 
//		 }
		
		
		
		
		
		
		
		System.out.println(KeyGeneratorUtils.generateRandomString());
		
		// HTTP POST request
		System.out.println("start");
			String url = "http://192.168.2.117/web/ws/r/aws_ttsrv2_toptest";
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		    String  postParams= "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tip=\"http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">"
		    					+"<soap:Header/>"
		    					+"<soap:Body>"
		    				    +"<tip:CreateAppCouponRequest>"
		    				    +"<tip:request>"
		    				    +" </tip:request></tip:CreateAppCouponRequest></soap:Body></soap:Envelope>";
		    String xmlStr =parserERPPointRequestXML();

			System.out.println(xmlStr);
			con.setDoInput(true);

			System.out.println(Integer.toString(xmlStr.getBytes().length));
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type",  "text/xml; charset=UTF-8");
			con.setRequestProperty("soapaction", "\"\"");
			con.setRequestProperty("Content-Length", Integer.toString(xmlStr.getBytes().length));

			//con.setRequestProperty("User-Agent", "Apache-HttpClient/4.1.1(java 1.5)");
			con.setUseCaches (false);
			con.setDoOutput(true);
			for (String header : con.getRequestProperties().keySet()) {
				   if (header != null) {
				     for (String value : con.getRequestProperties().get(header)) {
				        System.out.println(header + ":" + value);
				      }
				   }
				}
		
			OutputStream out = con.getOutputStream();
			out.write(xmlStr.getBytes());
			out.close();
			con.connect();
			int responseCode = con.getResponseCode();
			System.out.println("responseCode:"+responseCode);
			System.out.println("responseCodeMessage:"+con.getResponseMessage());
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			//print result
			String strResponse = response.toString();
			parserERPCreateCoupon(strResponse);
			
		
	}
	
	private static String parserERPPointRequestXML(){
		try{
			String strXML  = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tip=\"http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">"
					+"<soap:Header/>"
					+"<soap:Body>"
				    +"<tip:CreateAppCouponRequest>"
				    +"<tip:request>"
				    	+"&lt;Request>"
				    	+"&lt;Access>    &lt;!-- 呼叫存取資訊 -->"
				    	+"&lt;Authentication user=\"tiptop\" password=\"tiptop\" />"
				    	+"&lt;!-- user: 帳號, password: 密碼 ; 目前暫時不控管權限 -->"
				    	+"&lt;Connection application=\"NaNa\" source=\"192.168.1.2\" />"
				    	+"&lt;!-- application: 呼叫端系統代號 ; source: 呼叫端來源 IP or Host -->"
				    	+"&lt;Organization name=\"BY001\" />"
				    	+"&lt;!-- 此次呼叫指定存取的 ERP 營運中心代碼 -->"
				    	+"&lt;Locale language=\"zh_tw\" />"
				    	+"&lt;!-- 訊息語系; 目前定義 zh_tw(繁體) / zh_cn(簡體) / en_us(英文) -->"
				    	+"&lt;/Access>"
				    	+"&lt;RequestContent>"
				    	+"&lt;Parameter> &lt;!-- 參數資料節點 -->"
				    	+"&lt;Document>"
				    		+"&lt;RecordSet  id=\"1\" >"
				    		+"&lt;Master name=\"lrl_file\">"
				    		+"&lt;Record>"
				    		
				    		+"&lt;Field name=\"lrl00\" value=\"BY001\" />"
				    		+"&lt;!—兌換營運中心  -->"
				    		
				    		+"&lt;Field name=\"lrl13\" value=\"2017/03/14\" />"
				    		+"&lt;!—兌換日期  -->"
				    		+"&lt;!-- 日期格式需為 YYYY/MM/DD -->"
				    		
				    		+"&lt;Field name=\"lrl04\" value=\"APPMJUU6BR1\" />"
				    		+"&lt;!—會員編號  -->"
				    		
				    		+"&lt;Field name=\"lrl05\" value=\"KB201701001\" />"
				    		+"&lt;!—方案編號  -->"
				    		
				    		+"&lt;Field name=\"lrg08\" value=\"1\" />"
				    		+"&lt;!—方案項次  -->"
				    		
				    		+"&lt;Field name=\"lrg02\" value=\"UK100\" />"
				    		+"&lt;!—贈品編號  -->"
				    		
				    		+"&lt;Field name=\"lrg04\" value=\"1\" />"
				    		+"&lt;!—兌換份數 -->"
				    		
				    		+"&lt;Field name=\"lrg05\" value=\"1\" />"
				    		+"&lt;!—總扣抵積分  -->"
				    		
				    		+"&lt;/Record>"
				    		+"&lt;/Master>"
				    		+"&lt;/RecordSet>"
				    	+"&lt;/Document>"
				    	+"&lt;/Parameter>"
				    	+"&lt;/RequestContent>"  
				    	+"&lt;/Request>"
				    +" </tip:request>"
				    + "</tip:CreateAppCouponRequest></soap:Body></soap:Envelope>";
			
			
			return strXML;
		}catch (Exception e){
		
		}
		return null;
	}
	
	private static void parserERPCreateCoupon(String response){
			response = response.replaceAll("&lt;", "<");
			response = response.replaceAll("&gt;", ">");
			response = response.replaceAll("&quot", "\"");
			System.out.println("reverse string:"+response);
		try{
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(response));
			Document doc = db.parse(is);
			
			doc.getDocumentElement().normalize();
			 
			 System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			 
			 NodeList nList = doc.getElementsByTagName("Status");
			 
			 System.out.println("----------------------------");
			 
			 for (int temp = 0; temp < nList.getLength(); temp++) {
			 
			  Node nNode = nList.item(temp);
			  System.out.println("\nCurrent Element :" + nNode.getNodeName());
			  if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				 
				   Element eElement = (Element) nNode;
				   System.out.println("code : " + eElement.getAttribute("code"));
				   System.out.println("description : " + eElement.getAttribute("description"));
			  }
			  
			  NodeList rList = doc.getElementsByTagName("Field");
			  
			  for (int x = 0; x < rList.getLength(); x++) {
				  Node rNode = nList.item(x);
				  Element rElement = (Element) rNode;
				  System.out.println("name : " + rElement.getAttribute("name"));
				  System.out.println("value : " + rElement.getAttribute("value"));
			  }
			  
			  
			  
			 }
		}catch(Exception e){
			System.out.println("Exception:"+e.getMessage());
		}
		
	}
	
	
	private static String DocToString(Document doc){
		try{
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
			System.out.println("output:"+output);
			return output;
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
		return null;
	}
 

    public static String soapRequest(String soapMessage) {
        
        StringBuilder response = new StringBuilder();
        String soapAction = "";
        HttpURLConnection conn = null;
        try {
            conn = getConnection();
   
            conn.setRequestProperty("Content-Length", String.valueOf(soapMessage.length()));
            conn.setRequestProperty("SOAPAction", soapAction);
             

            OutputStream output = conn.getOutputStream();
            if (null != soapMessage) {
                byte[] b = soapMessage.getBytes("utf-8");

                output.write(b, 0, b.length);
            }
            output.flush();
            output.close();
 
       
            InputStream input = conn.getInputStream();
             
            getResponseMessage(input, response);
 
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return response.toString();
    }
 

    public static void getResponseMessage(InputStream input,
            StringBuilder response) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(input,"UTF-8"));
        int c = -1;

        while (-1 != (c = br.read())) {
            response.append((char) c);
        }
    }

    public static HttpURLConnection getConnection() throws Exception {

        URL url = new URL("http://192.168.2.107/web/ws/r/aws_ttsrv2_toptest?WSDL");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setDefaultUseCaches(false);

        conn.setRequestProperty("Content-Type","text/xml; charset=utf-8");
     
        return conn;
    }
 
	

		public static boolean IdentityRCNumberValidator(String identity){
			if (identity == null || "".equals(identity)) {
				return false;
			}
				
			final char[] pidCharArray = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

			// 原身分證英文字應轉換為10~33，這裡直接作個位數*9+10
			final int[] pidIDInt = { 1, 10, 19, 28, 37, 46, 55, 64, 39, 73, 82, 2, 11, 20, 48, 29, 38, 47, 56, 65, 74, 83, 21, 3, 12, 30 };

			// 原居留證第一碼英文字應轉換為10~33，十位數*1，個位數*9，這裡直接作[(十位數*1) mod 10] + [(個位數*9) mod 10]
			final int[] pidResidentFirstInt = { 1, 10, 9, 8, 7, 6, 5, 4, 9, 3, 2, 2, 11, 10, 8, 9, 8, 7, 6, 5, 4, 3, 11, 3, 12, 10 };
			
			// 原居留證第二碼英文字應轉換為10~33，並僅取個位數*6，這裡直接取[(個位數*6) mod 10]
			final int[] pidResidentSecondInt = {0, 8, 6, 4, 2, 0, 8, 6, 2, 4, 2, 0, 8, 6, 0, 4, 2, 0, 8, 6, 4, 2, 6, 0, 8, 4};
				
			identity = identity.toUpperCase();// 轉換大寫
			final char[] strArr = identity.toCharArray();// 字串轉成char陣列
			int verifyNum = 0;

			/* 檢查身分證字號 */
			if (identity.matches("[A-Z]{1}[1-2]{1}[0-9]{8}")) {
				// 第一碼
				verifyNum = verifyNum + pidIDInt[Arrays.binarySearch(pidCharArray, strArr[0])];
				// 第二~九碼
				for (int i = 1, j = 8; i < 9; i++, j--) {
					verifyNum += Character.digit(strArr[i], 10) * j;
				}
				// 檢查碼
				verifyNum = (10 - (verifyNum % 10)) % 10;
				
				return verifyNum == Character.digit(strArr[9], 10);
			}

				/* 檢查統一證(居留證)編號 */
				verifyNum = 0;
			if (identity.matches("[A-Z]{1}[A-D]{1}[0-9]{8}")) {
				// 第一碼
				verifyNum += pidResidentFirstInt[Arrays.binarySearch(pidCharArray, strArr[0])];
				// 第二碼
				verifyNum += pidResidentSecondInt[Arrays.binarySearch(pidCharArray, strArr[1])];
				// 第三~八碼
				for (int i = 2, j = 7; i < 9; i++, j--) {
					verifyNum += Character.digit(strArr[i], 10) * j;
				}
				// 檢查碼
				verifyNum = (10 - (verifyNum % 10)) % 10;
				
				return verifyNum == Character.digit(strArr[9], 10);
			}
			
			return false;
			
		}
	

}
