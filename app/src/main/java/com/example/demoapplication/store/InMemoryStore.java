package com.example.demoapplication.store;

import com.example.demoapplication.model.ToDoListModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryStore {

    private static final String TASK_INFO = "task_info";
    private final HashMap<String, List<ToDoListModel>> hashMapTask = new HashMap<>();
    private static InMemoryStore instance;

    public static InMemoryStore getInstance() {
        if(instance==null) {
            instance = new InMemoryStore();
        }
        return instance;
    }

    public InMemoryStore() {
    }

    /**
     * Method to store task details in app memory
     * @param model Task Detail
     */
    public void addTask(ToDoListModel model) {
        List<ToDoListModel> list = getTaskList();
        list.add(model);
        hashMapTask.put(TASK_INFO, list);
    }


    /**
     * Method to remove tasks
     * @param model Task Detail;
     */
    public void removeTask(ToDoListModel model) {
        List<ToDoListModel> list = getTaskList();
        list.remove(model);
        hashMapTask.put(TASK_INFO, list);
    }


    /**
     * Method to retrieve list of added tasks
     * @return Tasks List
     */
    public List<ToDoListModel> getTaskList() {
        if (hashMapTask.containsKey(TASK_INFO)) {
            return hashMapTask.get(TASK_INFO);
        }
        return new ArrayList<>();
    }

}
