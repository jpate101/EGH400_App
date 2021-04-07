package com.company;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.util.Base64;

public class AES_client {
    public static SecretKey secretKey;
    public static byte[] secretKey_encoded;

    public static void GenerateKeys() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); // for example
        secretKey = keyGen.generateKey();
        secretKey_encoded = secretKey.getEncoded();
    }




}
