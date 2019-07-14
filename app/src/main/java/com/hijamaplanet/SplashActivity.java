package com.hijamaplanet;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;
import com.hijamaplanet.admin.activity.AdminView;
import com.hijamaplanet.admin.activity.MasterAdminView;
import com.hijamaplanet.service.LoginService;
import com.hijamaplanet.user.UserView;
import com.hijamaplanet.utils.PrefUserInfo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //keyHashForFacebook();
        updateAndroidSecurityProvider(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.common_activity_splash);

        PrefUserInfo prefUserInfo = new PrefUserInfo(this);

        new Handler().postDelayed(() -> {
                if (prefUserInfo.getLoggedIn()) {
                    if (!prefUserInfo.getUserStatus().isEmpty()) {
                        if (prefUserInfo.getUserStatus().equalsIgnoreCase("1")) {

                            Intent homeIntent = new Intent(SplashActivity.this, AdminView.class);
                            startActivity(homeIntent);
                            finish();
                        } else if (prefUserInfo.getUserStatus().equalsIgnoreCase("2")) {

                            Intent homeIntent = new Intent(SplashActivity.this, UserView.class);
                            startActivity(homeIntent);
                            finish();
                        } else if (prefUserInfo.getUserStatus().equalsIgnoreCase("3")) {

                            Intent homeIntent = new Intent(SplashActivity.this, MasterAdminView.class);
                            startActivity(homeIntent);
                            finish();
                        }
                    }
                } else {
                    Intent homeIntent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(homeIntent);
                    finish();
                }
        }, SPLASH_TIME_OUT);
    }

    private void updateAndroidSecurityProvider(Activity callingActivity) {
        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (GooglePlayServicesRepairableException e) {
            // Thrown when Google Play Services is not installed, up-to-date, or enabled
            // Show dialog to allow users to install, update, or otherwise enable Google Play services.
            GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), callingActivity, 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e("SecurityException", "Google Play Services not available.");
        }
    }

    void keyHashForFacebook() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.hijama.planet.hijama.ruqyah", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
