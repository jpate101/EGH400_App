package com.example.app;

import android.graphics.Point;

/**
 * class stores individual task information needed it fill barCells
 */
public class GanttItem {
    private String taskName;
    private boolean isError,isEmpty;
    private Point point;
    private int status;

    public GanttItem(String taskName, boolean isError, Point point, int status) {
        this.taskName = taskName;
        this.isError = isError;
        this.point = point;
        this.status = status;
    }

    public GanttItem(String taskName, boolean isEmpty) {
        this.taskName = taskName;
        this.isEmpty = isEmpty;
    }

    public int getStatus() {
        return status;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }
}
