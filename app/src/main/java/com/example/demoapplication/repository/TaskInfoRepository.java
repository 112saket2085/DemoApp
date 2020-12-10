package com.example.demoapplication.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.demoapplication.model.ToDoListModel;
import com.example.demoapplication.store.InMemoryStore;
import java.util.List;

public class TaskInfoRepository {

    private static TaskInfoRepository instance;

    public static TaskInfoRepository getInstance() {
        if(instance==null) {
            instance = new TaskInfoRepository();
        }
        return instance;
    }

    public LiveData<List<ToDoListModel>> getData() {
        MutableLiveData<List<ToDoListModel>> mutableLiveData = new MutableLiveData<>();
        List<ToDoListModel> userInfoModelList = InMemoryStore.getInstance().getTaskList();
        mutableLiveData.setValue(userInfoModelList);
        return mutableLiveData;
    }

    public void setData(ToDoListModel model) {
        InMemoryStore.getInstance().addTask(model);
    }

    public void removeData(ToDoListModel model) {
        InMemoryStore.getInstance().removeTask(model);
    }
}
