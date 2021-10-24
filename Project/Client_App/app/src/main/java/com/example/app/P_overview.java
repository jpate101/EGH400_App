package com.example.app;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.format.Time;
import android.util.Log;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.Common.Common;
import com.example.app.GanttAdapter.GanttTableFixHeaderAdapter;
import com.example.app.ViewGroup.IOnHeaderClickListener;
import com.inqbarna.tablefixheaders.TableFixHeaders;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link P_overview#newInstance} factory method to
 * create an instance of this fragment.
 */
public class P_overview extends Fragment implements IOnHeaderClickListener {

    TableFixHeaders tableFixHeaders;
    public static boolean check1 = false;
    public static boolean week_or_day = true;
    public static boolean get_minor_task = false;
    public static TextView task_name;
    private Button eWeek_to_day;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public P_overview() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment P_overview.
     */
    // TODO: Rename and change types and number of parameters
    public static P_overview newInstance(String param1, String param2) {
        P_overview fragment = new P_overview();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_p_overview, container, false);
        //
        tableFixHeaders = (TableFixHeaders)v.findViewById(R.id.tablefixheaders);
        if(!get_minor_task){
            MainActivity.con.state = "GET_PROJECT_TASK_INFORMATION";
        }else{
            MainActivity.con.state = "GET_PROJECT_MINOR_TASK_INFORMATION";
            //MainActivity.con.state = "GET_PROJECT_TASK_INFORMATION";
            Log.e("YOUR_APP_LOG_TAG", "minor tasks");
            //P_overview.get_minor_task = true;
        }
        //
        //P_overview.get_minor_task = false;

        createGanttChart();
        Log.e("YOUR_APP_LOG_TAG", "end of gantt creation");



        eWeek_to_day = v.findViewById(R.id.overview_title);

        Log.e("YOUR_APP_LOG_TAG", "end of gantt creation");

        if(week_or_day){
            eWeek_to_day.setText("TIME LINE - WEEK");
        }else{
            eWeek_to_day.setText("TIME LINE - DAY");
        }

        Log.e("YOUR_APP_LOG_TAG", "end of gantt creation");

