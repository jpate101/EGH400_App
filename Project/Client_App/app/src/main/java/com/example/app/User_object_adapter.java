package com.example.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class User_object_adapter extends ArrayAdapter<User_object_add_user> {
    public User_object_adapter(@NonNull Context context, int resource, List<User_object_add_user> userList) {
        super(context, resource, userList);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        User_object_add_user user = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_object_add_user_cell, parent,false);
        }

        TextView tv = convertView.findViewById(R.id.user_name);

        tv.setText(user.getName());

        return convertView;
    }
}
