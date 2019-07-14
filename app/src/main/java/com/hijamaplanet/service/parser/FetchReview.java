package com.hijamaplanet.service.parser;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.hijamaplanet.R;
import com.hijamaplanet.admin.fragment.DeleteReview;
import com.hijamaplanet.drawer.fragment.ReviewFragment;
import com.hijamaplanet.service.network.NetworkClient;
import com.hijamaplanet.service.network.NetworkHelper;
import com.hijamaplanet.service.network.model.Review;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hijamaplanet.utils.DialogUtils.showFetchingDialog;

public class FetchReview {

    private Context context;
    private String checker;
    private Fragment fragment;

    public FetchReview(Context ctx, String checker) {
        context = ctx;
        this.checker=checker;
    }

    public void execute() {
        SpotsDialog spotsDialog = showFetchingDialog(context);
        spotsDialog.show();
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(context).create();
        alertDialog.setTitle(context.getResources().getString(R.string.networkTitle));

        NetworkHelper networkHelper = NetworkClient.newNetworkClient().create(NetworkHelper.class);
        Call<List<Review>> call = networkHelper.fetchReview();
        call.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                spotsDialog.dismiss();
                if (response.body().size() == 0) {
                    alertDialog.setMessage("No Review Posted yet");
                    alertDialog.setCancelable(true);
                    alertDialog.show();
                } else {
                    Bundle args = new Bundle();
                    args.putParcelableArrayList("review", (ArrayList<? extends Parcelable>) response.body());

                    if (checker.equalsIgnoreCase("delete")) fragment = new DeleteReview();
                    else fragment = new ReviewFragment();

                    fragment.setArguments(args);
                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().
                            replace(R.id.content_nav, fragment).addToBackStack(null).commit();
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                spotsDialog.dismiss();
                alertDialog.setMessage(context.getResources().getString(R.string.networkResult));
                alertDialog.show();
            }
        });
    }
}
