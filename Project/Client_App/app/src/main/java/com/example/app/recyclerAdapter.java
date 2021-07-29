package com.example.app;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * adapter class for projects list in fragment_home.xml
 */
public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.myViewHolder> {

    private OnProjectListener mOnProjectListener;
    private ArrayList<Project_Names_list> Projects;
    public recyclerAdapter(ArrayList<Project_Names_list> Projects, RecyclerViewClickListener listener,OnProjectListener onProjectListener){
        this.Projects = Projects;
        this.mOnProjectListener = onProjectListener;
    }

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nametxt;
        OnProjectListener onProjectListener;

        public myViewHolder(final View view, OnProjectListener onProjectListener){
            super(view);
            nametxt = view.findViewById(R.id.P_Name);
            this.onProjectListener = onProjectListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onProjectListener.onProjectClick(getAbsoluteAdapterPosition());
            //find project name that was selected
            Log.e("find project tab", Projects.get(getAbsoluteAdapterPosition()).Project_names);
            MainActivity.con.Currently_selected_project_view = Projects.get(getAbsoluteAdapterPosition()).Project_names;

        }
    }


    @NonNull
    @Override
    public recyclerAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_home_frag, parent, false);
        return new myViewHolder(itemView, mOnProjectListener);
    }

    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.myViewHolder holder, int position) {
        String name = Projects.get(position).getProject_names();
        holder.nametxt.setText(name);

    }

    @Override
    public int getItemCount() {
        return Projects.size();
    }

    public interface RecyclerViewClickListener{
        void onclick(View v, int position);
    }

    public interface OnProjectListener{
        void onProjectClick(int position);
    }


}
