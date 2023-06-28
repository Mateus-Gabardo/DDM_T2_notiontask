package com.example.notiontask.repository;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

import androidx.room.Room;

import com.example.notiontask.AppDatabase;
import com.example.notiontask.model.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private Context context;
    private AppDatabase db;

    public TaskRepository(Context context) {
        this.context = context;
        db = Room.databaseBuilder(context,
                AppDatabase.class, "ddm.sqlite").allowMainThreadQueries().build();
    }

    public void excluirTask(TaskModel task){
        try {
            db.taskDAO().deleteTask(task);
        }
        catch (Exception e){
            return;
        }
    }

    public boolean salvarTask(TaskModel task){
        try {
            db.taskDAO().insertAll(task);
            return true;
        }
        catch (SQLiteConstraintException e){
            return false;
        }
    }

    public boolean atualizarTask(TaskModel task){
        try {
            db.taskDAO().updateTask(task);
            return true;
        }
        catch (SQLiteConstraintException e){
            return false;
        }
    }

    public TaskModel getTask(String codigo){
        try {
            TaskModel TaskModel = db.taskDAO().getTask(codigo);
            return TaskModel;
        }
        catch (Exception e){
            return null;
        }
    }

    public List<TaskModel> listarTask(){
        try {
            List<TaskModel> TaskModels = db.taskDAO().getAll();
            return TaskModels;
        }
        catch (Exception e){
            return new ArrayList<>();
        }
    }
}
