package com.example.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class act_Home {

    public class balance extends AppCompatActivity {



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);

            BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView2);
            NavController navController = Navigation.findNavController(this,  R.id.homeFragment);
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
        }
    }
}
