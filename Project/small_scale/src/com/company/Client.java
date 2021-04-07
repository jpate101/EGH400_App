package com.company;

import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws Exception {
        //get the localhost IP address, if server is running on some other IP, you need to use that
        String host = "127.0.0.1";
        int port = 12345;
        AES_client aes = new AES_client();
        aes.GenerateKeys();
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

            AES_client.GenerateKeys();


        } catch (IOException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
