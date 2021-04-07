package com.company;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import java.util.concurrent.Semaphore;

public class Main {

    public static RSA rsa;


    public static void main(String[] args) {
        int PORT = 12345;
        //generate RSA Keys
        rsa = new RSA();
        try{
            rsa.getRSAKeys();
        }catch(Exception e){
            e.printStackTrace();
        }

        //start server
        ServerSocket server = null;
        try {
            server = new ServerSocket(PORT);
            server.setReuseAddress(true);
            System.out.println(server.getLocalSocketAddress());
            //server.getLocalSocketAddress();

            // The main thread is just accepting new connections
            while (true) {
                Socket client = server.accept();
                System.out.println("New client connected " + client.getInetAddress().getHostAddress());
                //begin client handling
                ClientHandler clientSock = new ClientHandler(client);
                // The background thread will handle each client separately
                new Thread(clientSock).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * handles client requests
     */
    private static class ClientHandler implements Runnable {

        private final Socket clientSocket;
        private String Test_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<billboard background=\"#7F3FBF\">\n<message>Billboard with custom background and default-coloured message</message>\n</billboard>";
        static Semaphore semaphore = new Semaphore(1);
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }


        @Override
        public void run() {
            byte[] key_en = rsa.privateKey.getEncoded();
            DataOutputStream dOut = null;
            try {
                String line;

                dOut = new DataOutputStream(clientSocket.getOutputStream());
                dOut.writeInt(key_en.length); // write length of the message
                DataInputStream dIn = new DataInputStream(clientSocket.getInputStream());
                dOut.write(key_en);

                int length = dIn.readInt();// read length of incoming message
                byte[] message = new byte[length];
                if(length>0) {
                    dIn.readFully(message, 0, message.length); // read the message
                }

                String message_s = rsa.decryptMessage_cipher(message,rsa.publicKey);
                System.out.printf("Sent from the client: %s\n", message_s);

                ///////////////////////////////////////



            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
