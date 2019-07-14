package com.hijamaplanet.service.parser;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.hijamaplanet.R;
import com.hijamaplanet.drawer.fragment.OfferFragment;
import com.hijamaplanet.drawer.fragment.OfferMasterFragment;
import com.hijamaplanet.service.network.NetworkClient;
import com.hijamaplanet.service.network.NetworkHelper;
import com.hijamaplanet.service.network.model.Offer;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hijamaplanet.utils.DialogUtils.showFetchingDialog;

public class FetchOffer {

    private Context context;
    private String checker;
    private Fragment fragment;

    public FetchOffer(Context ctx,String checker) {
        context = ctx;
        this.checker = checker;
    }

    public void execute() {
        SpotsDialog spotsDialog = showFetchingDialog(context);
        spotsDialog.show();
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(context.getResources().getString(R.string.networkTitle));

        NetworkHelper networkHelper = NetworkClient.newNetworkClient().create(NetworkHelper.class);
        Call<List<Offer>> call = networkHelper.getOfferList();
        call.enqueue(new Callback<List<Offer>>() {
            @Override
            public void onResponse(Call<List<Offer>> call, Response<List<Offer>> response) {
                spotsDialog.dismiss();
                if (response.body().size() == 0) Toast.makeText(context, "No Offer Available", Toast.LENGTH_SHORT).show();
                else {
                    Bundle args = new Bundle();
                    args.putParcelableArrayList("offer", (ArrayList<? extends Parcelable>) response.body());

                    if (checker.equalsIgnoreCase("delete")) fragment = new OfferMasterFragment();
                    else fragment = new OfferFragment();

                    fragment.setArguments(args);
                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().
                            replace(R.id.content_nav, fragment).addToBackStack(null).commit();
                }
            }

            @Override
            public void onFailure(Call<List<Offer>> call, Throwable t) {
                spotsDialog.dismiss();
                alertDialog.setMessage(context.getResources().getString(R.string.networkResult));
                alertDialog.show();
            }
        });
    }
}
