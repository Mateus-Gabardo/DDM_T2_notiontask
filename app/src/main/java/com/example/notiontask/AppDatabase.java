package com.example.notiontask;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.notiontask.dao.TaskDAO;
import com.example.notiontask.model.TaskModel;

@Database(entities = {TaskModel.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    public abstract TaskDAO taskDAO();
}
