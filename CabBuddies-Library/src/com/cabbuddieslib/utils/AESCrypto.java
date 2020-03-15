package com.cabbuddieslib.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESCrypto {
	final static Base64.Encoder encorder = Base64.getEncoder();
    final static Base64.Decoder decorder = Base64.getDecoder();
    
    private static final String MOST_SECURE_KEY = "0D434472240F828DFD9439D8AFC16EF894A57762DDCF10E0E253782833C7EE7A";
    private static final String LEAST_SECURE_KEY = "F66BBF8179937F5070978432B3067CD699E8B495FED4EE35582750C678816850";
    
    static private Cipher cipher(int opmode, String secretKey) throws Exception{
        if(secretKey.length() != 32) throw new RuntimeException("SecretKey length is not 32 chars");
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec sk = new SecretKeySpec(secretKey.getBytes(), "AES");
        IvParameterSpec iv = new IvParameterSpec(secretKey.substring(0, 16).getBytes()); //0~16은 서버와 합의!
        c.init(opmode, sk, iv);
        return c;
    }
    static private String encrypt(String str, String secretKey){
        try{
            byte[] encrypted = cipher(Cipher.ENCRYPT_MODE, secretKey).doFinal(str.getBytes("UTF-8"));
            return new String(encorder.encode(encrypted));
        }catch(Exception e){
            return null;
        }
    }
    static private String decrypt(String str, String secretKey){
        try{
            byte[] byteStr = decorder.decode(str.getBytes());
            return new String(cipher(Cipher.DECRYPT_MODE, secretKey).doFinal(byteStr),"UTF-8");
        }catch(Exception e){
            return null;
        }
    }

    public static String encrypt(String text,boolean mostSecure) {
    	return encrypt(text, mostSecure?MOST_SECURE_KEY:LEAST_SECURE_KEY);
    }
    public static String decrypt(String text,boolean mostSecure) {
    	return decrypt(text, mostSecure?MOST_SECURE_KEY:LEAST_SECURE_KEY);
    }
}
