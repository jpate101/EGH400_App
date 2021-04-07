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
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static byte[] encryptMessage_cipher(String plainText, PrivateKey ret) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, ret);

        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes("UTF-8"))).getBytes();
    }
}
