package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
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
import java.io.PrintWriter;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity {

    //
    String message = "";
    private static final int SERVERPORT = 12345;
    private static final String ip = "192.168.0.6";
    //private static final String ip = "0.0.0.0";
    //

    private EditText eName;
    private EditText ePassword;
    private Button elogin;
    private TextView eAttemptsInfo;

    private String temp_UserName = "Admin";
    private String temp_Passwowrd = "1234";
    private boolean login_isValid = false;
    private int counter = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eName = findViewById(R.id.et_UserName);
        ePassword = findViewById(R.id.et_Password);
        elogin = findViewById(R.id.btn_login);
        eAttemptsInfo = findViewById(R.id.tv_login_response);

        elogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputName = eName.getText().toString();
                String inputPass = ePassword.getText().toString();

                if(inputName.isEmpty() || inputPass.isEmpty()){
                    Toast.makeText(MainActivity.this,"Error", Toast.LENGTH_SHORT).show();
                }else{
                    login_isValid = login_valid(inputName,inputPass);
                    if(!login_isValid){
                        counter--;
                        Toast.makeText(MainActivity.this,"login fail", Toast.LENGTH_SHORT).show();
                        eAttemptsInfo.setText("Number of ttempts remaining: "+ counter);
                        if(counter == 0){
                            elogin.setEnabled(false);
                        }
                    }else{
                        Toast.makeText(MainActivity.this,"login Successful", Toast.LENGTH_SHORT).show();
                        //add the code to go to new activity
                        Intent intent = new Intent(MainActivity.this,temp_Home.class);
                        startActivity(intent);
                    }
                }
            }

        });
        myTask mt = new myTask();
        mt.execute();
    }

    private boolean login_valid(String User, String Pass){
        if(User.equals(temp_UserName) && Pass.equals(temp_Passwowrd)){
            return true;
        }else{
            return false;
        }

    }
    class myTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {


            try (Socket socket = new Socket(ip, 12345)) {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Scanner scanner = new Scanner(System.in);
                String line = "test";
                while (!"exit".equalsIgnoreCase(line)) {
                    out.println(line);
                    out.flush();
                    System.out.println("Server replied " + in.readLine());
                    break;
                }
                scanner.close();

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("YOUR_APP_LOG_TAG", "I got an error", e);
            }

            return null;
        }
    }

}

