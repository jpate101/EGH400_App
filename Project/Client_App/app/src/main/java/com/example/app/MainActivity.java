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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.ServerSocket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity {

    //
    String message = "";
    private static final int SERVERPORT = 12345;
    private static final String ip = "192.168.0.6";
    //private static final String ip = "0.0.0.0";
    //

    private int counter = 5;

    private String temp_UserName = "Admin";
    private String temp_Passwowrd = "1234";
    private boolean login_isValid = false;

    RSA rsa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client_con con = new client_con();
        con.execute();

    }

    private boolean login_valid(String User, String Pass){
        if(User.equals(temp_UserName) && Pass.equals(temp_Passwowrd)){
            return true;
        }else{
            return false;
        }

    }
    class client_con extends AsyncTask<Void,Void,Void>{


        private PrintWriter out;
        private BufferedReader in;
        private Scanner scanner;

        private EditText eName;
        private EditText ePassword;
        private Button elogin;
        private TextView eAttemptsInfo;

        private String state = "empty";

        private WeakReference<MainActivity> activityWeakReference;

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {
            eName = findViewById(R.id.et_UserName);
            ePassword = findViewById(R.id.et_Password);
            elogin = findViewById(R.id.btn_login);
            eAttemptsInfo = findViewById(R.id.tv_login_response);

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


            try (Socket socket = new Socket(ip, 12345)) {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                scanner = new Scanner(System.in);
                ObjectInputStream inputStream;

                //key
                inputStream = new ObjectInputStream(socket.getInputStream());
                rsa = new RSA();
                Object obj = inputStream.readObject();

                rsa.update_server_encryptKey(obj);
                out.println(rsa.encryptMessage("this is the message i want to see"));
                //
                while(state.equals("empty")){
                    if(in.equals("EXIT")){
                        break;
                    }
                    if(state.equals("LOGIN_request")){
                        String inputName = eName.getText().toString();
                        String inputPass = ePassword.getText().toString();
                        LOGIN_request(inputName,inputPass);
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

            return null;
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void LOGIN_request(String inputName, String inputPass) throws Exception {

            out.println("LOGIN_request");
            out.println(inputName);
            out.println(inputPass);
            if(in.readLine().equals("T")){
                Intent intent = new Intent(MainActivity.this,temp_Home.class);
                startActivity(intent);
            }else{


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"login fail", Toast.LENGTH_LONG).show();
                    }
                });




            }


        }
    }

}

