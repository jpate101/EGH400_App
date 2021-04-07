package com.company;

import java.lang.*;

import java.nio.ByteBuffer;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;



public class RSA_test {

    public static PrivateKey privateKey;
    public static PublicKey publicKey;

    public static void main(String[] args) throws Exception {
        String plainText = "LOGIN_request";

        // Generate public and private keys using RSA
        Map<String, Object> keys = getRSAKeys();

        privateKey = (PrivateKey) keys.get("private");
        publicKey = (PublicKey) keys.get("public");


        System.out.println("keys");
        System.out.println(privateKey);
        System.out.println(publicKey);
        System.out.println("-------");

        byte[] str1 = privateKey.getEncoded();

        System.out.println(str1);

        KeyFactory kf = KeyFactory.getInstance("RSA"); // or "EC" or whatever
        PrivateKey ret = kf.generatePrivate(new PKCS8EncodedKeySpec(str1));

        System.out.println(ret);


        System.out.println("-----------------------");


        //String encryptedText = encryptMessage(plainText, privateKey);
        //String descryptedText = decryptMessage(encryptedText, publicKey);
        byte[] encryptedText = encryptMessage_cipher(plainText,ret);
        String Text = decryptMessage_cipher(encryptedText);


        System.out.println("input:" + plainText);
        System.out.println("encrypted:" + encryptedText);
        System.out.println("decrypted:" + Text);

    }

    // Get RSA keys. Uses key size of 2048.
    private static Map<String,Object> getRSAKeys() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(3072);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        Map<String, Object> keys = new HashMap<String,Object>();
        keys.put("private", privateKey);
        keys.put("public", publicKey);
        return keys;
    }

    // Decrypt using RSA public key
    private static String decryptMessage(String encryptedText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)));
    }

    // Encrypt using RSA private key
    private static String encryptMessage(String plainText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
    }

    public static byte[] encryptMessage_cipher(String plainText, PrivateKey ret) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, ret);

        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes("UTF-8"))).getBytes();
    }

    public static String decryptMessage_cipher(byte[] encryptedText) throws Exception {

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)));
        //return new String(plainText);
    }
}
