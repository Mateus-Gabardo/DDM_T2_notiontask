package com.example.notiontask.ui.dashboard;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.notiontask.databinding.FragmentDashboardBinding;
import com.example.notiontask.model.TaskModel;
import com.example.notiontask.model.TimeResponseModel;
import com.example.notiontask.repository.TaskRepository;
import com.example.notiontask.retrofit.RetrofitClient;
import com.example.notiontask.service.ClockBinder;
import com.example.notiontask.service.ClockService;
import com.example.notiontask.service.TimeResponseService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private EditText edCodigo;
    private EditText edDescricao;
    private TextView edTimeInit;
    private TextView edDisplay;
    private Button btStart;
    private Button btStop;
    private TaskModel taskCurrent;
    private TaskRepository repository;
    private boolean started = false;
    private FragmentDashboardBinding binding;
    private TimeResponseService timeService;
    private Executor executor;
    private Handler handler;
    private Date startTime;
    private ClockService clockService;
    private boolean isServiceBound = false;
    private ServiceConnection serviceConnection;
    private Intent serviceIntent;

    public DashboardFragment() {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                ClockBinder clockBinder = (ClockBinder) binder;
                clockService = clockBinder.getService();
                isServiceBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                isServiceBound = false;
            }
        };
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceIntent = new Intent(getActivity(), ClockService.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        edCodigo = binding.edCodigo;
        edDescricao = binding.edDescricao;
        btStart = binding.btStart;
        btStop = binding.btStop;
        edTimeInit = binding.txTimeStart;
        edDisplay = binding.txtDisplay;

        btStart.setOnClickListener(v -> start(edCodigo.getText().toString(), edDescricao.getText().toString()));
        btStop.setOnClickListener(v -> stop());
        View root = binding.getRoot();

        repository = new TaskRepository(getContext());

        timeService = RetrofitClient.getClient().create(TimeResponseService.class);
        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());

        IntentFilter intentFilter = new IntentFilter("CLOCK_UPDATE");
        getActivity().registerReceiver(clockUpdateReceiver, intentFilter);
        return root;
    }

    private void bindClockService() {
        getActivity().bindService(serviceIntent, serviceConnection, 0);
    }

    private void unbindClockService() {
        if (isServiceBound) {
            getActivity().unbindService(serviceConnection);
            isServiceBound = false;
        }
    }

    private BroadcastReceiver clockUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String currentTime = intent.getStringExtra("current_time");
            int elapsedTimeInSeconds = intent.getIntExtra("elapsed_time", 0);
            edDisplay.setText(currentTime);
        }
    };


    private void start(String codigo, String descricao) {
        if (!started) {
            bindClockService();
            getActivity().startService(serviceIntent);
            taskCurrent = new TaskModel(codigo, descricao);
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
            });
        }
    }

    private void stop() {
        if (started) {
            getActivity().stopService(serviceIntent);
            getActivity().unregisterReceiver(clockUpdateReceiver);
            int elapsedTimeInSeconds = clockService.getElapsedTimeInSeconds();
            String formattedDuration = formatDuration(elapsedTimeInSeconds * 1000); // Multiplicar por 1000 para obter em milissegundos
            taskCurrent.setTempo(elapsedTimeInSeconds);

            salvarTask(taskCurrent);
            started = false;
            limparTela();
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
            }

            @Override
            public void onFailure(Call<TimeResponseModel> call, Throwable t) {
                handler.post(()-> callback.onFailure(t));
            }
        });
    }

    private Date parseDateTime(String dateTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSX", Locale.getDefault());
        try {
            // Parse a data UTC
            Date utcDate = dateFormat.parse(dateTime);

            // Obtém o deslocamento de fuso horário atual do dispositivo
            TimeZone localTimeZone = TimeZone.getDefault();
            int offsetInMillis = localTimeZone.getOffset(utcDate.getTime());

            // Ajusta a data UTC para a hora local correta
            Date localDate = new Date(utcDate.getTime() + offsetInMillis);
            return localDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }



    private String formatTime(Date date) {
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return outputFormat.format(date);
    }

    private String formatDuration(int durationInSeconds) {
        int hours = durationInSeconds / 3600;
        int minutes = (durationInSeconds % 3600) / 60;
        int seconds = durationInSeconds % 60;

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
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
        edTimeInit.setText("00:00:00");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private interface TimeCallback {
        void onSuccess(Date dateTime);
        void onFailure(Throwable t);
    }
}
