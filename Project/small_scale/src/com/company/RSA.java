package com.company;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class RSA {
    public static PrivateKey privateKey;
    public static PublicKey publicKey;


    public static byte[] encryptMessage_cipher(String plainText, PrivateKey ret) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, ret);

        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes("UTF-8"))).getBytes();
    }


    public static String decryptMessage_cipher(byte[] encryptedText , PublicKey publicKey) throws Exception {

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)));
        //return new String(plainText);
    }

    public static void getRSAKeys() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(3072);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();

        Map<String, Object> keys = new HashMap<String,Object>();
        keys.put("private", privateKey);
        keys.put("public", publicKey);
    }


    public static byte[] encryptMessage_cipher_key(byte[] key, PrivateKey ret) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, ret);

        return Base64.getEncoder().encodeToString(cipher.doFinal(key)).getBytes();
    }

    public static SecretKey decryptMessage_cipher_key(byte[] key , PublicKey publicKey) throws Exception {

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] message = cipher.doFinal(Base64.getDecoder().decode(key));

        System.out.println(message);

        return new SecretKeySpec(key, 0, key.length, "AES");
        //return new String(plainText);
    }


}
