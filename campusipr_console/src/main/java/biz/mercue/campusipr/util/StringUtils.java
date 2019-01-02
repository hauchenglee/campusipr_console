package biz.mercue.campusipr.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class StringUtils {
	
	public static String getFileName(String fileItemName){
		String fname = "";
		StringTokenizer st = null;
		
		if(fileItemName.indexOf("\\")!=-1) {		
			st = new StringTokenizer(fileItemName, "\\");
		}
		if(fileItemName.indexOf("/")!=-1) {		
			st = new StringTokenizer(fileItemName, "/");
		}
		if(st != null) {
			while(st.hasMoreTokens()) {
				fname = st.nextToken();
			}
		}else{
			fname = fileItemName;
		}
		return fname;
	}
	
	public static boolean  isNULL(String str){
		if("".equals(str)|| str == null){
			return true;
		}else{
			return false;
		}
	}
	
	public static String checkNULL(String str){
		if("".equals(str)|| str == null){
			return null;
		}else{
			return str;
		}
	}
	public static String checkNULL(String str,String strDefault){
		if("".equals(str)|| str == null){
			return strDefault;
		}else{
			return str;
		}
	}
//	public static boolean validatePassword(String password,String storedPassword){
//		return false;
//	}
//	
//	public static String  generatePasswordHash(String password) {
//		return "";
//	}
	
	
	
	
	public static boolean validatePBKDF2Password(String password,String storedPassword)throws NoSuchAlgorithmException, InvalidKeySpecException{
		Logger.getLogger(StringUtils.class.getName()).info("validatePassword");
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
        Logger.getLogger(StringUtils.class.getName()).info("validatePassword end");
        return diff == 0;
        
	}
	
	//output salt:hashPassword
	public static String generatePBKDF2PasswordHash(String password)throws NoSuchAlgorithmException, InvalidKeySpecException{
		int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = generateSalt().getBytes();
         
        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        
        return toHex(salt) + ":" + toHex(hash);
	}
	

	
	
	
	
	private static String generateSalt() throws NoSuchAlgorithmException{
		 SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
	     byte[] salt = new byte[16];
	     sr.nextBytes(salt);
	     return salt.toString();
	}
	
	private static String toHex(byte[] array) throws NoSuchAlgorithmException{
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0) {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }
	
	private static byte[] fromHex(String hex) throws NoSuchAlgorithmException{
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i<bytes.length ;i++) {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
	}
	
	
	
	public static String getJSONString(JSONObject receiveJSON, String string) {
		try{
			return receiveJSON.getString(string);
		} catch(JSONException e){
			Logger.getLogger(StringUtils.class.getName()).info("getJSONString: " + e.getMessage());
			return null;
		}
	}
	
	public static String covertInputStreamToString(InputStream input){

		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(input, "UTF-8"));

			StringBuilder builder = new StringBuilder();
			for(String line = null ; (line = in.readLine())!=null;){
				builder.append(line);
			}
			return builder.toString();
		} catch (UnsupportedEncodingException e) {
			Logger.getLogger(StringUtils.class.getName()).error("UnsupportedEncodingException :"+e.getMessage());
		} catch (IOException e) {
			Logger.getLogger(StringUtils.class.getName()).error("IOException :"+e.getMessage());
		}
		return null;
	}
	
	



}
