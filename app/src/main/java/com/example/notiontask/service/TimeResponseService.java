package com.example.notiontask.service;

import com.example.notiontask.model.TimeResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TimeResponseService {
    @GET("timezone/{timezone}")
    Call<TimeResponseModel> getTime(@Path("timezone") String timezone);

}
