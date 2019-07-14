package com.hijamaplanet.service.parser;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.hijamaplanet.drawer.fragment.TabbedPrice;
import com.hijamaplanet.R;
import com.hijamaplanet.service.network.NetworkClient;
import com.hijamaplanet.service.network.NetworkHelper;
import com.hijamaplanet.service.network.model.TreatmentPrice;

import java.util.ArrayList;
import java.util.List;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hijamaplanet.utils.DialogUtils.showFetchingDialog;

public class FetchTreatmentPrice {

    private Context context;

    public FetchTreatmentPrice(Context context) {
        this.context = context;
    }

    public void execute() {
        SpotsDialog spotsDialog = showFetchingDialog(context);
        spotsDialog.show();
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(context.getResources().getString(R.string.networkTitle));

        NetworkHelper networkHelper = NetworkClient.newNetworkClient().create(NetworkHelper.class);
        Call<List<TreatmentPrice>> call = networkHelper.fetchTreatmentPrice();
        call.enqueue(new Callback<List<TreatmentPrice>>() {
            @Override
            public void onResponse(Call<List<TreatmentPrice>> call, Response<List<TreatmentPrice>> response) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("price", (ArrayList<? extends Parcelable>) response.body());
                Fragment TabbedPrice = new TabbedPrice();
                TabbedPrice.setArguments(bundle);

                spotsDialog.dismiss();

                ((FragmentActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_nav, TabbedPrice).addToBackStack(null)
                        .commit();
            }

            @Override
            public void onFailure(Call<List<TreatmentPrice>> call, Throwable t) {
                spotsDialog.dismiss();
                alertDialog.setMessage(context.getResources().getString(R.string.networkResult));
                alertDialog.show();
            }
        });
    }
}
