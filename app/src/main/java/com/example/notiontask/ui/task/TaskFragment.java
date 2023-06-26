package com.example.notiontask.ui.task;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notiontask.repository.adapter.TaskAdapter;
import com.example.notiontask.databinding.FragmentTaskBinding;
import com.example.notiontask.model.TaskModel;
import com.example.notiontask.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends Fragment {

    private Context context;

    private RecyclerView recyclerView;

    private TaskAdapter taskAdapter;

    private List<TaskModel> tasks;

    private TaskViewModel taskViewModel;

    private Handler handler = new Handler();

    private TaskRepository repository;

    ProgressBar progressBar;
    private FragmentTaskBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        context = getContext();
        recyclerView = binding.recyclerView;
        progressBar = binding.progressBar;
        progressBar.setVisibility(View.GONE);
        repository = new TaskRepository(context);
        tasks = repository.listarTask();
        tasks.addAll(exemplos());
        taskViewModel = new TaskViewModel(context, tasks);
        taskAdapter = new TaskAdapter(context, tasks, taskViewModel);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(taskAdapter);

        return root;
    }

    private List<TaskModel> exemplos(){
        ArrayList<TaskModel> task = new ArrayList<>();
        TaskModel t1 = new TaskModel("ERPDUB-2345", "Desenvolver tela de login", "08:00", 4.0 );
        TaskModel t2 = new TaskModel("ERPDUB-2346", "Desenvolver tela de cadastro", "13:30", 4.5 );
        task.add(t1);
        task.add(t2);
        return task;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}