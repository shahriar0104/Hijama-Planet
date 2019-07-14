package com.hijamaplanet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.hijamaplanet.location.LocationClient;
import com.hijamaplanet.service.LoginService;

import pub.devrel.easypermissions.AppSettingsDialog;

import static com.hijamaplanet.utils.NetworkUtil.haveNetworkConnection;

public class MainActivity extends BaseActivity {

    private Snackbar snackbar;
    private final int APP_REQUEST_CODE = 57900;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.common_activity_main, null, false);
        drawer.addView(contentView,1);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        duaDownloadResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) checkPermission();
        else {
            if (!locationSend) {
                if (haveNetworkConnection(this)) locationClient = new LocationClient(this, MainActivity.this);
                else showRefreshNetDialog();
            }
        }
        //for ssl
        try {
            ProviderInstaller.installIfNeeded(getApplicationContext());
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void OnLogin(View view) {
        if (!haveNetworkConnection(this)) {
            RelativeLayout content_nav = findViewById(R.id.content_nav);
            snackbar = Snackbar.make(content_nav, "No internet connection!", Snackbar.LENGTH_SHORT)
                    .setAction("Close",(View v) -> snackbar.dismiss());
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.snackbar));
            snackbar.show();
        } else {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                checkPermission();
            } else loggedIn();
        }
    }

    private void loggedIn() {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN);
        //configurationBuilder.setReceiveSMS(true);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {}
        if (locationClient != null) locationClient.activityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) {
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (loginResult.getError() != null)
                Toast.makeText(getApplicationContext(), ""+loginResult.getError().getErrorType().getMessage(), Toast.LENGTH_SHORT).show();
             else if (loginResult.wasCancelled())
                Toast.makeText(getApplicationContext(), "Login Cancelled", Toast.LENGTH_SHORT).show();
             else {
                if (loginResult.getAccessToken() != null) {
                        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                        @Override
                        public void onSuccess(final Account account) {
                            PhoneNumber phoneNumber = account.getPhoneNumber();
                            if (phoneNumber != null) {
                                LoginService loginService = new LoginService(MainActivity.this);
                                loginService.execute(phoneNumber.toString());
                            }
                        }

                        @Override
                        public void onError(final AccountKitError error) {
                            Toast.makeText(MainActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    }
}