        eWeek_to_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                week_or_day = !week_or_day;
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(P_overview.this).attach(P_overview.this).commit();
            }
        });

        Log.e("YOUR_APP_LOG_TAG", "end of gantt creation");

        return v;
    }
    //error is being created bent week x and week 9 = 0 
    private void createGanttChart() {
        //wait for data
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
        // get gantt chart creation values
        ArrayList<Date> All_dates = new ArrayList<>();
        for(int i = 0; i < MainActivity.con.Project_Tasks.size();i++){
            All_dates.add(MainActivity.con.Project_Tasks.get(i).Start);
            All_dates.add(MainActivity.con.Project_Tasks.get(i).End);
        }
        if(All_dates == null || All_dates.isEmpty()){
            All_dates.add(new Date());
            All_dates.add(new Date());
        }
        Date Max = Collections.max(All_dates);
        Date Min = Collections.min(All_dates);
        Log.e("YOUR_APP_LOG_TAG", "Date stuff  "+String.valueOf(Max));
        Log.e("YOUR_APP_LOG_TAG", "Date stuff  "+String.valueOf(Min));

        int weeks;
        long diffSeconds;

        if(week_or_day){
            diffSeconds = (Max.getTime() - Min.getTime())/1000;
            weeks = (int)diffSeconds/(60 * 60 * 24 * 7);
        }else{
            diffSeconds = (Max.getTime() - Min.getTime())/1000;
            weeks = (int)diffSeconds/(60 * 60 * 24);
        }

        Log.e("YOUR_APP_LOG_TAG", "Date stuff  "+String.valueOf(weeks));
        Common.HEADER_COUNT = weeks+1;
        Common.COLUMN_COUNT = weeks*2+1;

        Max = new Date();
        if(week_or_day){
            diffSeconds = (Max.getTime() - Min.getTime())/1000;
            weeks = (int)diffSeconds/(60 * 60 * 24 * 7);
        }else{
            diffSeconds = (Max.getTime() - Min.getTime())/1000;
            weeks = (int)diffSeconds/(60 * 60 * 24);
        }

        Common.CURRENT_WEEK = weeks;
        //create list item
        List<GanttItem> ganttItemList = new ArrayList<>();
        //repeat to write incomplete jobs
        for(int i = 0; i < MainActivity.con.Project_Tasks.size();i++){

            int weeks_x;
            int weeks_y;
            if(week_or_day){
                long diffSeconds_x = ( MainActivity.con.Project_Tasks.get(i).Start.getTime()-Collections.min(All_dates).getTime() )/1000;
                weeks_x = (int)diffSeconds_x/(60 * 60 * 24 * 7);

                long diffSeconds_y = ( MainActivity.con.Project_Tasks.get(i).End.getTime() - Collections.min(All_dates).getTime() )/1000;
                weeks_y = (int)diffSeconds_y/(60 * 60 * 24 * 7);
            }else{
                long diffSeconds_x = ( MainActivity.con.Project_Tasks.get(i).Start.getTime()-Collections.min(All_dates).getTime() )/1000;
                weeks_x = (int)diffSeconds_x/(60 * 60 * 24);

                long diffSeconds_y = ( MainActivity.con.Project_Tasks.get(i).End.getTime() - Collections.min(All_dates).getTime() )/1000;
                weeks_y = (int)diffSeconds_y/(60 * 60 * 24);
            }

            if(MainActivity.con.Project_Tasks.get(i).Status == 1){
                if(weeks_y < weeks_x){
                    ganttItemList.add(new GanttItem(MainActivity.con.Project_Tasks.get(i).Task_Name, true));
                }else if(weeks_y == weeks_x){
                    ganttItemList.add(new GanttItem(MainActivity.con.Project_Tasks.get(i).Task_Name, false, new Point(weeks_x, weeks_y), MainActivity.con.Project_Tasks.get(i).Status));
                }else{
                    ganttItemList.add(new GanttItem(MainActivity.con.Project_Tasks.get(i).Task_Name, false, new Point(weeks_x, weeks_y), MainActivity.con.Project_Tasks.get(i).Status));
                }
            }else{
                //ganttItemList.add(new GanttItem(MainActivity.con.Project_Tasks.get(i).Task_Name , true,new Point(weeks_x,weeks_y)));
            }

            //ganttItemList.add(new GanttItem(MainActivity.con.Project_Tasks.get(i).Task_Name ,true));
            Log.e("YOUR_APP_LOG_TAG", "Date stuff  weeks_x  "+String.valueOf(weeks_x));
            Log.e("YOUR_APP_LOG_TAG", "Date stuff  weeks_y  "+String.valueOf(weeks_y));

        }
        //write all complete
        for(int i = 0; i < MainActivity.con.Project_Tasks.size();i++){

            int weeks_x;
            int weeks_y;
            if(week_or_day){
                long diffSeconds_x = ( MainActivity.con.Project_Tasks.get(i).Start.getTime()-Collections.min(All_dates).getTime() )/1000;
                weeks_x = (int)diffSeconds_x/(60 * 60 * 24 * 7);

                long diffSeconds_y = ( MainActivity.con.Project_Tasks.get(i).End.getTime() - Collections.min(All_dates).getTime() )/1000;
                weeks_y = (int)diffSeconds_y/(60 * 60 * 24 * 7);
            }else{
                long diffSeconds_x = ( MainActivity.con.Project_Tasks.get(i).Start.getTime()-Collections.min(All_dates).getTime() )/1000;
                weeks_x = (int)diffSeconds_x/(60 * 60 * 24);

                long diffSeconds_y = ( MainActivity.con.Project_Tasks.get(i).End.getTime() - Collections.min(All_dates).getTime() )/1000;
                weeks_y = (int)diffSeconds_y/(60 * 60 * 24);
            }

            if(MainActivity.con.Project_Tasks.get(i).Status == 2){
                if(weeks_y < weeks_x){
                    ganttItemList.add(new GanttItem(MainActivity.con.Project_Tasks.get(i).Task_Name, true));
                }else if(weeks_y == weeks_x){
                    ganttItemList.add(new GanttItem(MainActivity.con.Project_Tasks.get(i).Task_Name, false, new Point(weeks_x, weeks_y), MainActivity.con.Project_Tasks.get(i).Status));
                }else{
                    ganttItemList.add(new GanttItem(MainActivity.con.Project_Tasks.get(i).Task_Name, false, new Point(weeks_x, weeks_y), MainActivity.con.Project_Tasks.get(i).Status));
                }
            }else{
                //ganttItemList.add(new GanttItem(MainActivity.con.Project_Tasks.get(i).Task_Name , true,new Point(weeks_x,weeks_y)));
            }

            //ganttItemList.add(new GanttItem(MainActivity.con.Project_Tasks.get(i).Task_Name ,true));
            Log.e("YOUR_APP_LOG_TAG", "Date stuff  weeks_x  "+String.valueOf(weeks_x));
            Log.e("YOUR_APP_LOG_TAG", "Date stuff  weeks_y  "+String.valueOf(weeks_y));

        }
        //write all  cancelled
        for(int i = 0; i < MainActivity.con.Project_Tasks.size();i++){
            int weeks_x;
            int weeks_y;
            if(week_or_day){
                long diffSeconds_x = ( MainActivity.con.Project_Tasks.get(i).Start.getTime()-Collections.min(All_dates).getTime() )/1000;
                weeks_x = (int)diffSeconds_x/(60 * 60 * 24 * 7);

                long diffSeconds_y = ( MainActivity.con.Project_Tasks.get(i).End.getTime() - Collections.min(All_dates).getTime() )/1000;
                weeks_y = (int)diffSeconds_y/(60 * 60 * 24 * 7);
            }else{
                long diffSeconds_x = ( MainActivity.con.Project_Tasks.get(i).Start.getTime()-Collections.min(All_dates).getTime() )/1000;
                weeks_x = (int)diffSeconds_x/(60 * 60 * 24);

                long diffSeconds_y = ( MainActivity.con.Project_Tasks.get(i).End.getTime() - Collections.min(All_dates).getTime() )/1000;
                weeks_y = (int)diffSeconds_y/(60 * 60 * 24);
            }

            if(MainActivity.con.Project_Tasks.get(i).Status == 3){
                if(weeks_y < weeks_x){
                    ganttItemList.add(new GanttItem(MainActivity.con.Project_Tasks.get(i).Task_Name, true));
                }else if(weeks_y == weeks_x){
                    ganttItemList.add(new GanttItem(MainActivity.con.Project_Tasks.get(i).Task_Name, false, new Point(weeks_x, weeks_y), MainActivity.con.Project_Tasks.get(i).Status));
                }else{
                    ganttItemList.add(new GanttItem(MainActivity.con.Project_Tasks.get(i).Task_Name, false, new Point(weeks_x, weeks_y), MainActivity.con.Project_Tasks.get(i).Status));
                }
            }else{
                //ganttItemList.add(new GanttItem(MainActivity.con.Project_Tasks.get(i).Task_Name , true,new Point(weeks_x,weeks_y)));
            }

            //ganttItemList.add(new GanttItem(MainActivity.con.Project_Tasks.get(i).Task_Name ,true));
            Log.e("YOUR_APP_LOG_TAG", "Date stuff  weeks_x  "+String.valueOf(weeks_x));
            Log.e("YOUR_APP_LOG_TAG", "Date stuff  weeks_y  "+String.valueOf(weeks_y));

        }
        if(ganttItemList == null || ganttItemList.isEmpty() ){
            ganttItemList.add(new GanttItem("no project information" , true));
            Common.HEADER_COUNT = 20;
            Common.COLUMN_COUNT = 40;
            Common.CURRENT_WEEK = 1;
        }
        //adapter
        GanttTableFixHeaderAdapter adapter = new GanttTableFixHeaderAdapter(getContext(),ganttItemList);
        //set listener late

        //create body
        List<List<String>> body = getBody(ganttItemList);

        adapter.setFirstHeader("Task Name");
        adapter.setHeader(getHeader());
        adapter.setFirstBody(body);
        adapter.setBody(body);
        adapter.setSection(body);
        adapter.setListener(this);

        //set adapter
        tableFixHeaders.setAdapter(adapter);

    }

    private List<String> getHeader() {
        List<String> headers = new ArrayList<>();
        for(int i =0;i<=Common.HEADER_COUNT;i++)
            headers.add(new StringBuilder().append(i).toString());
        return headers;
    }

    private List<List<String>> getBody(List<GanttItem> ganttItemList) {
        List<List<String>> rows = new ArrayList<>();
        for(GanttItem ganttItem: ganttItemList){
            //each gantt item will create body
            List<String> cols = new ArrayList<>();
            if(!ganttItem.isEmpty()){
                for(int col = 0; col< Common.COLUMN_COUNT;col++) {

                    if(ganttItem.getStatus() == 2){
                        if(col == Common.CURRENT_WEEK){
                            cols.add("complete & current_week");
                        }else{
                            cols.add("complete");
                        }
                    }else if (col >= ganttItem.getPoint().x) {
                        if (col <= ganttItem.getPoint().y) {
                            Log.e("YOUR_APP_LOG_TAG", "X    :   " + ganttItem.getPoint().x);
                            Log.e("YOUR_APP_LOG_TAG", "Y    :   " + ganttItem.getPoint().y);
                            Log.e("YOUR_APP_LOG_TAG", "Col    :   " + col);

                            if(col == Common.CURRENT_WEEK && ganttItem.isError() ){
                                cols.add("done & current_week");
                            } else if(col == Common.CURRENT_WEEK && !ganttItem.isError()){
                                cols.add("done & current_week");
                            }else if (ganttItem.isError()) {
                                cols.add("error");
                            } else {
                                cols.add("done");
                            }
                        } else if (col <= Common.CURRENT_WEEK) {
                            cols.add("overtime");
                        }else{
                            if(col == Common.CURRENT_WEEK){
                                cols.add("current_week");
                            }else {
                                cols.add("default");
                            }
                        }
                    }else{
                        if(col == Common.CURRENT_WEEK){
                            cols.add("current_week");
                        }else {
                            cols.add("default");
                        }
                    }
                }

            }else{//if just empty row
                for(int col = 0; col< Common.COLUMN_COUNT;col++){
                    if(col == Common.CURRENT_WEEK){
                        cols.add("current_week");
                    }else {
                        cols.add("default");
                    }
                }
            }
            rows.add(cols);
        }
        Log.e("YOUR_APP_LOG_TAG_2", "rows    :   " + rows);
        return rows;
    }
    public static String sel_task;

    @Override
    public void onHeaderItemClick(View view, int row) {

        TextView textView = (TextView)view;
        task_name = textView;
        //Toast.makeText(getContext(),"item click "+row+" Content "+textView.getText(), Toast.LENGTH_LONG).show();
        MainActivity.con.state = "GET_TASK_INFO";

        Intent intent = new Intent(getContext(),Task_Page.class);
        startActivity(intent);
    }
}