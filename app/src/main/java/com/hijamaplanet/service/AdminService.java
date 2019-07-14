package com.hijamaplanet.service;

import android.app.AlertDialog;
import android.content.Context;

import com.hijamaplanet.R;
import com.hijamaplanet.service.network.NetworkClient;
import com.hijamaplanet.service.network.NetworkHelper;
import com.hijamaplanet.service.network.model.GetResponse;
import com.hijamaplanet.utils.PrefUserInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminService {
    private AlertDialog alertDialog;
    private Context context;
    private String branchHead;
    private PrefUserInfo prefUserInfo;

    public AdminService(Context ctx) {
        context = ctx;
        prefUserInfo = new PrefUserInfo(context);
        branchHead = prefUserInfo.getAdminBranchName();
    }

    public void execute(String... params) {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(context.getResources().getString(R.string.networkTitle));

        if (params[0].equals("create new Admin")) {
            String user_name = params[1];
            String docMob = params[2];
            String userMob = params[3];
            String age = params[4];
            String user_gender = params[5];
            String branch = params[6];
            String key = params[7];

            NetworkHelper networkHelper = NetworkClient.newNetworkClient().create(NetworkHelper.class);
            Call<GetResponse> call = networkHelper.addAdmin(user_name, docMob, userMob, age, user_gender, branch, branchHead, key);
            call.enqueue(new Callback<GetResponse>() {
                @Override
                public void onResponse(Call<GetResponse> call, Response<GetResponse> response) {
                    if (response.body().getResponse().equalsIgnoreCase("admin created")) {
                        alertDialog.setMessage("Admin Created Successfully");
                        alertDialog.show();
                    } else if (response.body().getResponse().equalsIgnoreCase("admin account creation failed")) {
                        alertDialog.setMessage("Admin Account Creation Failed");
                        alertDialog.show();
                    }
                }

                @Override
                public void onFailure(Call<GetResponse> call, Throwable t) {
                    alertDialog.setMessage(context.getResources().getString(R.string.networkResult));
                    alertDialog.show();
                }
            });
        } else if (params[0].equals("create master Admin")) {
            String user_name = params[1];
            String docMob = params[2];
            String userMob = params[3];
            String age = params[4];
            String user_gender = params[5];
            String key = params[6];

            NetworkHelper networkHelper = NetworkClient.newNetworkClient().create(NetworkHelper.class);
            Call<GetResponse> call = networkHelper.addMasterAdmin(user_name, docMob, userMob, age, user_gender, branchHead, key);
            call.enqueue(new Callback<GetResponse>() {
                @Override
                public void onResponse(Call<GetResponse> call, Response<GetResponse> response) {
                    if (response.body().getResponse().equalsIgnoreCase("admin created")) {
                        alertDialog.setMessage("Admin Created Successfully");
                        alertDialog.show();
                    } else if (response.body().getResponse().equalsIgnoreCase("admin account creation failed")) {
                        alertDialog.setMessage("Admin Account Creation Failed");
                        alertDialog.show();
                    } else if (response.body().getResponse().equalsIgnoreCase("New Master Admin Added Succesfully")) {
                        alertDialog.setMessage("Failed");
                        alertDialog.show();
                    }
                }

                @Override
                public void onFailure(Call<GetResponse> call, Throwable t) {
                    alertDialog.setMessage(context.getResources().getString(R.string.networkResult));
                    alertDialog.show();
                }
            });
        } else if (params[0].equals("approved_appointment")) {
            String appointment_id = params[1];
            String appointment_date = params[2];
            String appointment_time = params[3];
            String mobile_number = params[4];

            NetworkHelper networkHelper = NetworkClient.newNetworkClient().create(NetworkHelper.class);
            Call<GetResponse> call = networkHelper.approvedAppointment(appointment_id, appointment_date, appointment_time,
                    mobile_number, branchHead);
            call.enqueue(new Callback<GetResponse>() {
                @Override
                public void onResponse(Call<GetResponse> call, Response<GetResponse> response) {
                    if (response.body().getResponse().equalsIgnoreCase("Appointment Confirmation Sent")) {
                        alertDialog.setMessage("Appointment Confirmed");
                        alertDialog.show();
                    } else if (response.body().getResponse().equalsIgnoreCase("Appointment Failed to Change")) {
                        alertDialog.setMessage("Appointment confirmation failed");
                        alertDialog.show();
                    }
                }

                @Override
                public void onFailure(Call<GetResponse> call, Throwable t) {
                    alertDialog.setMessage(context.getResources().getString(R.string.networkResult));
                    alertDialog.show();
                }
            });
        } else if (params[0].equals("add_branch")) {
            String branchName = params[1];
            String addBranchLocation = params[2];
            String addBranchLatitude = params[3];
            String addBranchLongitude = params[4];
            String address = params[5];
            String phone = params[6];
            String userPhone = params[7];

            NetworkHelper networkHelper = NetworkClient.newNetworkClient().create(NetworkHelper.class);
            Call<GetResponse> call = networkHelper.addBranch(branchName, addBranchLocation, addBranchLatitude,
                    addBranchLongitude, address, phone, userPhone, branchHead);
            call.enqueue(new Callback<GetResponse>() {
                @Override
                public void onResponse(Call<GetResponse> call, Response<GetResponse> response) {
                    if (response.body().getResponse().equalsIgnoreCase("Branch Added Successfully")) {
                        alertDialog.setMessage("Branch Added Successfully");
                        alertDialog.show();
                    } else if (response.body().getResponse().equalsIgnoreCase("branch_added_failed")) {
                        alertDialog.setMessage("Branch Added Failed");
                        alertDialog.show();
                    }
                }

                @Override
                public void onFailure(Call<GetResponse> call, Throwable t) {
                    alertDialog.setMessage(context.getResources().getString(R.string.networkResult));
                    alertDialog.show();
                }
            });
        }
    }
}
