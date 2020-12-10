package com.example.demoapplication.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.demoapplication.model.ToDoListModel;
import com.example.demoapplication.repository.TaskInfoRepository;
import java.util.List;

public class TaskInfoViewModel extends AndroidViewModel {

    public TaskInfoViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<ToDoListModel>> getData(){
      return TaskInfoRepository.getInstance().getData();
    }

    public void setData(ToDoListModel model){
        TaskInfoRepository.getInstance().setData(model);
    }

    public void removeData(ToDoListModel model){
        TaskInfoRepository.getInstance().removeData(model);
    }
}
