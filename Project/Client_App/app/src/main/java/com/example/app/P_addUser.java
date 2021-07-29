package com.example.app;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link P_addUser#newInstance} factory method to
 * create an instance of this fragment.
 */
public class P_addUser extends Fragment {
    //initialize list var
    ListView add_users_listView;
    ArrayList<String> StringArrayList = new ArrayList<>();
    ArrayAdapter add_users_adapter;
    public static String new_user_to_project;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public P_addUser() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment P_addUser.
     */
    // TODO: Rename and change types and number of parameters
    public static P_addUser newInstance(String param1, String param2) {
        P_addUser fragment = new P_addUser();
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
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_p_add_user, container, false);
        //

        setUpData();
        setUpList(v);
        initSearchWidgets(v);
        setUpOnClick();



        //
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return v;
    }

    private void initSearchWidgets(View v){
        SearchView searchView = v.findViewById(R.id.userListSearchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<User_object_add_user> filteredUser = new ArrayList<User_object_add_user>();
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
                MainActivity.con.state = "ASSIGN_USER_TO_PROJECT";

            }
        });


    }

    public void setUpList(View v) {
        User_object_adapter adapter = new User_object_adapter(getContext(),android.R.layout.simple_list_item_1,User_List_obj);
        listView = v.findViewById(R.id.add_user_list_view);
        listView.setAdapter(adapter);
    }

    static void setUpData() {
        //User_List_obj = new ArrayList<User_object_add_user>();
        //User_object_add_user test_u = new User_object_add_user("admin_from_client");
        //User_List_obj.add(test_u);
        //User_object_add_user test_u2 = new User_object_add_user("josh_from_client");
        //User_List_obj.add(test_u2);
        if(User_List_obj.isEmpty()){
            MainActivity.con.state = "GET_ALL_USERS";
        }

    }




}