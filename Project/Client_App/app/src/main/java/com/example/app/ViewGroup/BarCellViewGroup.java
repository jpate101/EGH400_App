package com.example.app.ViewGroup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.app.GanttItem;
import com.example.app.R;

import java.util.List;

import miguelbcr.ui.tableFixHeadesWrapper.TableFixHeaderAdapter;

/**
 * class for gantt chart cells
 */
public class BarCellViewGroup extends FrameLayout implements TableFixHeaderAdapter.BodyBinder<List<String>> {
    RelativeLayout card_item;
    Context context;
    List<GanttItem> ganttItemList;

    public BarCellViewGroup(@NonNull Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.card_view_item,this,true);
        card_item = (RelativeLayout) findViewById(R.id.card_item);
    }

    public BarCellViewGroup(@NonNull Context context, List<GanttItem> ganttItemList) {
        super(context);
        this.context = context;
        this.ganttItemList = ganttItemList;
        LayoutInflater.from(context).inflate(R.layout.card_view_item,this,true);
        card_item = (RelativeLayout) findViewById(R.id.card_item);
    }
    //this function to add more colours
    @Override
    public void bindBody(List<String> strings, int row, int col) {
        //will be base on string value to set data
        //Log.e("YOUR_APP_LOG_TAG", "bindBody CarCell"+ strings);
        //Log.e("YOUR_APP_LOG_TAG", "bindBody CarCell"+ row);
        //Log.e("YOUR_APP_LOG_TAG", "bindBody CarCell"+ col);
        //Log.e("YOUR_APP_LOG_TAG", "bindBody CarCell"+ strings.size());


        if (col >= strings.size()) {
            //card_item.setBackgroundColor(ContextCompat.getColor(context,android.R.color.holo_orange_dark));
        } else if(strings.get(col).equals("done & current_week")){
            card_item.setBackgroundResource(R.drawable.border_current_with_task);
        }else if(strings.get(col).equals("error & current_week")){
            card_item.setBackgroundResource(R.drawable.border_current_without_task);
        }else if(strings.get(col).equals("error")) {
            card_item.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
        }else if (strings.get(col).equals("done")) {
            card_item.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark));
        }else if(strings.get(col).equals("current_week")){
            card_item.setBackgroundResource(R.drawable.border_current);
        }else if(strings.get(col).equals("overtime")){
            card_item.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_orange_dark));
        }else if(strings.get(col).equals("complete & current_week")){
            card_item.setBackgroundResource(R.drawable.border_current_week_complete);
        }else if(strings.get(col).equals("complete")){
            card_item.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_green_light));
        }else{
            card_item.setBackgroundResource(R.drawable.border);
        }


    }
}
