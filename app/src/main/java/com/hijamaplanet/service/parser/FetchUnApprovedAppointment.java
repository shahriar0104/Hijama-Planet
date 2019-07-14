package com.hijamaplanet.service.parser;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;

import com.hijamaplanet.R;
import com.hijamaplanet.admin.activity.TabbedTreatment;
import com.hijamaplanet.service.network.NetworkClient;
import com.hijamaplanet.service.network.NetworkHelper;
import com.hijamaplanet.service.network.model.UserAppointmentHistory;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hijamaplanet.utils.DialogUtils.showFetchingDialog;

public class FetchUnApprovedAppointment {

    Context context;

    public FetchUnApprovedAppointment(Context ctx) {
        context = ctx;
    }

    public void execute(String... params) {
        SpotsDialog spotsDialog = showFetchingDialog(context);
        spotsDialog.show();

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(context.getResources().getString(R.string.networkTitle));
        alertDialog.setMessage("No Appointment Request Available");

        if (params[0].equalsIgnoreCase("master")) {
            NetworkHelper networkHelper = NetworkClient.newNetworkClient().create(NetworkHelper.class);
            Call<List<UserAppointmentHistory>> call = networkHelper.fetchMasterUnApproved();
            call.enqueue(new Callback<List<UserAppointmentHistory>>() {
                @Override
                public void onResponse(Call<List<UserAppointmentHistory>> call, Response<List<UserAppointmentHistory>> response) {
                    spotsDialog.dismiss();
                    if (response.body().size() == 0) alertDialog.show();
                    else context.startActivity(new Intent(context, TabbedTreatment.class)
                            .putExtra("userAppointment", (ArrayList<? extends Parcelable>) response.body()));
                }

                @Override
                public void onFailure(Call<List<UserAppointmentHistory>> call, Throwable t) {
                    spotsDialog.dismiss();
                    alertDialog.setMessage(context.getResources().getString(R.string.networkResult));
                    alertDialog.show();
                }
            });
        } else if (params[0].equalsIgnoreCase("admin")) {
            String userId = params[1];
            NetworkHelper networkHelper = NetworkClient.newNetworkClient().create(NetworkHelper.class);
            Call<List<UserAppointmentHistory>> call = networkHelper.fetchUnApproved(userId);
            call.enqueue(new Callback<List<UserAppointmentHistory>>() {
                @Override
                public void onResponse(Call<List<UserAppointmentHistory>> call, Response<List<UserAppointmentHistory>> response) {
                    spotsDialog.dismiss();
                    if (response.body().size() == 0) alertDialog.show();
                    else context.startActivity(new Intent(context, TabbedTreatment.class)
                            .putExtra("userAppointment", (ArrayList<? extends Parcelable>) response.body()));
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
}
