package com.example.app.ViewGroup;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.app.GanttItem;
import com.example.app.R;
import com.inqbarna.tablefixheaders.TableFixHeaders;

import java.util.List;

import miguelbcr.ui.tableFixHeadesWrapper.TableFixHeaderAdapter;

/**
 * class for gantt task cells
 */
public class CellViewGroup extends FrameLayout implements
        TableFixHeaderAdapter.FirstBodyBinder<List<String>>,
        TableFixHeaderAdapter.FirstHeaderBinder<String>,
        TableFixHeaderAdapter.HeaderBinder<String>,
        TableFixHeaderAdapter.BodyBinder<List<String>>,
        TableFixHeaderAdapter.SectionBinder<List<String>>
{
    //global
    TextView txt_content;
    List<GanttItem> ganttItemList;
    IOnHeaderClickListener listener;

    public void setListener(IOnHeaderClickListener listener) {
        this.listener = listener;
    }

    public CellViewGroup(@NonNull Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.gantt_view_item,this,true);
        txt_content = (TextView)findViewById(R.id.txt_content);
    }

    public CellViewGroup(@NonNull Context context, List<GanttItem> ganttItemList) {
        super(context);
        this.ganttItemList = ganttItemList;
        LayoutInflater.from(context).inflate(R.layout.gantt_view_item,this,true);
        txt_content = (TextView)findViewById(R.id.txt_content);
    }

    @Override
    public void bindBody(List<String> strings, int i, int i1) {
        txt_content.setText(strings.get(i1));

    }

    @Override
    public void bindFirstHeader(String s) {
        txt_content.setText(s);
    }


    @Override
    public void bindSection(List<String> strings, int i, int i1) {
        txt_content.setText(i1 == 0 ? "Section: "+(i+1) : "");

    }

    @Override
    public void bindHeader(String s, int i) {
        txt_content.setText(s);
    }


    @Override
    public void bindFirstBody(List<String> strings, int i) {
        txt_content.setText(ganttItemList.get(i).getTaskName());
        //Implement Click here
        txt_content.setOnClickListener(new TaskClickListener(i,listener));

    }
}
