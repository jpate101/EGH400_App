package com.company;

import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws Exception {
        //get the localhost IP address, if server is running on some other IP, you need to use that
        String host = "127.0.0.1";
        int port = 12345;
        //AES_client aes = new AES_client();
        //aes.GenerateKeys();
        try (Socket socket = new Socket(host, port)) {

            ObjectInputStream inputStream;
            DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());

            DataInputStream dIn = new DataInputStream(socket.getInputStream());
            int length = dIn.readInt();// read length of incoming message
            byte[] key_en = new byte[length];
            if(length>0) {
                dIn.readFully(key_en, 0, key_en.length); // read the message
            }

            KeyFactory kf = KeyFactory.getInstance("RSA"); // or "EC" or whatever
            PrivateKey ret = kf.generatePrivate(new PKCS8EncodedKeySpec(key_en));

            byte[] test;
            test = RSA.encryptMessage_cipher("this is the message i want to see",ret);
            dOut.writeInt(test.length); // write length of the message
            dOut.write(test);

            ///////////////////////////////////
            System.out.println("-----aes test-----");

            String test_aes_message = "testing message";

            AES_client aes = new AES_client();
            aes.GenerateKeys();

            System.out.println(aes.secretKey);

            System.out.println(test_aes_message);
            String test_en = aes.encrypt(test_aes_message);
            System.out.println(test_en);
            String test_de = aes.decrypt(test_en);
            System.out.println(test_de);

            System.out.println("-----aes key send test-----");

            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(aes.secretKey);

            System.out.println("---- aes key with rsa encription and decrption  ------");
            RSA rsa = new RSA();
            try{
                rsa.getRSAKeys();
            }catch(Exception e){
                e.printStackTrace();
            }


            // get base64 encoded version of the key
            String encodedKey = Base64.getEncoder().encodeToString(aes.secretKey.getEncoded());

            //rsa encryption
            test = RSA.encryptMessage_cipher(encodedKey,rsa.privateKey);
            //rsa decryption
            String test2 = rsa.decryptMessage_cipher(test,rsa.publicKey);

            // decode the base64 encoded string
            byte[] decodedKey = Base64.getDecoder().decode(test2);
            // rebuild key using SecretKeySpec
            SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

            System.out.println(aes.secretKey);
            System.out.println(encodedKey);
            System.out.println(test);
            System.out.println(test2);
            System.out.println(decodedKey);
            System.out.println(originalKey);

            System.out.println("---- send aes key with rsa ------");

            // get base64 encoded version of the key
            encodedKey = Base64.getEncoder().encodeToString(aes.secretKey.getEncoded());

            //rsa encryption
            test = RSA.encryptMessage_cipher(encodedKey,ret);

            dOut.writeInt(test.length); // write length of the message
            dOut.write(test);
            System.out.println(aes.secretKey);













        } catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
