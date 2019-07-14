package com.hijamaplanet.service;

import android.content.Context;
import com.hijamaplanet.service.network.NetworkClient;
import com.hijamaplanet.service.network.NetworkHelper;
import com.hijamaplanet.service.network.model.GetResponse;
import com.hijamaplanet.utils.PrefUserInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateLocationService {
    Context context;

    public UpdateLocationService(Context context) {
        this.context = context;
    }

    public void execute(String location) {
        NetworkHelper networkHelper = NetworkClient.newNetworkClient().create(NetworkHelper.class);
        Call<GetResponse> call = networkHelper.updateLocation(location);
        call.enqueue(new Callback<GetResponse>() {
            @Override
            public void onResponse(Call<GetResponse> call, Response<GetResponse> response) {
                if (response.body().getResponse().equalsIgnoreCase("location_value_updated")) {
                    PrefUserInfo prefUserInfo = new PrefUserInfo(context);
                    prefUserInfo.setLocationSend(true);
                }
            }

            @Override
            public void onFailure(Call<GetResponse> call, Throwable t) {

            }
        });
    }
}
