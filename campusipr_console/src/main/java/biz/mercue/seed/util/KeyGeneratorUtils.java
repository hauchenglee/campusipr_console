package biz.mercue.seed.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import org.apache.commons.codec.binary.Hex;

public class KeyGeneratorUtils {
	
	/**
	 * Creates a 128-bit random string.
	 */
	public static String generateRandomString() {
		return generateRandomString(16);
	}  
	
	/**
	 * Creates a len-byte random string. String length = len * 2
	 */
	public static String generateRandomString(int len) {
		String randomString  = null;
		try {
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		byte[] randomBytes = random.generateSeed( len );
		randomString = new String( Hex.encodeHex( randomBytes ) );
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
		} 
		return randomString;
	} 
	
//	public static void main(String[] args) throws Exception {
//		System.out.print("response: "+generateRandomString());
//	}

}
