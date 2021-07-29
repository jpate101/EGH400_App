package com.example.app.GanttAdapter;
import android.content.Context;

import miguelbcr.ui.tableFixHeadesWrapper.TableFixHeaderAdapter;

import com.example.app.Common.Common;
import com.example.app.GanttItem;
import com.example.app.P_overview;
import com.example.app.R;
import com.example.app.ViewGroup.BarCellViewGroup;
import com.example.app.ViewGroup.CellViewGroup;
import com.example.app.ViewGroup.IOnHeaderClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * adapter class for gantt chart
 */
public class GanttTableFixHeaderAdapter extends TableFixHeaderAdapter<
        String, CellViewGroup,// first header
        String, CellViewGroup,//header items
        List<String>,//data source
        CellViewGroup,//first body
        BarCellViewGroup,//body items
        CellViewGroup// Section
        > {

    private Context context;
    private List<GanttItem> ganttItems;
    private IOnHeaderClickListener listener;

    public void setListener(IOnHeaderClickListener listener){
        this.listener = listener;
    }

    public GanttTableFixHeaderAdapter(Context context, List<GanttItem> ganttItems) {
        super(context);
        this.context = context;
        this.ganttItems = ganttItems;
    }

    @Override
    protected CellViewGroup inflateFirstHeader() {
        return new CellViewGroup(context);
    }

    @Override
    protected CellViewGroup inflateHeader() {
        CellViewGroup defaultCellViewGroup = new CellViewGroup(context);
        defaultCellViewGroup.setListener(listener);//set click
        //return new CellViewGroup(context);
        return defaultCellViewGroup;
    }

    @Override
    protected CellViewGroup inflateFirstBody() {
        CellViewGroup defaultCellViewGroup = new CellViewGroup(context,ganttItems);
        defaultCellViewGroup.setListener(listener);//set click
        //return new CellViewGroup(context);
        return defaultCellViewGroup;
    }

    @Override
    protected BarCellViewGroup inflateBody() {
        return new BarCellViewGroup(context, ganttItems);
    }

    @Override
    protected CellViewGroup inflateSection() {
        return new CellViewGroup(context);
    }

    @Override
    protected List<Integer> getHeaderWidths() {
        List<Integer> headerWidths = new ArrayList<>();
        //first body will have 150dp
        headerWidths.add((int) context.getResources().getDimension(R.dimen._150dp));
        for(int i = 0; i< Common.COLUMN_COUNT; i++){
            headerWidths.add((int) context.getResources().getDimension(R.dimen._40dp));//each item will have 40dp

        }
        return headerWidths;
    }

    @Override
    protected int getHeaderHeight() {
        return (int)context.getResources().getDimension(R.dimen._40dp);
    }

    @Override
    protected int getSectionHeight() {
        return (int)context.getResources().getDimension(R.dimen._40dp);
    }

    @Override
    protected int getBodyHeight() {
        return (int)context.getResources().getDimension(R.dimen._40dp);
    }

    @Override
    protected boolean isSection(List<List<String>> list, int i) {
        return false;
    }

}
