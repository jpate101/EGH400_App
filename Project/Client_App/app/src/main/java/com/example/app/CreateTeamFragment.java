package com.example.app;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateTeamFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 *
 * inflate fragment that contains create new team gui
 */
public class CreateTeamFragment extends Fragment {



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button e_create_project;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateTeamFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateTeamFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateTeamFragment newInstance(String param1, String param2) {
        CreateTeamFragment fragment = new CreateTeamFragment();
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
    private Button e_pro_create;
    private TextView e_P_Name;
    private TextView e_P_Decription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_team, container, false);

        e_pro_create = view.findViewById(R.id.btn_create_project);
        e_P_Name = view.findViewById(R.id.et_new_project_name);
        e_P_Decription = view.findViewById(R.id.et_new_description);
        e_pro_create.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String inputName = e_P_Name.getText().toString();
                String inputDes = e_P_Decription.getText().toString();
                if(inputName.equals("Enter New Project Name")){

                }else{
                    Log.e("Here", inputName+" "+inputDes);
                    Log.e("Here", "button click");
                    MainActivity.con.Project_Name = inputName;
                    MainActivity.con.Project_Description = inputDes;
                    MainActivity.con.state = "CREATE_NEW_PROJECT";
                }

            }
        });


        return view;
    }


    public void test_fun(View view){
        Log.e("Here", "button click");
    }
}