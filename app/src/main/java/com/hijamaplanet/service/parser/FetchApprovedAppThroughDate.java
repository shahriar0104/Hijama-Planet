package com.hijamaplanet.service.parser;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.hijamaplanet.R;
import com.hijamaplanet.admin.adapter.ApprovedAppAdapter;
import com.hijamaplanet.service.network.NetworkClient;
import com.hijamaplanet.service.network.NetworkHelper;
import com.hijamaplanet.service.network.model.GetResponse;
import com.hijamaplanet.service.network.model.UserAppointmentHistory;
import com.hijamaplanet.utils.PrefUserInfo;

import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hijamaplanet.utils.DialogUtils.showFetchingDialog;

public class FetchApprovedAppThroughDate {

    private Context context;
    private PrefUserInfo prefUserInfo;
    private ListView listView;

    public FetchApprovedAppThroughDate(Context context) {
        this.context = context;
        prefUserInfo = new PrefUserInfo(context);
    }

    public FetchApprovedAppThroughDate(Context context, ListView listView) {
        this.context = context;
        prefUserInfo = new PrefUserInfo(context);
        this.listView = listView;
    }

    public void execute(String... params) {
        SpotsDialog spotsDialog = showFetchingDialog(context);
        spotsDialog.show();
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        if (params[0].equalsIgnoreCase("delete")) {
            String appointmentId = params[1];

            NetworkHelper networkHelper = NetworkClient.newNetworkClient().create(NetworkHelper.class);
            Call<GetResponse> call = networkHelper.deleteAppointment(appointmentId, prefUserInfo.getUserMobile(), prefUserInfo.getAdminBranchName());
            call.enqueue(new Callback<GetResponse>() {
                @Override
                public void onResponse(Call<GetResponse> call, Response<GetResponse> response) {
                    spotsDialog.dismiss();
                    if (response.body().getResponse().equalsIgnoreCase("appointment_deleted_successfully")) {
                        alertDialog.setMessage("Appointment Deleted Successfully");
                        alertDialog.show();
                    } else if (response.body().getResponse().equalsIgnoreCase("appointment_deletation_failed")) {
                        alertDialog.setMessage("Appointment Deletion Failed");
                        alertDialog.show();
                    }
                }

                @Override
                public void onFailure(Call<GetResponse> call, Throwable t) {
                    spotsDialog.dismiss();
                    alertDialog.setMessage(context.getResources().getString(R.string.networkResult));
                    alertDialog.show();
                }
            });
        } else if (params[0].equalsIgnoreCase("master")) {
            String date = params[1];

            NetworkHelper networkHelper = NetworkClient.newNetworkClient().create(NetworkHelper.class);
            Call<List<UserAppointmentHistory>> call = networkHelper.fetchMasterApproved(date);
            call.enqueue(new Callback<List<UserAppointmentHistory>>() {
                @Override
                public void onResponse(Call<List<UserAppointmentHistory>> call, Response<List<UserAppointmentHistory>> response) {
                    spotsDialog.dismiss();
                    if (response.body().size() == 0) {
                        listView.setAdapter(new ApprovedAppAdapter(context, response.body()));
                        alertDialog.setMessage("No available appointment on this date");
                        alertDialog.show();
                    } else listView.setAdapter(new ApprovedAppAdapter(context, response.body()));
                }

                @Override
                public void onFailure(Call<List<UserAppointmentHistory>> call, Throwable t) {
                    spotsDialog.dismiss();
                    alertDialog.setMessage(context.getResources().getString(R.string.networkResult));
                    alertDialog.show();
                }
            });
        } else if (params[0].equalsIgnoreCase("admin")) {
            String date = params[1];

            NetworkHelper networkHelper = NetworkClient.newNetworkClient().create(NetworkHelper.class);
            Call<List<UserAppointmentHistory>> call = networkHelper.fetchApproved(prefUserInfo.getUserId(), date);
            call.enqueue(new Callback<List<UserAppointmentHistory>>() {
                @Override
                public void onResponse(Call<List<UserAppointmentHistory>> call, Response<List<UserAppointmentHistory>> response) {
                    spotsDialog.dismiss();
                    if (response.body().size() == 0) {
                        listView.setAdapter(new ApprovedAppAdapter(context, response.body()));
                        alertDialog.setMessage("No available appointment on this date");
                        alertDialog.show();
                    } else listView.setAdapter(new ApprovedAppAdapter(context, response.body()));
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
