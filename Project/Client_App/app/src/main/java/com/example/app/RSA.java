package com.example.app;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.Cipher;

public class RSA {
    private static PrivateKey server_encryptKey;
    // Decrypt using RSA public key
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decryptMessage(String encryptedText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)));
    }

    // Encrypt using RSA private key
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encryptMessage(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, server_encryptKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
    }
    public static void update_server_encryptKey(Object key){
        server_encryptKey = (PrivateKey) key;
        Log.e("YOUR_APP_LOG_TAG", server_encryptKey.toString());
        Log.e("YOUR_APP_LOG_TAG", "test");
    }
}
