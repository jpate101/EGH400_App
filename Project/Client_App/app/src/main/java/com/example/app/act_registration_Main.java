package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

/**
 * registration page java file
 */
public class act_registration_Main extends Activity {

    private Button eBack_act_reg;
    private Button eReg_new_user;

    private EditText eName_reg;
    private EditText ePass_reg;
    private EditText ePass2_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        eBack_act_reg = findViewById(R.id.btn_back_act_reg);
        eReg_new_user = findViewById(R.id.btn_register);

        eName_reg = findViewById(R.id.et_newUser);
        ePass_reg = findViewById(R.id.et_newPass);
        ePass2_reg = findViewById(R.id.et_newPass2);

        eBack_act_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg_page = new Intent(act_registration_Main.this,MainActivity.class);
                startActivity(reg_page);
            }
        });
        eReg_new_user.setOnClickListener(new View.OnClickListener() {

            @Override
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View v) {
                Log.e("YOUR_APP_LOG_TAG", "button click");
                String inputName = eName_reg.getText().toString();
                String inputPass = ePass_reg.getText().toString();
                String inputPass2 = ePass2_reg.getText().toString();

                try {
                    if(inputPass.equals(inputPass2)){

                        MainActivity.con.New_user = inputName;
                        MainActivity.con.New_Pass = inputPass;
                        //send new user request to server
                        MainActivity.con.state = "NEW_USER_request";

                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(act_registration_Main.this,"reg fail", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }



}
