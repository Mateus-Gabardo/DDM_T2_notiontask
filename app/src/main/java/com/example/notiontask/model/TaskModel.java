package com.example.notiontask.model;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "task")
public class TaskModel {
    @PrimaryKey
    @NotNull
    private String codigo;
    @NotNull
    private String descricao;
    private String horaInicio;
    private double tempo;

    public TaskModel(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    @Ignore
    public TaskModel(String codigo, String descricao, String horaInicio, double tempo) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.horaInicio = horaInicio;
        this.tempo = tempo;
    }

    public void setDescricao(@NotNull String descricao) {
        this.descricao = descricao;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }


    public void setTempo(double tempo) {
        this.tempo = tempo;
    }

    @NotNull
    public String getCodigo() {
        return codigo;
    }

    @NotNull
    public String getDescricao() {
        return descricao;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public double getTempo() {
        return tempo;
    }
}
