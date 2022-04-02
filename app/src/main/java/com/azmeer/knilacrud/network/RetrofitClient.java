package com.azmeer.knilacrud.network;

import android.content.Context;

import com.azmeer.knilacrud.utils.AppConstant;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

        private static Retrofit retrofit;
        public static Retrofit getRetrofitInstance(Context context) {

            if (retrofit == null) {
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                httpClient.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain) throws IOException {
                        Request original = chain.request();
                        // Request customization: add request headers
                        Request.Builder requestBuilder = original.newBuilder();
                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                });
                httpClient.readTimeout(5, TimeUnit.MINUTES);
                httpClient.connectTimeout(5,TimeUnit.MINUTES);
                OkHttpClient client = httpClient.build();
                retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .baseUrl(AppConstant.BASEURL)
                        .build();
            }
            return retrofit;
        }
}