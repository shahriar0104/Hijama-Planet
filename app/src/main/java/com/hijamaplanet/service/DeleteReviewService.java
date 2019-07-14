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

public class DeleteReviewService {
    AlertDialog alertDialog;
    Context context;
    private PrefUserInfo prefUserInfo;

    public DeleteReviewService(Context context) {
        this.context=context;
        prefUserInfo = new PrefUserInfo(context);
    }

    public void execute(String reviewid){
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Delete Review");

        NetworkHelper networkHelper = NetworkClient.newNetworkClient().create(NetworkHelper.class);
        Call<GetResponse> call = networkHelper.deleteReview(reviewid,prefUserInfo.getUserMobile(), prefUserInfo.getAdminBranchName());
        call.enqueue(new Callback<GetResponse>() {
            @Override
            public void onResponse(Call<GetResponse> call, Response<GetResponse> response) {
                GetResponse getResponses = response.body();
                if (getResponses.getResponse().equalsIgnoreCase("reviewDeletedSuccessfully")){
                    alertDialog.setMessage("Review Deleted Successfully");
                    alertDialog.show();
                } else if (getResponses.getResponse().equalsIgnoreCase("Review Deletion failed")) {
                    alertDialog.setMessage("Review Deletion Failed");
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
