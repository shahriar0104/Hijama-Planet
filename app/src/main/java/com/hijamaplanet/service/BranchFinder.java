package com.hijamaplanet.service;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.hijamaplanet.R;
import com.hijamaplanet.admin.activity.AdminView;
import com.hijamaplanet.service.network.NetworkClient;
import com.hijamaplanet.service.network.NetworkHelper;
import com.hijamaplanet.service.network.model.BranchAdmin;
import com.hijamaplanet.utils.PrefUserInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BranchFinder {
    private Context context;
    private ProgressDialog dialog;

    public BranchFinder(Context context) {
        this.context = context;
    }

    public BranchFinder(Context context, ProgressDialog dialog) {
        this.context = context;
        this.dialog = dialog;
    }

    public void execute(String branchAdminId) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(context.getResources().getString(R.string.networkTitle));

        NetworkHelper networkHelper = NetworkClient.newNetworkClient().create(NetworkHelper.class);
        Call<List<BranchAdmin>> call = networkHelper.getBranchAdmin(branchAdminId);
        call.enqueue(new Callback<List<BranchAdmin>>() {
            @Override
            public void onResponse(Call<List<BranchAdmin>> call, Response<List<BranchAdmin>> response) {
                List<BranchAdmin> branchAdmin = response.body();
                if (response.body().size() == 0) {
                    if (dialog.isShowing()) dialog.dismiss();
                    alertDialog.setMessage("Couldn't find your Branch Address");
                    alertDialog.show();
                } else {
                    PrefUserInfo prefUserInfo = new PrefUserInfo(context);
                    prefUserInfo.setAdminBranchName(branchAdmin.get(0).getBranch_name());

                    if (dialog.isShowing()) dialog.dismiss();
                    context.startActivity(new Intent(context, AdminView.class));
                }
            }

            @Override
            public void onFailure(Call<List<BranchAdmin>> call, Throwable t) {
                if (dialog.isShowing()) dialog.dismiss();
                alertDialog.setMessage(context.getResources().getString(R.string.networkResult));
                alertDialog.show();
            }
        });
    }
}
