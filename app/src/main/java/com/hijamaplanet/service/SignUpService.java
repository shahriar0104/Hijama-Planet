package com.hijamaplanet.service;

import android.app.AlertDialog;
import android.content.Context;


import com.hijamaplanet.R;
import com.hijamaplanet.service.network.NetworkClient;
import com.hijamaplanet.service.network.NetworkHelper;
import com.hijamaplanet.service.network.model.GetResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpService {
    Context context;

    public SignUpService(Context ctx) {
        context = ctx;
    }

    public void execute(String name, String mobile, String age, String gender, String device, String location, String key) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(context.getResources().getString(R.string.networkTitle));

        NetworkHelper networkHelper = NetworkClient.newNetworkClient().create(NetworkHelper.class);
        Call<GetResponse> call = networkHelper.registration(name, mobile, age, gender, device, location, key);
        call.enqueue(new Callback<GetResponse>() {
            @Override
            public void onResponse(Call<GetResponse> call, Response<GetResponse> response) {
                if (response.body().getResponse().equalsIgnoreCase("account_successfully_created")) {
                    LoginService loginService = new LoginService(context);
                    loginService.execute(mobile);
                } else {
                    alertDialog.setMessage("Account Creation Failed");
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<GetResponse> call, Throwable t) {
                alertDialog.setMessage("Account Creation Failed");
                alertDialog.show();
            }
        });
    }
}
