package com.company;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.security.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class RSA {
    public static PrivateKey privateKey;
    public static PublicKey publicKey;


    public static byte[] encryptMessage_cipher(String plainText, PrivateKey ret) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, ret);

        return Base64.getEncoder().encode(cipher.doFinal(plainText.getBytes("UTF-8")));
    }



    public static String decryptMessage_cipher(byte[] encryptedText , PublicKey publicKey) throws Exception {

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] de_test = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(de_test);
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
