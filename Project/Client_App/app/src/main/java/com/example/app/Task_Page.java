package com.example.app;

import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Task_Page extends AppCompatActivity {

    public static String Des;
    public static String Status;
    public static String Assigned_user;
    public static String s_date;
    public static String e_date;
    public static String e_taskname;

    public static ArrayList<User_object_add_user> User_List_obj = new ArrayList<User_object_add_user>();
    ArrayList<User_object_add_user> filteredUser;
    private ListView listView;
    public static String new_user_to_project;

    private static final String TAG = "Task Page";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private TextView mDisplayDate2;
    private DatePickerDialog.OnDateSetListener mDateSetListener2;

    public static EditText task_name;
    public static EditText task_des;
    public static TextView task_start;
    public static TextView task_end;
    public static String select_user;

    public static boolean check1 = false;
    public static boolean check2 = false;

    RadioGroup radioGroup;
    RadioButton radioButton;
    public static String selected_radio_option = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_info);

        TextView tv_Title;
        TextView tv_Description;
        TextView tv_Status;
        TextView tv_ResponsibleUser;
        TextView tv_StartDate;
        TextView tv_EndDate;

        tv_Title = findViewById(R.id.TaskTitle_taskinfopage);
        tv_Description = findViewById(R.id.TaskDes);
        tv_Status = findViewById(R.id.TaskStatus);
        tv_ResponsibleUser = findViewById(R.id.TaskResponsibleUser);
        tv_StartDate = findViewById(R.id.TaskStartDate);
        tv_EndDate = findViewById(R.id.TaskEndDate);

        //
        Button ViewMinorTasks = findViewById(R.id.btn_ViewMinorTasks);
        ViewMinorTasks.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                P_overview.get_minor_task = true;
                startActivity(new Intent(Task_Page.this, Project_activity.class));

            }
        });

        //update status radio button
        radioGroup = findViewById(R.id.radioGroupStatusUpdate);

        Button buttonApplyStutus = findViewById(R.id.btn_change_task_status);
        buttonApplyStutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = radioGroup.getCheckedRadioButtonId();

                radioButton = findViewById(radioId);
                //Toast.makeText(getApplicationContext(),"Selected Radio Button: " + radioButton.getText(),Toast.LENGTH_LONG).show();
                if(radioButton.getText().equals("in progress")){
                    selected_radio_option = "1";
                }else if(radioButton.getText().equals("Resolved")){
                    selected_radio_option = "2";
                }else if(radioButton.getText().equals("Cancelled")){
                    selected_radio_option = "3";
                }else{
                    selected_radio_option = "error";
                }
                MainActivity.con.state = "UPDATE_TASK_STATUS";
            }
        });
        try {
            setUpData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mDisplayDate = findViewById(R.id.tvDateStart2);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Task_Page.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDisplayDate2 = findViewById(R.id.tvDateEnd2);
        mDisplayDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Task_Page.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener2,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                Log.d(TAG,"date: "+dayOfMonth+"/"+month+"/"+year);
                String date = "Start: "+dayOfMonth+"/"+month+"/"+year;
                mDisplayDate.setText(date);
            }
        };
        mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                Log.d(TAG,"date2: "+dayOfMonth+"/"+month+"/"+year);
                String date = "End: "+dayOfMonth+"/"+month+"/"+year;
                mDisplayDate2.setText(date);
            }
        };



        task_name = findViewById(R.id.etTaskName2);
        task_des = findViewById(R.id.etTaskDes2);
        task_start = findViewById(R.id.tvDateStart2);
        task_end = findViewById(R.id.tvDateEnd2);

        Button SubmitMinorTask = findViewById(R.id.btn_SubmitMinorTask);
        SubmitMinorTask.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                select_user = null;
                if(filteredUser == null){
                    select_user = User_List_obj.get(0).getName();
                }else if(filteredUser.isEmpty()){
                    select_user = User_List_obj.get(0).getName();
                }else if(!filteredUser.isEmpty()){
                    select_user = filteredUser.get(0).getName();
                }else{
                    //condition should never be used
                    select_user = "No User Assigned to Task";
                }
                Log.e("task submit clicked : ", task_name.getText().toString()+" "+task_des.getText().toString()+" "+task_start.getText().toString()+" "+task_end.getText().toString()+" "+select_user);
                if(task_name.getText().toString().isEmpty()){
                    Toast.makeText(Task_Page.this,"Insert Task Name", Toast.LENGTH_LONG).show();

                }else if (task_start.getText().toString().isEmpty()){
                    Toast.makeText(Task_Page.this,"Insert Task Start Date", Toast.LENGTH_LONG).show();
                }else if (task_end.getText().toString().isEmpty()){
                    Toast.makeText(Task_Page.this,"Insert Task Start Date", Toast.LENGTH_LONG).show();
                }else{
                    MainActivity.con.state = "INSERT_NEW_MINOR_TASK";

                }

            }
        });

        while(true){
            if(check1 == true){
                break;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        check1 = false;
        String temp = "Task \n"+e_taskname;
        Log.e("YOUR_APP_LOG_TAG", "task page  "+temp+" - "+temp.length());

        tv_Title.setText(temp);
        tv_Description.setText("Description \n"+Des);
        tv_ResponsibleUser.setText("Assign User \n"+Assigned_user);
        //tv_Title.setText("Task \n"+P_overview.task_name);

        tv_StartDate.setText("Task Planned Start Date \n"+s_date);
        tv_EndDate.setText("Task Planned End Date \n"+e_date);
        if(Status.equals("1")){
            tv_Status.setText("Status \nIn Progress");
        }else if(Status.equals("2")){
            tv_Status.setText("Status \nResolved");
        }else if(Status.equals("3")){
            tv_Status.setText("Status \nCanceled");
        }else{
            tv_Status.setText("Status \nError");
        }

        Des = null;
        Status = null;
        Assigned_user = null;
        s_date = null;
        e_date = null;




    }

    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = findViewById(radioId);
        Toast.makeText(getApplicationContext(),"Selected Radio Button: " + radioButton.getText(),Toast.LENGTH_LONG).show();
    }

    public void setUpData() throws InterruptedException {
        if(User_List_obj.isEmpty()){
            MainActivity.con.state = "GET_ALL_USERS_IN_PROJECT_TASKPAGE";
        }
    }
}
