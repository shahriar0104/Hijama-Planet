package com.hijamaplanet.service.parser;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.hijamaplanet.R;
import com.hijamaplanet.service.network.NetworkClient;
import com.hijamaplanet.service.network.NetworkHelper;
import com.hijamaplanet.service.network.model.UserAppointmentHistory;
import com.hijamaplanet.user.AllAppointment;

import java.util.ArrayList;
import java.util.List;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hijamaplanet.utils.DialogUtils.showFetchingDialog;

public class FetchUserAppointment {
    private static final String TAG = "FetchUserAppointment";
    Context context;

    public FetchUserAppointment(Context context) {
        this.context = context;
    }

    public void execute(String mobile) {
        SpotsDialog spotsDialog = showFetchingDialog(context);
        spotsDialog.show();
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(context.getResources().getString(R.string.networkTitle));

        NetworkHelper networkHelper = NetworkClient.newNetworkClient().create(NetworkHelper.class);
        Call<List<UserAppointmentHistory>> call = networkHelper.userAppointmentHistory(mobile);
        call.enqueue(new Callback<List<UserAppointmentHistory>>() {
            @Override
            public void onResponse(Call<List<UserAppointmentHistory>> call, Response<List<UserAppointmentHistory>> response) {
                Bundle args = new Bundle();
                args.putParcelableArrayList("userAppointment", (ArrayList<? extends Parcelable>) response.body());
                AllAppointment fragment = new AllAppointment();
                fragment.setArguments(args);
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().
                        replace(R.id.content_nav, fragment).addToBackStack(null).commit();
                spotsDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<UserAppointmentHistory>> call, Throwable t) {
                spotsDialog.dismiss();
                alertDialog.setMessage(context.getResources().getString(R.string.networkResult));
                alertDialog.show();
            }
        });
    }
}
