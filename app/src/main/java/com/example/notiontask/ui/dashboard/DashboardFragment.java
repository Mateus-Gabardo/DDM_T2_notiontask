package com.example.notiontask.ui.dashboard;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import com.example.notiontask.databinding.FragmentDashboardBinding;
import com.example.notiontask.model.TaskModel;
import com.example.notiontask.model.TimeResponseModel;
import com.example.notiontask.repository.TaskRepository;
import com.example.notiontask.retrofit.RetrofitClient;
import com.example.notiontask.service.TimeResponseService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private EditText edCodigo;
    private EditText edDescricao;

    private TextView edTimeInit;
    private Button btStart;
    private Button btStop;


    private ProgressBar progressBar;

    private TaskModel taskCurrent;

    private TaskRepository repository;

    private boolean started = false;

    private FragmentDashboardBinding binding;

    private TimeResponseService timeService;
    private Executor executor;
    private Handler handler;
    private Date startTime;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        edCodigo = binding.edCodigo;
        edDescricao = binding.edDescricao;
        btStart = binding.btStart;
        btStop = binding.btStop;
        progressBar = binding.progressBar2;
        edTimeInit = binding.txTimeStart;
        progressBar.setVisibility(View.GONE);

        btStart.setOnClickListener(v -> start(edCodigo.getText().toString(), edDescricao.getText().toString()));
        btStop.setOnClickListener(v -> stop());
        View root = binding.getRoot();

        repository = new TaskRepository(getContext());

        timeService = RetrofitClient.getClient().create(TimeResponseService.class);
        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());

        return root;
    }

    private void start(String codigo, String descricao) {
        if (!started) {
            taskCurrent = new TaskModel(codigo, descricao);
            progressBar.setVisibility(View.VISIBLE);
            started = true;

            fetchTimeFromApi("America/Sao_Paulo", new TimeCallback() {
                @Override
                public void onSuccess(Date dateTime) {
                    startTime = dateTime;
                    String formattedTime = formatTime(startTime);
                    taskCurrent.setHoraInicio(formattedTime);
                    edTimeInit.setText(formattedTime);
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(getContext(), "Falha ao obter a hora atual", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFinish() {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private void stop() {
        if (started) {
            String tempoFim = "12:00";
            taskCurrent.setHoraFim(tempoFim);

            fetchTimeFromApi("America/Sao_Paulo", new TimeCallback() {
                @Override
                public void onSuccess(Date dateTime) {
                    Date endTime = dateTime;

                    long duration = endTime.getTime() - startTime.getTime();
                    String formattedDuration = formatDuration(duration);
                    taskCurrent.setHoraFim(formattedDuration);
                    taskCurrent.setTempo(duration);

                    salvarTask(taskCurrent);
                    started = false;
                    progressBar.setVisibility(View.GONE);
                    limparTela();
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(getContext(), "Falha ao obter a hora atual", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFinish() {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private void fetchTimeFromApi(String timezone, TimeCallback callback) {
        Call<TimeResponseModel> call = timeService.getTime(timezone);
        call.enqueue(new Callback<TimeResponseModel>() {
            @Override
            public void onResponse(Call<TimeResponseModel> call, Response<TimeResponseModel> response) {
                if (response.isSuccessful()) {
                    TimeResponseModel timeResponse = response.body();
                    if (timeResponse != null) {
                        String dateTime = timeResponse.getDateTime();
                        if(!dateTime.isEmpty()){
                            Date date = parseDateTime(dateTime);
                            handler.post(() -> callback.onSuccess(date));
                        }
                    }
                }
                handler.post(callback::onFinish);
            }

            @Override
            public void onFailure(Call<TimeResponseModel> call, Throwable t) {
                handler.post(()-> callback.onFailure(t));
                handler.post(callback::onFinish);
            }
        });
    }

    private Date parseDateTime(String dateTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX", Locale.getDefault());
        try {
            return dateFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String formatTime(Date date) {
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return outputFormat.format(date);
    }

    private String formatDuration(long duration) {
        long hours = duration / (60 * 60 * 1000);
        long minutes = (duration % (60 * 60 * 1000)) / (60 * 1000);
        return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes);
    }

    private void salvarTask(TaskModel task) {
        if (repository.salvarTask(task)) {
            Toast.makeText(getContext(), task.getCodigo(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Erro ao salvar Task", Toast.LENGTH_LONG).show();
        }
    }

    private void limparTela() {
        edCodigo.setText("");
        edDescricao.setText("");
        edTimeInit.setText("00:00");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private interface TimeCallback {
        void onSuccess(Date dateTime);
        void onFailure(Throwable t);
        void onFinish();
    }
}
