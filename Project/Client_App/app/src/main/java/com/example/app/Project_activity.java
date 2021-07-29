package com.example.app;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

/**
 * inflates individual projects activity
 */
public class Project_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bnv_project);
        NavController navController = Navigation.findNavController(this,  R.id.fragment_projects_main);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }
}
