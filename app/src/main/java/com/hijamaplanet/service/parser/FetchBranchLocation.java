package com.hijamaplanet.service.parser;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;

import com.hijamaplanet.R;
import com.hijamaplanet.drawer.maps.MapBranchFragment;
import com.hijamaplanet.service.network.NetworkClient;
import com.hijamaplanet.service.network.NetworkHelper;
import com.hijamaplanet.service.network.model.BranchLocation;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hijamaplanet.utils.DialogUtils.showFetchingDialog;

public class FetchBranchLocation {
    Context context;

    public FetchBranchLocation(Context context) {
        this.context = context;
    }

    public void execute() {
        SpotsDialog spotsDialog = showFetchingDialog(context);
        spotsDialog.show();
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(context.getResources().getString(R.string.networkTitle));

        NetworkHelper networkHelper = NetworkClient.newNetworkClient().create(NetworkHelper.class);
        Call<List<BranchLocation>> call = networkHelper.fetchBranchLocation();
        call.enqueue(new Callback<List<BranchLocation>>() {
            @Override
            public void onResponse(Call<List<BranchLocation>> call, Response<List<BranchLocation>> response) {
                Bundle args = new Bundle();
                args.putParcelableArrayList("branchLocation", (ArrayList<? extends Parcelable>) response.body());
                MapBranchFragment fragment = new MapBranchFragment();
                fragment.setArguments(args);
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_nav, fragment).addToBackStack(null).commit();
                spotsDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<BranchLocation>> call, Throwable t) {
                spotsDialog.dismiss();
                alertDialog.setMessage(context.getResources().getString(R.string.networkResult));
                alertDialog.show();
            }
        });
    }
}
