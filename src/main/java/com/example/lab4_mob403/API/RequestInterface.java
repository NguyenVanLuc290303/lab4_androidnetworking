package com.example.lab4_mob403.API;

import com.example.lab4_mob403.model.ServerRequest;
import com.example.lab4_mob403.model.ServerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequestInterface {
    @POST("lab4_MOB403/")
    Call<ServerResponse> operation(@Body ServerRequest request);
}
