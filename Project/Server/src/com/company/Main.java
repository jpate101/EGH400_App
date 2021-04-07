package com.company;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.*;
import java.util.Base64;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Semaphore;
import java.util.Base64.Encoder;

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
        //database connection
        try{
            //create connection
            server_db_conection main_con = new server_db_conection("jdbc:mariadb://localhost:1433","egh400_test","root","jpate101");
        }catch(Exception e){
            System.out.println("error: unable to connect to database");
            //e.printStackTrace();
        }

        //
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
            PrintWriter out = null;
            BufferedReader in = null;
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String line;
                //key
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());

                System.out.println(rsa.encryptKey);
                System.out.println((Object)rsa.encryptKey);



                objectOutputStream.writeObject((Object) rsa.encryptKey);
                objectOutputStream.flush();
                line = in.readLine();
                line = rsa.decryptMessage(line);
                System.out.printf("Sent from the client: %s\n", line);
                //

                while ((line = in.readLine()) != null) {
                    System.out.printf("Sent from the client: %s\n", line);
                    //out.println(line);
                    if(line.equals("LOGIN_request")){
                        System.out.printf("login request rev");
                        String LOGIN_request_user = in.readLine();
                        String LOGIN_request_pass = in.readLine();
                        if(LOGIN_request_user.equals("Admin") && LOGIN_request_pass.equals("1234")){
                            System.out.println("T");
                            out.println("T");
                        }else{
                            System.out.println("F");
                            out.println("F");
                        }
                    }

                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null)
                        in.close();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
