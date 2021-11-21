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

            //start key encryption exchange
            //create RSA keys
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
                //end key exchange
                line = "empty";//variable for storing incomming messages

                while (!"exit".equalsIgnoreCase(line)) {//request loop
                    line = in.readLine();//get incomming message
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
                    if(line.equals(("CREATE_NEW_PROJECT"))){
                        System.out.println("CREATE_NEW_PROJECT");
                        String P_Name = aes.decrypt(in.readLine());
                        String Description = aes.decrypt(in.readLine());
                        String User = aes.decrypt(in.readLine());
                        //search db for same entry
                        Boolean check_1 = server_db_conection.check_for_project(P_Name);
                        //if success
                        System.out.println(check_1);
                        if(check_1 == true){
                            System.out.println("here true");
                            out.println(aes.encrypt("T"));
                            server_db_conection.insert_new_project(P_Name,Description,User);
                        }else{
                            System.out.println("here false");
                            out.println(aes.encrypt("F"));
                        }
                    }
                    if(line.equals(("GET_USER_PROJECTS"))){
                        System.out.println("GET_USER_PROJECTS");
                        String User = aes.decrypt(in.readLine());
                        String[] projects = server_db_conection.get_user_projects(User);
                        for (int i = 0; i < projects.length; i++) {
                            //System.out.println(projects[i]);
                            out.println(aes.encrypt(projects[i]));
                        }
                        out.println(aes.encrypt("end_of_String_array_n10193197"));
                    }
                    if(line.equals(("GET_ALL_USERS"))){
                        System.out.println("GET_ALL_USERS");
                        String User = aes.decrypt(in.readLine());
                        String[] Users = server_db_conection.get_all_users(User);

                        for (int i = 0; i < Users.length; i++) {
                            //System.out.println(projects[i]);
                            out.println(aes.encrypt(Users[i]));
                        }
                        out.println(aes.encrypt("end_of_String_array_n10193197"));
                    }
                    if(line.equals(("ASSIGN_USER_TO_PROJECT"))){
                        System.out.println("ASSIGN_USER_TO_PROJECT");
                        String User = aes.decrypt(in.readLine());
                        String New_User = aes.decrypt(in.readLine());
                        String Project = aes.decrypt(in.readLine());

                        System.out.println(User);
                        System.out.println(New_User);
                        System.out.println(Project);

                        String check = server_db_conection.insert_user_into_project(User,New_User,Project);
                        if(check.equals("T")){
                            out.println(aes.encrypt("T"));
                        } else if (check.equals("error check permission")) {
                            out.println(aes.encrypt("error check permission"));
                        }else if (check.equals("error inserting new user")){
                            out.println(aes.encrypt("error inserting new user"));
                        } else if (check.equals("need permission")){
                            out.println(aes.encrypt("need permission"));
                        }else{
                            out.println(aes.encrypt("unknown error"));
                        }
                    }
                    if(line.equals(("GET_ALL_USERS_IN_PROJECT"))){
                        System.out.println("GET_ALL_USERS_IN_PROJECT");
                        String project = aes.decrypt(in.readLine());
                        String[] Users = server_db_conection.get_all_users_in_project(project);

                        for (int i = 0; i < Users.length; i++) {
                            //System.out.println(projects[i]);
                            out.println(aes.encrypt(Users[i]));
                        }
                        out.println(aes.encrypt("end_of_String_array_n10193197"));

                    }
                    if(line.equals(("INSERT_NEW_TASK"))){
                        System.out.println("INSERT_NEW_TASK");
                        String TaskName = aes.decrypt(in.readLine());
                        String TaskDes = aes.decrypt(in.readLine());
                        String Task_start = aes.decrypt(in.readLine());
                        String Task_end = aes.decrypt(in.readLine());
                        String Task_User = aes.decrypt(in.readLine());
                        String Project = aes.decrypt(in.readLine());
                        String Created_by = aes.decrypt(in.readLine());

                        boolean response = server_db_conection.insert_task_into_project(TaskName,Project,Task_User,Created_by,Task_start,Task_end,TaskDes);
                        System.out.println(response);
                        if(response){
                            out.println(aes.encrypt("T"));
                        }else{
                            out.println(aes.encrypt("F"));
                        }
                    }
                    if(line.equals(("GET_PROJECT_TASK_INFORMATION"))){
                        System.out.println("GET_PROJECT_TASK_INFORMATION");
                        String Project = aes.decrypt(in.readLine());

                        String[] results = server_db_conection.get_project_tasks(Project);

                        for (int i = 0; i < results.length; i++) {
                            //System.out.println(projects[i]);
                            out.println(aes.encrypt(results[i]));
                        }
                        out.println(aes.encrypt("end_of_String_array_n10193197"));
                    }
                    if(line.equals(("GET_TASK_INFO"))){
                        System.out.println("GET_TASK_INFO");
                        String Project = aes.decrypt(in.readLine());
                        String Task = aes.decrypt(in.readLine());

                        System.out.println(Project);
                        System.out.println(Task);

                        String[] results = server_db_conection.GET_TASK_INFO(Project,Task);

                        out.println(aes.encrypt(results[0]));
                        out.println(aes.encrypt(results[1]));
                        out.println(aes.encrypt(results[2]));
                        out.println(aes.encrypt(results[3]));
                        out.println(aes.encrypt(results[4]));
                        out.println(aes.encrypt(results[5]));
                    }
                    if(line.equals(("UPDATE_TASK_STATUS"))){
                        System.out.println("UPDATE_TASK_STATUS");
                        String Project = aes.decrypt(in.readLine());
                        String Task = aes.decrypt(in.readLine());
                        String Status = aes.decrypt(in.readLine());
                        if(server_db_conection.UPDATE_TASK_STATUS(Project,Task,Status)){
                            out.println(aes.encrypt("T"));
                        }else{
                            out.println(aes.encrypt("F"));
                        }

                    }
                    if(line.equals(("INSERT_NEW_MINOR_TASK"))){
                        System.out.println("INSERT_NEW_MINOR_TASK");
                        String RootTask = aes.decrypt(in.readLine());
                        System.out.println(RootTask);
                        String TaskName = aes.decrypt(in.readLine());
                        String TaskDes = aes.decrypt(in.readLine());
                        String Task_start = aes.decrypt(in.readLine());
                        String Task_end = aes.decrypt(in.readLine());
                        String Task_User = aes.decrypt(in.readLine());
                        String Project = aes.decrypt(in.readLine());
                        String Created_by = aes.decrypt(in.readLine());


                        boolean response = server_db_conection.insert_minor_task_into_project(RootTask,TaskName,Project,Task_User,Created_by,Task_start,Task_end,TaskDes);
                        System.out.println(response);
                        if(response){
                            out.println(aes.encrypt("T"));
                        }else{
                            out.println(aes.encrypt("F"));
                        }


                    }
                    if(line.equals(("GET_PROJECT_MINOR_TASK_INFORMATION"))){
                        System.out.println("GET_PROJECT_MINOR_TASK_INFORMATION");
                        String Project = aes.decrypt(in.readLine());
                        String root_task = aes.decrypt(in.readLine());

                        String[] results = server_db_conection.get_project_tasks_minor(Project,root_task);

                        for (int i = 0; i < results.length; i++) {
                            System.out.println(results[i]);
                            out.println(aes.encrypt(results[i]));
                        }
                        System.out.println("GET_PROJECT_MINOR_TASK_INFORMATION END LOOP");
                        out.println(aes.encrypt("end_of_String_array_n10193197"));
                        System.out.println("GET_PROJECT_MINOR_TASK_INFORMATION END");
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
