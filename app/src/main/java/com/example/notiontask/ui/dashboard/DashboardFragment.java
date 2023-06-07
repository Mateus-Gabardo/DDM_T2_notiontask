package com.example.notiontask.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.notiontask.databinding.FragmentDashboardBinding;
import com.example.notiontask.model.TaskModel;
import com.example.notiontask.repository.TaskRepository;

public class DashboardFragment extends Fragment {

    private EditText edCodigo;
    private EditText edDescricao;
    private Button btStart;
    private Button btStop;

    private ProgressBar progressBar;

    private TaskModel taskCurrent;

    private TaskRepository repository;

    private boolean started = false;

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        edCodigo = binding.edCodigo;
        edDescricao = binding.edDescricao;
        btStart = binding.btStart;
        btStop = binding.btStop;
        progressBar = binding.progressBar2;
        progressBar.setVisibility(View.GONE);

        btStart.setOnClickListener(v -> start(edCodigo.getText().toString(), edDescricao.getText().toString()));
        btStop.setOnClickListener(v -> stop());
        View root = binding.getRoot();

        repository = new TaskRepository(getContext());

        return root;
    }

    private void start(String codigo, String descricao){
        if(!started) {
            taskCurrent  = new TaskModel(codigo, descricao);
            String timeInicio = "09:00";
            taskCurrent.setHoraInicio(timeInicio);
            progressBar.setVisibility(View.VISIBLE);
            started = true;
        }
    }

    private void stop(){
        if(started){
            String tempoFim = "12:00";
            taskCurrent.setHoraFim(tempoFim);
            salvarTask(taskCurrent);
            started = false;
            progressBar.setVisibility(View.GONE);
            limparTela();
        }
    }

    private void salvarTask(TaskModel task){
        if(repository.salvarTask(task)){
            Toast.makeText(getContext(), task.getCodigo(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Erro ao salvar Task", Toast.LENGTH_LONG).show();
        }
    }

    private void limparTela(){
        edCodigo.setText("");
        edDescricao.setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}