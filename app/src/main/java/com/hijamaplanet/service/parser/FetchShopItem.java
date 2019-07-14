package com.hijamaplanet.service.parser;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.hijamaplanet.R;
import com.hijamaplanet.drawer.fragment.ShopFragment;
import com.hijamaplanet.service.network.NetworkClient;
import com.hijamaplanet.service.network.NetworkHelper;
import com.hijamaplanet.service.network.model.Shop;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hijamaplanet.utils.DialogUtils.showFetchingDialog;

public class FetchShopItem {

    private Context context;

    public FetchShopItem(Context ctx) {
        context = ctx;
    }

    public void execute() {
        SpotsDialog spotsDialog = showFetchingDialog(context);
        spotsDialog.show();
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(context.getResources().getString(R.string.networkTitle));

        NetworkHelper networkHelper = NetworkClient.newNetworkClient().create(NetworkHelper.class);
        Call<List<Shop>> call = networkHelper.fetchShop();
        call.enqueue(new Callback<List<Shop>>() {
            @Override
            public void onResponse(Call<List<Shop>> call, Response<List<Shop>> response) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("shop", (ArrayList<? extends Parcelable>) response.body());
                Fragment shopFragment = new ShopFragment();
                shopFragment.setArguments(bundle);

                spotsDialog.dismiss();

                ((FragmentActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_nav, shopFragment).addToBackStack(null)
                        .commit();
            }

            @Override
            public void onFailure(Call<List<Shop>> call, Throwable t) {
                spotsDialog.dismiss();
                alertDialog.setMessage(context.getResources().getString(R.string.networkResult));
                alertDialog.show();
            }
        });

    }
}
