package biz.mercue.campusipr.util;


import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;






public class TestMain {

	
	public static void testmain() {

    }
	
	public static void main(String[] args) throws Exception {

		int i = 0;
		while (i < 10) {
			String id = KeyGeneratorUtils.generateRandomString();
			System.out.println("id :" + id);
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
	
	
	
	
	
	
	

 


 
	

}
