package com.azmeer.knilacrud.network;

import com.azmeer.knilacrud.models.ResultResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface  Api {

    @GET("users")
    Call<ResultResponse> getusers();
}
