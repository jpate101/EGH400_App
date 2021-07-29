package com.example.app;

import java.util.Date;

/**
 * class stores individual task information
 * different to gantt item as this class will store more then minimum to display information
 * as well as start and end times in date form
 */
public class Task_Object {
    public String Task_Name;
    public int Status;
    public Date Start;
    public Date End;

    public Task_Object(String task_Name, int status, Date start, Date end) {
        Task_Name = task_Name;
        Status = status;
        Start = start;
        End = end;
    }
}
