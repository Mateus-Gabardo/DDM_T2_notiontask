package com.example.notiontask.ui.task;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.notiontask.model.TaskModel;
import com.example.notiontask.repository.TaskRepository;

import java.util.List;

public class TaskViewModel extends ViewModel {
    private TaskRepository repository;
    private Context context;
    private MutableLiveData<List<TaskModel>> taskListLiveData;

    public TaskViewModel(Context context, List<TaskModel> taskList) {
        this.context = context;
        this.repository = new TaskRepository(context);
        this.taskListLiveData = new MutableLiveData<>(taskList);
    }

    public LiveData<List<TaskModel>> getTaskListLiveData() {
        return taskListLiveData;
    }

    public void insertTask(TaskModel task) {
        repository.salvarTask(task);
    }

    public void updateTask(TaskModel task) {
        repository.atualizarTask(task);
    }

    public void deleteTask(TaskModel task) {
        repository.atualizarTask(task);
    }
}
