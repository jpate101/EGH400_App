package com.company;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class RSA {
    public static PrivateKey encryptKey;
    private static PublicKey decryptKey;

    // Get RSA keys. Uses key size of 3072.
    public static void getRSAKeys() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(3072);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        encryptKey = keyPair.getPrivate();
        decryptKey = keyPair.getPublic();

        System.out.println(encryptKey);
    }

    // Decrypt using RSA public key
    public static String decryptMessage(String encryptedText) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, decryptKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)));
    }

    // Encrypt using RSA private key
    public static String encryptMessage(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, encryptKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
    }
}
