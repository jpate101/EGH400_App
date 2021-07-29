package com.example.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * fill act_home.java/activity_home.xml activity home tab fragment
 */
public class HomeFragment extends Fragment implements recyclerAdapter.OnProjectListener {
    private recyclerAdapter.RecyclerViewClickListener listener;
    private static ArrayList<Project_Names_list> projectsList;
    private RecyclerView recyclerView;
    public static String[] Projects;
    public static Semaphore s = new java.util.concurrent.Semaphore(0);

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    private static View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        //get projects
        MainActivity.con.state = "GET_USER_PROJECTS";
        //
        recyclerView = view.findViewById(R.id.listRev_1);
        projectsList = new ArrayList<>();
        try {
            setProjectinfo();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setAdapter();

        return view;
    }

    private void setAdapter() {
        recyclerAdapter adapter = new recyclerAdapter(projectsList,listener,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }



    private void setProjectinfo() throws InterruptedException {
        //String[] Projects = new String[] { "Orange", "Apple", "Pear", "Strawberry" , "Strawberry" , "Strawberry" , "testing project 1 and some more words and stuff" };
        //waitUntilDone();
        for (int i = 0; i < Projects.length; i++) {
            projectsList.add(new Project_Names_list(Projects[i]));
        }
    }
    public static void setDone() {
        s.release();
    }

    public static void waitUntilDone() throws InterruptedException {
        s.acquire();
    }

    @Override
    public void onProjectClick(int position) {
        //Intent intent = new Intent(MainActivity.this,temp_Home.class);
        //projectsList.get(position);
        Intent intent = new Intent(getActivity(), Project_activity.class);
        startActivity(intent);
    }
}