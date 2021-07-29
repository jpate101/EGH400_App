package com.example.app.ViewGroup;

import android.view.View;

/**
 * modified on click listener for task title cells
 */
public class TaskClickListener implements View.OnClickListener {
    private int row;
    private IOnHeaderClickListener listener;

    public TaskClickListener(int row, IOnHeaderClickListener listener) {
        this.row = row;
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onHeaderItemClick(v,row);


    }
}
