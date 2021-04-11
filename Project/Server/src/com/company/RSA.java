package com.company;

import javax.crypto.Cipher;
import java.security.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class RSA {
    public static PrivateKey privateKey;
    public static PublicKey publicKey;
    /*
    public static byte[] encryptMessage_cipher(String plainText, PrivateKey ret) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, ret);

        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes("UTF-8"))).getBytes();
    }

     */

    public static String decryptMessage_cipher(byte[] encryptedText , PublicKey publicKey1) throws Exception {
        System.out.println(publicKey);

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
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
}
