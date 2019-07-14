package com.hijamaplanet;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hijamaplanet.admin.activity.AdminView;
import com.hijamaplanet.admin.activity.MasterAdminView;
import com.hijamaplanet.drawer.DuaActivity;
import com.hijamaplanet.drawer.fragment.BaseFragment;
import com.hijamaplanet.drawer.fragment.Hijama;
import com.hijamaplanet.drawer.fragment.LikeUs;
import com.hijamaplanet.drawer.fragment.TestFragment;
import com.hijamaplanet.location.LocationClient;
import com.hijamaplanet.service.parser.FetchBranchLocation;
import com.hijamaplanet.service.parser.FetchDua;
import com.hijamaplanet.service.parser.FetchReview;
import com.hijamaplanet.service.parser.FetchOffer;
import com.hijamaplanet.service.parser.FetchShopItem;
import com.hijamaplanet.service.parser.FetchTreatmentPrice;
import com.hijamaplanet.user.UserView;
import com.hijamaplanet.utils.DialogUtils;
import com.hijamaplanet.utils.PrefUserInfo;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.hijamaplanet.utils.AppConstants.ASHFIA;
import static com.hijamaplanet.utils.AppConstants.MASNUN_AMOL;
import static com.hijamaplanet.utils.AppConstants.NO_INTERNET_TOAST;
import static com.hijamaplanet.utils.AppConstants.RUQYAH_SHARIYAH;
import static com.hijamaplanet.utils.NetworkUtil.haveNetworkConnection;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        EasyPermissions.PermissionCallbacks {

    public static DrawerLayout drawer;
    protected FontClass fontClass;
    private final int REQUEST_LOCATION_CODE = 99;
    protected boolean locationSend;
    protected final String NETWORK_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    protected LocationClient locationClient;
    protected PrefUserInfo prefUserInfo;
    protected AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);

        prefUserInfo = new PrefUserInfo(this);
        locationSend = prefUserInfo.getLocationSend();
        fontClass = new FontClass(this);
        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        navigationView.getMenu().getItem(8).setActionView(R.layout.drawer_offer_image);

        FrameLayout drawerIcon = findViewById(R.id.drawerIcon);
        drawerIcon.setOnClickListener((View view) -> {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) checkPermission();
            drawer.openDrawer(Gravity.START);
        });
    }

    protected Typeface getRegularFont() {
        return fontClass.getRegularFont();
    }

    protected Typeface getBoldFont() {
        return fontClass.getBoldFont();
    }

    protected Typeface getUbuntuFont() {
        return fontClass.getUbuntuFont();
    }

    public void showRefreshNetDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.network_dialog, null);
        mBuilder.setView(mView).setCancelable(false)
                .setPositiveButton("REFRESH", (DialogInterface dialog, int which) -> registerReceiver(networkReceiver, new IntentFilter(NETWORK_ACTION)));
        dialog = mBuilder.create();
        dialog.show();
    }

    public Runnable sendUpdatesToUI = () -> showRefreshNetDialog();

    public BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().matches(NETWORK_ACTION)) {
                if (haveNetworkConnection(getApplicationContext())) {
                    dialog.dismiss();
                    unregisterReceiver(networkReceiver);
                    locationClient = new LocationClient(getApplicationContext(), BaseActivity.this);
                } else {
                    unregisterReceiver(networkReceiver);
                    new Handler().postDelayed(sendUpdatesToUI, 150);
                }
            }
        }
    };

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void openFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_nav, fragment).addToBackStack(null).commit();
    }

    protected void openFragmentWithBundle(Fragment fragment,String fragmentName) {
        Bundle bundle = new Bundle();
        bundle.putString("fragmentName",fragmentName);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_nav, fragment).addToBackStack(null).commit();
    }

    protected void removeFragmentBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
        }
    }

    protected void duaDownloadResume() {
        String menuFragment = getIntent().getStringExtra("duaList");
        if (menuFragment != null) {
            if (menuFragment.equals("downloadedDua")) {
                removeFragmentBackStack();
                if (haveNetworkConnection(this)) new FetchDua(this).execute();
                else startActivity(new Intent(this, DuaActivity.class));
            }
        }
    }

    @AfterPermissionGranted(REQUEST_LOCATION_CODE)
    protected void checkPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            if (!locationSend) {
                if (haveNetworkConnection(this)) locationClient = new LocationClient(this, this);
                else showRefreshNetDialog();
            }
        } else
            EasyPermissions.requestPermissions(this, "We need your location permission for the purpose of providing " +
                            "better services from Hijama Planet to you and to use all the features of our app.",
                    REQUEST_LOCATION_CODE, perms);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms))
            new AppSettingsDialog.Builder(this).build().show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            String activityName = getClass().getSimpleName();
            if (activityName.startsWith("Main"))
                startActivity(new Intent(this, MainActivity.class));
            else if (activityName.startsWith("User"))
                startActivity(new Intent(this, UserView.class));
            else if (activityName.startsWith("Admin"))
                startActivity(new Intent(this, AdminView.class));
            else if (activityName.startsWith("Master"))
                startActivity(new Intent(this, MasterAdminView.class));
            finish();

        } else if (id == R.id.nav_hijama) {
            removeFragmentBackStack();
            openFragment(new Hijama());

        } else if (id == R.id.nav_dua) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                checkPermission();
            } else {
                if (!haveNetworkConnection(this)) startActivity(new Intent(this, DuaActivity.class));
                else new FetchDua(this).execute();
                removeFragmentBackStack();
            }

        } else if (id == R.id.nav_ruqyahshariah) {
            removeFragmentBackStack();
            openFragmentWithBundle(new BaseFragment(),RUQYAH_SHARIYAH);

        } else if (id == R.id.nav_masnun) {
            removeFragmentBackStack();
            openFragmentWithBundle(new BaseFragment(),MASNUN_AMOL);

        } else if (id == R.id.nav_ashfia) {
            removeFragmentBackStack();
            openFragmentWithBundle(new BaseFragment(),ASHFIA);

        } else if (id == R.id.nav_ruqyahTest) {
            removeFragmentBackStack();
            openFragment(new TestFragment());

        } else if (id == R.id.nav_shop) {
            if (!haveNetworkConnection(this)) toast(NO_INTERNET_TOAST);
            else {
                new FetchShopItem(this).execute();
                removeFragmentBackStack();
            }

        } else if (id == R.id.nav_price) {
            if (!haveNetworkConnection(this)) toast(NO_INTERNET_TOAST);
            else {
                FetchTreatmentPrice fetchTreatmentPrice = new FetchTreatmentPrice(this);
                fetchTreatmentPrice.execute();
                removeFragmentBackStack();
            }

        } else if (id == R.id.nav_offer) {
            if (!haveNetworkConnection(this)) toast(NO_INTERNET_TOAST);
            else {
                FetchOffer fetchOffer = new FetchOffer(this, "");
                fetchOffer.execute();
                removeFragmentBackStack();
            }
        } else if (id == R.id.nav_review) {
            if (!haveNetworkConnection(this)) toast(NO_INTERNET_TOAST);
            else {
                FetchReview fetchReview = new FetchReview(this, "");
                fetchReview.execute();
                removeFragmentBackStack();
            }

        } else if (id == R.id.nav_map) {
            if (!haveNetworkConnection(this)) toast(NO_INTERNET_TOAST);
            else {
                FetchBranchLocation fetchBranchLocation = new FetchBranchLocation(this);
                fetchBranchLocation.execute();
                removeFragmentBackStack();
            }
        } else if (id == R.id.nav_like) {
            removeFragmentBackStack();
            openFragment(new LikeUs());

        } else if (id == R.id.nav_about) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            View mView = getLayoutInflater().inflate(R.layout.nav_dialog_about, null);
            TextView sparentech = mView.findViewById(R.id.sparentech);
            sparentech.setMovementMethod(LinkMovementMethod.getInstance());
            mBuilder.setView(mView);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) return false;
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) drawer.closeDrawer(GravityCompat.START);
        else {
            if (getSupportFragmentManager().getBackStackEntryCount() >= 1)
                getSupportFragmentManager().popBackStack();
            else if (getClass().getSimpleName().startsWith("Dua")) {
                finish();
                super.onBackPressed();
            }
            else DialogUtils.showExitDialog(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        drawer = findViewById(R.id.drawer_layout);
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) checkPermission();
        duaDownloadResume();
    }
}
