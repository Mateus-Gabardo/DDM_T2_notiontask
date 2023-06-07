package com.example.notiontask.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notiontask.model.TaskModel;

import java.util.List;

@Dao
public interface TaskDAO {

    @Query("SELECT * FROM task")
    List<TaskModel> getAll();

    @Query("SELECT * FROM task WHERE codigo =:codigo")
    TaskModel getTask(String codigo);

    @Insert
    void insertAll(TaskModel... tasks);

    @Update
    void updateTask(TaskModel task);

    @Delete
    void deleteTask(TaskModel task);

}
