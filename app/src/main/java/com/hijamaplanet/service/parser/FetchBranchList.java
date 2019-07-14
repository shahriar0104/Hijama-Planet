package com.hijamaplanet.service.parser;

import android.app.AlertDialog;
import android.content.Context;

import com.hijamaplanet.R;
import com.hijamaplanet.service.network.NetworkCallback;
import com.hijamaplanet.service.network.NetworkClient;
import com.hijamaplanet.service.network.NetworkHelper;
import com.hijamaplanet.service.network.model.Branch;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hijamaplanet.utils.DialogUtils.showFetchingDialog;

public class FetchBranchList {

    private Context context;
    private NetworkCallback networkCallback;

    public FetchBranchList(Context ctx) {
        context = ctx;
        networkCallback = (NetworkCallback) ctx;
    }

    public void execute() {
        SpotsDialog spotsDialog = showFetchingDialog(context);
        spotsDialog.show();
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(context.getResources().getString(R.string.networkTitle));

        NetworkHelper networkHelper = NetworkClient.newNetworkClient().create(NetworkHelper.class);
        Call<List<Branch>> call = networkHelper.fetchBranchList();
        call.enqueue(new Callback<List<Branch>>() {
            @Override
            public void onResponse(Call<List<Branch>> call, Response<List<Branch>> response) {
                spotsDialog.dismiss();
                List<String> BranchList = new ArrayList<>();
                for (Branch branch : response.body()) BranchList.add(branch.getBranch_name());
                networkCallback.onFetchData(BranchList);
            }

            @Override
            public void onFailure(Call<List<Branch>> call, Throwable t) {
                spotsDialog.dismiss();
                alertDialog.setMessage(context.getResources().getString(R.string.networkResult));
                alertDialog.show();
            }
        });
    }
}
