package com.hijamaplanet.service;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hijamaplanet.R;
import com.hijamaplanet.service.network.NetworkClient;
import com.hijamaplanet.service.network.NetworkHelper;
import com.hijamaplanet.service.network.model.GetResponse;
import com.hijamaplanet.utils.DialogUtils;
import com.hijamaplanet.utils.PrefUserInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hijamaplanet.utils.AppConstants.PLEASE_WAIT;
import static com.hijamaplanet.utils.AppConstants.ONE;
import static com.hijamaplanet.utils.DialogUtils.showDialogTrans;

public class UserService {

    Context context;

    public UserService(Context context) {
        this.context = context;
    }

    public void execute(String... params) {
        PrefUserInfo prefUserInfo = new PrefUserInfo(context);
        String userMobile = prefUserInfo.getUserMobile();
        String branchHead = prefUserInfo.getAdminBranchName();

        ProgressDialog progressDialog = DialogUtils.showProgressDialog(context, PLEASE_WAIT);
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        if (params[0].equals("make_appointment")) {
            alertDialog.setTitle("Appointment");
            progressDialog.show();

            String name = params[1];
            String age = params[2];
            String mobile_number = params[3];
            String sex = params[4];
            String date = params[5];
            String time = params[6];
            String treatment_method = params[7];
            String branch = params[8];
            String problem = params[9];

            NetworkHelper networkHelper = NetworkClient.newNetworkClient().create(NetworkHelper.class);
            Call<GetResponse> call = networkHelper.makeAppointment(name, age, mobile_number, sex, date, time, treatment_method, branch, branchHead, problem);
            call.enqueue(new Callback<GetResponse>() {
                @Override
                public void onResponse(Call<GetResponse> call, Response<GetResponse> response) {
                    GetResponse getResponses = response.body();
                    if (getResponses.getResponse().equalsIgnoreCase("appointment_successfully_created")) {
                        progressDialog.dismiss();
                        Dialog dialogConfirmation = showDialogTrans(context, R.layout.user_appointment_confirmation_dialog, false, false);
                        dialogConfirmation.show();

                        TextView dateForAppointment = dialogConfirmation.findViewById(R.id.dateForAppointnment);
                        TextView textForAppointment = dialogConfirmation.findViewById(R.id.textForAppointnment);
                        Button okButton = dialogConfirmation.findViewById(R.id.okButton);

                        dateFormatterCustom(date, dateForAppointment, textForAppointment);
                        okButton.setOnClickListener((View v) -> dialogConfirmation.dismiss());
                    } else {
                        progressDialog.dismiss();
                        alertDialog.setMessage("appointment creation failed");
                        alertDialog.show();
                    }
                }

                @Override
                public void onFailure(Call<GetResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    alertDialog.setMessage(context.getResources().getString(R.string.networkResult));
                    alertDialog.show();
                }
            });
        } else if (params[0].equals("review")) {
            alertDialog.setTitle("Review");
            progressDialog.show();

            String reviewer_name = params[1];
            String review_star = params[2];
            String review_post = params[3];

            NetworkHelper networkHelper = NetworkClient.newNetworkClient().create(NetworkHelper.class);
            Call<GetResponse> call = networkHelper.reviewPost(reviewer_name, review_star, review_post, userMobile, branchHead);
            call.enqueue(new Callback<GetResponse>() {
                @Override
                public void onResponse(Call<GetResponse> call, Response<GetResponse> response) {
                    GetResponse getResponses = response.body();
                    if (getResponses.getResponse().equalsIgnoreCase("review_posted_successfully")) {
                        progressDialog.dismiss();
                        prefUserInfo.setReviewStatus(ONE);
                        alertDialog.setMessage("review posted successfully");
                        alertDialog.show();
                    } else {
                        progressDialog.dismiss();
                        alertDialog.setMessage("review failed to post");
                        alertDialog.show();
                    }
                }

                @Override
                public void onFailure(Call<GetResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    alertDialog.setMessage(context.getResources().getString(R.string.networkResult));
                    alertDialog.show();
                }
            });
        }
    }

    private void dateFormatterCustom(String date, TextView dateForAppointment, TextView textForAppointment) {
        String DATE = date.trim();
        String[] parts = DATE.split("/");
        if (parts[1].equalsIgnoreCase("1")) {
            dateForAppointment.setText(parts[0]);
            textForAppointment.setText("JAN," + parts[2]);
        } else if (parts[1].equalsIgnoreCase("2")) {
            dateForAppointment.setText(parts[0]);
            textForAppointment.setText("FEB," + parts[2]);
        } else if (parts[1].equalsIgnoreCase("3")) {
            dateForAppointment.setText(parts[0]);
            textForAppointment.setText("MAR," + parts[2]);
        } else if (parts[1].equalsIgnoreCase("4")) {
            dateForAppointment.setText(parts[0]);
            textForAppointment.setText("APR," + parts[2]);
        } else if (parts[1].equalsIgnoreCase("5")) {
            dateForAppointment.setText(parts[0]);
            textForAppointment.setText("MAY," + parts[2]);
        } else if (parts[1].equalsIgnoreCase("6")) {
            dateForAppointment.setText(parts[0]);
            textForAppointment.setText("JUN," + parts[2]);
        } else if (parts[1].equalsIgnoreCase("7")) {
            dateForAppointment.setText(parts[0]);
            textForAppointment.setText("JUL," + parts[2]);
        } else if (parts[1].equalsIgnoreCase("8")) {
            dateForAppointment.setText(parts[0]);
            textForAppointment.setText("AUG," + parts[2]);
        } else if (parts[1].equalsIgnoreCase("9")) {
            dateForAppointment.setText(parts[0]);
            textForAppointment.setText("SEP," + parts[2]);
        } else if (parts[1].equalsIgnoreCase("10")) {
            dateForAppointment.setText(parts[0]);
            textForAppointment.setText("OCT," + parts[2]);
        } else if (parts[1].equalsIgnoreCase("11")) {
            dateForAppointment.setText(parts[0]);
            textForAppointment.setText("NOV," + parts[2]);
        } else if (parts[1].equalsIgnoreCase("12")) {
            dateForAppointment.setText(parts[0]);
            textForAppointment.setText("DEC," + parts[2]);
        }
    }
}
