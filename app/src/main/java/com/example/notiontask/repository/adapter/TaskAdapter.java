package com.example.notiontask.repository.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notiontask.R;
import com.example.notiontask.model.TaskModel;
import com.example.notiontask.ui.task.TaskViewModel;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private Context context;
    private List<TaskModel> tasks;
    private TaskViewModel taskViewModel;

    public TaskAdapter(Context context, List<TaskModel> tasks, TaskViewModel taskViewModel) {
        this.context = context;
        this.tasks = tasks;
        this.taskViewModel = taskViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TaskModel task = tasks.get(position);

        holder.textViewCodigo.setText(task.getCodigo());
        holder.textViewDescription.setText(task.getDescricao());
        holder.textViewTimesInicio.setText(task.getHoraInicio());
        holder.textViewTimes.setText(String.valueOf(task.getTempo()));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewCodigo;
        TextView textViewDescription;
        TextView textViewTimesInicio;
        TextView textViewTimes;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCodigo = itemView.findViewById(R.id.textViewCodigo);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewTimesInicio = itemView.findViewById(R.id.textViewTimesInicio);
            textViewTimes = itemView.findViewById(R.id.textViewTimes);
        }
    }
}
