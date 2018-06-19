package com.example.cristian.biblioteca.connection.rf;

import com.example.cristian.biblioteca.Impostazioni;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Cristian on 13/04/2018.
 */

public class RFClient {

    private static String AUTH = "Basic Ym9sZHJpbjpndWVwZXF1ZW5vMDI=";
    private static  String BASE_URL = Impostazioni.DB_ADDRESS;
    private static Retrofit retrofit;
    private static OkHttpClient client;

    static {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request request = chain.request().newBuilder().addHeader("Authorization", AUTH).build();
            return chain.proceed(request);
        });


        client = httpClient.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }


    public static Retrofit getClient() {
        return retrofit;
    }
}
