package com.example.app;

import android.app.DatePickerDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link P_addTask#newInstance} factory method to
 * create an instance of this fragment.
 */
public class P_addTask extends Fragment {

    private static final String TAG = "P_addTask";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private TextView mDisplayDate2;
    private DatePickerDialog.OnDateSetListener mDateSetListener2;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public P_addTask() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment P_addTask.
     */
    // TODO: Rename and change types and number of parameters
    public static P_addTask newInstance(String param1, String param2) {
        P_addTask fragment = new P_addTask();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    public static ArrayList<User_object_add_user> User_List_obj = new ArrayList<User_object_add_user>();
    ArrayList<User_object_add_user> filteredUser;

    private ListView listView;
    public static String new_user_to_project;
    private Button eSubmitTask;
    public static EditText task_name;
    public static EditText task_des;
    public static TextView task_start;
    public static TextView task_end;
    public static String select_user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        P_overview.get_minor_task = false;
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_p_add_task, container, false);
        //
        mDisplayDate = v.findViewById(R.id.tvDateStart);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDisplayDate2 = v.findViewById(R.id.tvDateEnd);
        mDisplayDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
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
        //select assigned users
        try {
            setUpData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setUpList(v);
        initSearchWidgets(v);
        //setUpOnClick();
        //setup submit button and et's
        task_name = v.findViewById(R.id.etTaskName);
        task_des = v.findViewById(R.id.etTaskDes);
        task_start = v.findViewById(R.id.tvDateStart);
        task_end = v.findViewById(R.id.tvDateEnd);

        eSubmitTask = v.findViewById(R.id.btn_SubmitTask);
        eSubmitTask.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(getContext(),"Insert Task Name", Toast.LENGTH_LONG).show();

                }else if (task_start.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Insert Task Start Date", Toast.LENGTH_LONG).show();
                }else if (task_end.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Insert Task Start Date", Toast.LENGTH_LONG).show();
                }else{
                    MainActivity.con.state = "INSERT_NEW_TASK";

                }
            }
        });
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //
        return v;
    }

    private void initSearchWidgets(View v){
        SearchView searchView = v.findViewById(R.id.userListSearchView2);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredUser = new ArrayList<User_object_add_user>();
                for(User_object_add_user user : User_List_obj){
                    if(user.getName().toLowerCase().contains(newText.toLowerCase())){
                        filteredUser.add(user);
                    }
                }
                User_object_adapter adapter = new User_object_adapter(getContext(),android.R.layout.simple_list_item_1,filteredUser);
                listView.setAdapter(adapter);
                return false;
            }
        });
    }

    public void setUpOnClick() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("user clicked : ", String.valueOf(position));
                new_user_to_project = User_List_obj.get(position).getName();
            }
        });


    }

    public void setUpList(View v){
        while(true){
            if(User_List_obj != null){
                break;
            }
        }
        User_object_adapter adapter = new User_object_adapter(getContext(),android.R.layout.simple_list_item_1,User_List_obj);
        listView = v.findViewById(R.id.add_user_list_view2);
        listView.setAdapter(adapter);
    }

    public void setUpData() throws InterruptedException {
        //User_List_obj = new ArrayList<User_object_add_user>();
        //User_object_add_user test_u = new User_object_add_user("admin_from_client");
        //User_List_obj.add(test_u);
        //User_object_add_user test_u2 = new User_object_add_user("josh_from_client");
        //User_List_obj.add(test_u2);
        //
        if(User_List_obj.isEmpty()){
            MainActivity.con.state = "GET_ALL_USERS_IN_PROJECT";
        }




    }
}