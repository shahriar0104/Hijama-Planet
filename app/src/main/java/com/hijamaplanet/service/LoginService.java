package com.hijamaplanet.service;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.hijamaplanet.R;
import com.hijamaplanet.SignupActivity;
import com.hijamaplanet.admin.activity.AdminView;
import com.hijamaplanet.admin.activity.MasterAdminView;
import com.hijamaplanet.service.network.NetworkClient;
import com.hijamaplanet.service.network.NetworkHelper;
import com.hijamaplanet.service.network.model.User;
import com.hijamaplanet.user.UserView;
import com.hijamaplanet.utils.PrefUserInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hijamaplanet.utils.AppConstants.ONE;
import static com.hijamaplanet.utils.AppConstants.THREE;
import static com.hijamaplanet.utils.AppConstants.TWO;
import static com.hijamaplanet.utils.DialogUtils.showProgressDialog;

public class LoginService {

    private Context context;
    private ProgressDialog dialog;
    private PrefUserInfo prefUserInfo;

    public LoginService(Context context) {
        this.context = context;
        prefUserInfo = new PrefUserInfo(context);
    }

    public void execute(final String mobile) {
        dialog = showProgressDialog(context, "Checking Info,please wait...");
        dialog.show();
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");

        NetworkHelper networkHelper = NetworkClient.newNetworkClient().create(NetworkHelper.class);
        Call<List<User>> call = networkHelper.getUserInfo(mobile);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> user = response.body();

                if (user.size() == 0) {
                    if (dialog.isShowing()) dialog.dismiss();
                    context.startActivity(new Intent(context, SignupActivity.class).putExtra("mobile", mobile));
                } else {
                    prefUserInfo.setLoggedIn(true);
                    prefUserInfo.setUserId(user.get(0).getId());
                    prefUserInfo.setUserName(user.get(0).getName());
                    prefUserInfo.setUserMobile(user.get(0).getMobile());
                    prefUserInfo.setUserAge(user.get(0).getAge());
                    prefUserInfo.setUserStatus(user.get(0).getStatus());
                    prefUserInfo.setUserGender(user.get(0).getGender());
                    prefUserInfo.setAdminBranchName(user.get(0).getAdminBranch());
                    prefUserInfo.setReviewStatus(user.get(0).getReviewStatus());

                    if (dialog.isShowing()) dialog.dismiss();
                    if (ONE.equalsIgnoreCase(prefUserInfo.getUserStatus()))
                        context.startActivity(new Intent(context, AdminView.class));
                    else if (TWO.equalsIgnoreCase(prefUserInfo.getUserStatus()))
                        context.startActivity(new Intent(context, UserView.class));
                    else if (THREE.equalsIgnoreCase(prefUserInfo.getUserStatus()))
                        context.startActivity(new Intent(context, MasterAdminView.class));
                    else {
                        alertDialog.setMessage(context.getResources().getString(R.string.networkResult));
                        alertDialog.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                if (dialog.isShowing()) dialog.dismiss();
                alertDialog.setMessage(context.getResources().getString(R.string.networkResult));
                alertDialog.show();
            }
        });
    }
}

