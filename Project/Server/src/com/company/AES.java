package com.company;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class AES {
    public static SecretKey secretKey;
    public static byte[] secretKey_encoded;

    public static void set_server_keys(String aes_key) throws UnsupportedEncodingException {
        secretKey_encoded =  aes_key.getBytes();
        secretKey = new SecretKeySpec(secretKey_encoded, 0, secretKey_encoded.length, "AES");
    }

    public static void set_server_keys_2(Object aes_key) throws UnsupportedEncodingException {
        secretKey = (SecretKey) aes_key;
    }

    public static void GenerateKeys() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES/ECB/PKCS5Padding");
        keyGen.init(256); // for example
        secretKey = keyGen.generateKey();
        secretKey_encoded = secretKey.getEncoded();
    }

    public static String encrypt(String strToEncrypt)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public static String decrypt(String strToDecrypt)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        }
        catch (Exception e)
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
}
