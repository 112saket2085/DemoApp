package com.example.demoapplication.model;

public class ToDoListModel {

    private final String taskName;
    private final String taskDateTime;

    public ToDoListModel(String taskName, String taskDateTime) {
        this.taskName = taskName;
        this.taskDateTime = taskDateTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDateTime() {
        return taskDateTime;
    }
}
