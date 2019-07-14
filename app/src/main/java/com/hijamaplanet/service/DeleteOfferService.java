package com.hijamaplanet.service;

import android.app.AlertDialog;
import android.content.Context;

import com.hijamaplanet.R;
import com.hijamaplanet.service.network.NetworkClient;
import com.hijamaplanet.service.network.NetworkHelper;
import com.hijamaplanet.service.network.model.GetResponse;
import com.hijamaplanet.utils.PrefUserInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteOfferService {
    private AlertDialog alertDialog;
    private Context context;
    private PrefUserInfo prefUserInfo;

    public DeleteOfferService(Context context) {
        this.context = context;
        prefUserInfo = new PrefUserInfo(context);
    }

    public void execute(String offer_id) {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Delete Offer");

        NetworkHelper networkHelper = NetworkClient.newNetworkClient().create(NetworkHelper.class);
        Call<GetResponse> call = networkHelper.deleteOffer(offer_id, prefUserInfo.getUserMobile(), prefUserInfo.getAdminBranchName());
        call.enqueue(new Callback<GetResponse>() {
            @Override
            public void onResponse(Call<GetResponse> call, Response<GetResponse> response) {
                if (response.body().getResponse().equalsIgnoreCase("Offer Deleted Successfully")) {
                    alertDialog.setMessage("Offer Deleted Successfully");
                    alertDialog.show();
                } else if (response.body().getResponse().equalsIgnoreCase("Offer Deletion Failed")) {
                    alertDialog.setMessage("Offer Deletion Failed");
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<GetResponse> call, Throwable t) {
                alertDialog.setMessage(context.getResources().getString(R.string.networkResult));
                alertDialog.show();
            }
        });
    }
}
