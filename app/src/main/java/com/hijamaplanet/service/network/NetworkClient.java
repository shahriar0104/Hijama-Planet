package com.hijamaplanet.service.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {
    private static Retrofit retrofit;
    //private static final String BASE_URL = "https://hijama.com.bd/hijamaapp/hijama/";
    //private static final String BASE_URL = "https://hijama.com.bd/hijamaapp/swTouO89a2018/titleApp38/hijama/";
    //private static final String BASE_URL = "https://ruqyahbd.com/hijamaapp/";
    //private static final String BASE_URL = "https://iamtorab.com/hijama/";
    private static final String BASE_URL = "http://192.168.1.2/hijama/";

    public static Retrofit newNetworkClient() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
