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
    public static server_db_conection main_con;



    public static void main(String[] args) {
        int PORT = 12345;
        //database connection
        try{
            //create connection
            main_con = new server_db_conection("jdbc:mariadb://localhost:1433","egh400_test","root","jpate101");
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
        SHA sha = new SHA();
        private String Test_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<billboard background=\"#7F3FBF\">\n<message>Billboard with custom background and default-coloured message</message>\n</billboard>";
        static Semaphore semaphore = new Semaphore(1);
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }


        @Override
        public void run() {
            rsa = new RSA();
            try{
                rsa.getRSAKeys();
            }catch(Exception e){
                e.printStackTrace();
            }
            byte[] key_en = rsa.privateKey.getEncoded();
            DataOutputStream dOut = null;
            try {
                String line;

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

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
                //System.out.printf("Sent from the client: %s\n", message_s);

                //gen aes
                AES aes = new AES();

                length = dIn.readInt();// read length of incoming message
                message = new byte[length];
                if(length>0) {
                    dIn.readFully(message, 0, message.length); // read the message
                }

                //rsa decryption
                String aes_key_en = rsa.decryptMessage_cipher(message,rsa.publicKey);

                // decode the base64 encoded string
                byte[] decodedKey = Base64.getDecoder().decode(aes_key_en);
                // rebuild key using SecretKeySpec
                SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
                //System.out.println(originalKey);

                aes.secretKey = originalKey;

                String success = "success";
                dOut.writeUTF(aes.encrypt(success));


                line = "empty";

                while (!"exit".equalsIgnoreCase(line)) {
                    line = in.readLine();
                    if(line == null || line.equals("empty")){
                        line = "empty";
                    }else{
                        System.out.printf("Sent from the client: %s\n", line);
                    }
                    line = aes.decrypt(line);

                    //out.println(line);

                    if(line.equals(("LOGIN_request"))){
                        System.out.println("login request rev");
                        String LOGIN_request_user = aes.decrypt(in.readLine());
                        String LOGIN_request_pass = aes.decrypt(in.readLine());
                        String From_db = main_con.get_user_salt(LOGIN_request_user);
                        if(From_db.equals("SQL_ERROR")){
                            System.out.println("F");
                            out.println(aes.encrypt("F"));
                        }else{
                            byte[] user_salt = Base64.getDecoder().decode(From_db);
                            String user_pass_db = main_con.get_user_Pass(LOGIN_request_user);


                            if(sha.encrypt(LOGIN_request_pass,user_salt).equals(user_pass_db)){
                                System.out.println("T");
                                out.println(aes.encrypt("T"));
                            }else{
                                System.out.println("F");
                                out.println(aes.encrypt("F"));
                            }
                        }

                    }
                    if(line.equals(("NEW_USER_request"))){
                        System.out.println("NEW_USER_request");
                        String User = aes.decrypt(in.readLine());
                        String Pass = aes.decrypt(in.readLine());

                        System.out.println(User);
                        System.out.println(Pass);

                        byte[] salt = sha.getSalt();
                        String str = Base64.getEncoder().encodeToString(salt);
                        if(main_con.Insert_New_Current_user(User,str,sha.encrypt(Pass,salt))){
                            out.println(aes.encrypt("T"));
                        }else{
                            out.println(aes.encrypt("F"));
                        }

                    }
                     

                }

            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("connection terminated");
            }
        }
    }
}
