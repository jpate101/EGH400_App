package com.example.app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.ServerSocket;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity {
    //
    String message = "";
    private static final int SERVERPORT = 12345;
    private static final String ip = "192.168.0.6";
    //private static final String ip = "0.0.0.0";
    //

    public static client_con con;

    private int counter = 5;

    private String temp_UserName = "Admin";
    private String temp_Passwowrd = "1234";
    private boolean login_isValid = false;

    AES aes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        con = new client_con();

        con.execute();



    }

    /*
    private boolean login_valid(String User, String Pass){
        if(User.equals(temp_UserName) && Pass.equals(temp_Passwowrd)){
            return true;
        }else{
            return false;
        }

    }
    */
    class client_con extends AsyncTask<Void,Void,Void>{


        private PrintWriter out;
        private BufferedReader in;
        private Scanner scanner;

        private EditText eName;
        private EditText ePassword;
        private Button elogin;
        private TextView eAttemptsInfo;

        private Button eRegister_Main;
        private Button eBack_act_reg;

        private String USER_id;

        public ObjectInputStream ois;

        String Currently_selected_project_view = "no project selected";



        String state = "empty";
        //reg new user var
        String New_user = "empty";
        String New_Pass = "empty";
        String Project_Name;
        String Project_Description;


        private WeakReference<MainActivity> activityWeakReference;

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {
            eName = findViewById(R.id.et_UserName);
            ePassword = findViewById(R.id.et_Password);
            elogin = findViewById(R.id.btn_login);
            eAttemptsInfo = findViewById(R.id.tv_login_response);
            eRegister_Main = findViewById(R.id.btn_registration);
            eBack_act_reg = findViewById(R.id.btn_back_act_reg);



            Object server_encryptKey;



            elogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String inputName = eName.getText().toString();
                    String inputPass = ePassword.getText().toString();
                    if(inputName.isEmpty() || inputPass.isEmpty()) {
                        state = "empty";
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this,"login fail", Toast.LENGTH_LONG).show();
                            }
                        });
                    }else{
                        state = "LOGIN_request";
                    }
                }

            });

            eRegister_Main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent reg_page = new Intent(MainActivity.this,act_registration_Main.class);
                    startActivity(reg_page);

                }

            });



            try (Socket socket = new Socket(ip, 12345)) {
                ObjectInputStream inputStream;
                DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

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

                aes = new AES();
                aes.GenerateKeys();
                // get base64 encoded version of the key
                String encodedKey = Base64.getEncoder().encodeToString(aes.secretKey_encoded);

                //rsa encryption
                byte[] aes_key_en = RSA.encryptMessage_cipher(encodedKey,ret);

                dOut.writeInt(aes_key_en.length); // write length of the message
                dOut.write(aes_key_en);

                Log.e("YOUR_APP_LOG_TAG", aes.decrypt(dIn.readUTF()));

                while(!"exit".equalsIgnoreCase(state)){
                    if(state == null || state.equals("empty")){
                        state = "empty";
                    }

                    if(state.equals("EXIT")){
                        break;
                    }
                    if(state.equals("LOGIN_request")){
                        String inputName = eName.getText().toString();
                        String inputPass = ePassword.getText().toString();
                        LOGIN_request(inputName,inputPass);
                        //state = "empty";
                        state = "GET_USER_PROJECTS";
                    }
                    if(state.equals("NEW_USER_request")){
                        NEW_USER_request(New_user,New_Pass);
                        state = "empty";
                    }
                    if(state.equals("CREATE_NEW_PROJECT")){
                        Log.e("YOUR_APP_LOG_TAG", "Create new project sig "+ USER_id);
                        CREATE_NEW_PROJECT(Project_Name, Project_Description);
                        state = "empty";
                    }
                    if(state.equals("GET_USER_PROJECTS")){
                        //HomeFragment.Projects =
                        Log.e("YOUR_APP_LOG_TAG", "get user projects "+ USER_id);
                        GET_USER_PROJECTS();
                        state = "empty";
                    }
                    if(state.equals("GET_ALL_USERS")){
                        Log.e("YOUR_APP_LOG_TAG", "get all users"+ USER_id);
                        GET_ALL_USERS();
                        state = "empty";
                    }
                    if(state.equals("ASSIGN_USER_TO_PROJECT")){
                        Log.e("YOUR_APP_LOG_TAG", "ASSIGN_USER_TO_PROJECT"+ USER_id);
                        ASSIGN_USER_TO_PROJECT();
                        state = "empty";
                    }



                }


                scanner.close();

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                Log.e("YOUR_APP_LOG_TAG", "I got an error", e);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("YOUR_APP_LOG_TAG", "connection terminated");

            return null;
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void LOGIN_request(String inputName, String inputPass) throws Exception {



            out.println(aes.encrypt("LOGIN_request"));
            out.println(aes.encrypt(inputName));
            out.println(aes.encrypt(inputPass));
            if(aes.decrypt(in.readLine()).equals("T")){
                Intent intent = new Intent(MainActivity.this,temp_Home.class);
                startActivity(intent);
                USER_id = inputName;
            }else{


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"login fail", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void NEW_USER_request(String User,String Pass) throws Exception {

            Log.e("YOUR_APP_LOG_TAG", "signal sent new user request");
            out.println(aes.encrypt("NEW_USER_request"));
            out.println(aes.encrypt(User));
            out.println(aes.encrypt(Pass));

            if(aes.decrypt(in.readLine()).equals("T")){
                Intent reg_page = new Intent(MainActivity.this,MainActivity.class);
                startActivity(reg_page);
            }else{


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"login fail", Toast.LENGTH_LONG).show();
                    }
                });
            }


        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void CREATE_NEW_PROJECT(String P_Name, String Description) throws Exception {
            Log.e("YOUR_APP_LOG_TAG", "signal sent new user request");
            out.println(aes.encrypt("CREATE_NEW_PROJECT"));
            out.println(aes.encrypt(P_Name));
            out.println(aes.encrypt(Description));
            out.println(aes.encrypt(USER_id));
            if(aes.decrypt(in.readLine()).equals("T")){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"Project has been created", Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"Project Name is taken please selected new name", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void GET_USER_PROJECTS() throws Exception {
            out.println(aes.encrypt("GET_USER_PROJECTS"));
            out.println(aes.encrypt(USER_id));
            boolean check1 = true;
            String check2;
            ArrayList list = new ArrayList();
            while(check1){
                check2 = aes.decrypt(in.readLine());
                //System.out.println(test);
                Log.e("YOUR_APP_LOG_TAG", check2);

                if(check2.equals("end_of_String_array_n10193197")){
                    break;
                }
                list.add(check2);
            }
            String[] temp_store = (String[]) list.toArray(new String[list.size()]);
            Log.e("YOUR_APP_LOG_TAG", "end of get user projects");
            HomeFragment.Projects = new String[temp_store.length];
            HomeFragment.Projects = temp_store;
            //HomeFragment.setDone();
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void GET_ALL_USERS() throws IOException {
            out.println(aes.encrypt("GET_ALL_USERS"));
            out.println(aes.encrypt(USER_id));

            boolean check1 = true;
            String check2;
            ArrayList list = new ArrayList();
            ArrayList<User_object_add_user> User_List_obj_temp = new ArrayList<User_object_add_user>();
            while(check1){
                check2 = aes.decrypt(in.readLine());
                if(check2.equals("end_of_String_array_n10193197")){
                    break;
                }
                //System.out.println(test);
                Log.e("YOUR_APP_LOG_TAG", check2);
                User_object_add_user test_u = new User_object_add_user(check2);
                if(check2.equals(USER_id)){

                }else{
                    P_addUser.User_List_obj.add(test_u);
                }
                list.add(check2);
            }
            String[] temp_store = (String[]) list.toArray(new String[list.size()]);
            Log.e("YOUR_APP_LOG_TAG", "end of get all users");



        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void ASSIGN_USER_TO_PROJECT() throws IOException {
            out.println(aes.encrypt("ASSIGN_USER_TO_PROJECT"));
            out.println(aes.encrypt(USER_id));
            out.println(aes.encrypt(P_addUser.new_user_to_project));
            out.println(aes.encrypt(Currently_selected_project_view));
            String response = aes.decrypt(in.readLine());
            if(response.equals("T")){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"Success", Toast.LENGTH_LONG).show();
                    }
                });
            } else if (response.equals("error check permission")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"user is not permitted to take this action", Toast.LENGTH_LONG).show();
                    }
                });
            }else if (response.equals("error inserting new user")){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"unable to add user", Toast.LENGTH_LONG).show();
                    }
                });
            } else if (response.equals("need permission")){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"User is not permitted to take this action", Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"Unknown Error", Toast.LENGTH_LONG).show();
                    }
                });
            }

        }
    }

}

