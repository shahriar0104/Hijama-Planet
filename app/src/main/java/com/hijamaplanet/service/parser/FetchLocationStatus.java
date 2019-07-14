package com.hijamaplanet.service.parser;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;

import com.hijamaplanet.R;
import com.hijamaplanet.admin.activity.GraphCountryShow;
import com.hijamaplanet.service.network.NetworkClient;
import com.hijamaplanet.service.network.NetworkHelper;
import com.hijamaplanet.service.network.model.LocationData;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hijamaplanet.utils.DialogUtils.showFetchingDialog;

public class FetchLocationStatus {
    private Context context;

    public  FetchLocationStatus(Context ctx) {
        context = ctx;
    }

    public void execute(){
        SpotsDialog spotsDialog = showFetchingDialog(context);
        spotsDialog.show();
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        NetworkHelper networkHelper = NetworkClient.newNetworkClient().create(NetworkHelper.class);
        Call<List<LocationData>> call = networkHelper.fetchLocationStatus();
        call.enqueue(new Callback<List<LocationData>>() {
            @Override
            public void onResponse(Call<List<LocationData>> call, Response<List<LocationData>> response) {
                spotsDialog.dismiss();
                if (response.body().size() == 0) {
                    alertDialog.setMessage("No User signed up yet");
                    alertDialog.show();
                } else context.startActivity(new Intent(context, GraphCountryShow.class)
                            .putExtra("location",(ArrayList<? extends Parcelable>) response.body()));
            }

            @Override
            public void onFailure(Call<List<LocationData>> call, Throwable t) {
                spotsDialog.dismiss();
                alertDialog.setMessage(context.getResources().getString(R.string.networkResult));
                alertDialog.show();
            }
        });
    }
}
