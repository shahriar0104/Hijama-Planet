package com.hijamaplanet.drawer.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hijamaplanet.BaseActivity;
import com.hijamaplanet.MainActivity;
import com.hijamaplanet.R;
import com.hijamaplanet.admin.activity.AdminView;
import com.hijamaplanet.admin.activity.MasterAdminView;
import com.hijamaplanet.drawer.DuaActivity;
import com.hijamaplanet.user.UserView;

import static com.hijamaplanet.BaseActivity.drawer;

public class LikeUs extends Fragment {

    private LinearLayout fbPageLayout, fbGroupLayout, hijamaWeb, ruqyahWeb, ruqyahSupport, ruqyahShariyah;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nav_like_us, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("যোগাযোগ");

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.drawer_icon_small);

        fbPageLayout = view.findViewById(R.id.fbPageLayout);
        fbGroupLayout = view.findViewById(R.id.fbGroupLayout);
        hijamaWeb = view.findViewById(R.id.websiteVisit);
        ruqyahWeb = view.findViewById(R.id.ruqyahVisit);
        ruqyahShariyah = view.findViewById(R.id.fbRuqyahShariahPage);
        ruqyahSupport = view.findViewById(R.id.fbRuqyahSupport);

        fbPageLayout.setOnClickListener(v -> {
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
            String facebookUrl = getFacebookPageURL(getActivity());
            facebookIntent.setData(Uri.parse(facebookUrl));
            startActivity(facebookIntent);
        });

        fbGroupLayout.setOnClickListener(v -> {
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
            String facebookUrl = getFacebookGroupURL(getActivity());
            facebookIntent.setData(Uri.parse(facebookUrl));
            startActivity(facebookIntent);
        });

        hijamaWeb.setOnClickListener((View v) -> {
            String uri = "http://hijama.com.bd/";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        });

        ruqyahWeb.setOnClickListener((View v) -> {
            String uri = "https://ruqyahbd.com/";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        });

        ruqyahSupport.setOnClickListener((View v) -> {
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
            String facebookUrl = getRuqyahSupportURL(getActivity());
            facebookIntent.setData(Uri.parse(facebookUrl));
            startActivity(facebookIntent);
        });

        ruqyahShariyah.setOnClickListener((View v) -> {
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
            String facebookUrl = getRuqyahShariayhPageURL(getActivity());
            facebookIntent.setData(Uri.parse(facebookUrl));
            startActivity(facebookIntent);
        });

        return view;
    }

    private String getFacebookPageURL(Context context) {
        final String FACEBOOK_PAGE_ID = "161872823856572";
        final String FACEBOOK_URL = "https://www.facebook.com/bangladesh.hijama.clinic/";

        if (appInstalledOrNot(context, "com.facebook.katana")) {
            try {
                return "fb://page/" + FACEBOOK_PAGE_ID;
            } catch (Exception e) {
            }
        } else {
            return FACEBOOK_URL;
        }
        return null;
    }

    private String getRuqyahShariayhPageURL(Context context) {
        final String FACEBOOK_PAGE_ID = "309498956152249";
        final String FACEBOOK_URL = "https://www.facebook.com/ruqyahbd";

        if (appInstalledOrNot(context, "com.facebook.katana")) {
            try {
                return "fb://page/" + FACEBOOK_PAGE_ID;
            } catch (Exception e) {
            }
        } else {
            return FACEBOOK_URL;
        }
        return null;
    }

    private String getFacebookGroupURL(Context context) {
        final String FACEBOOK_GROUP_ID = "1638725459773520";
        final String FACEBOOK_URL = "https://www.facebook.com/groups/1638725459773520";

        if (appInstalledOrNot(context, "com.facebook.katana")) {
            try {
                return "fb://group/" + FACEBOOK_GROUP_ID;
            } catch (Exception e) {
            }
        } else {
            return FACEBOOK_URL;
        }
        return null;
    }

    private String getRuqyahSupportURL(Context context) {
        final String FACEBOOK_GROUP_ID = "976418825899372";
        final String FACEBOOK_URL = "https://www.facebook.com/groups/ruqyahbd.support/976418825899372";

        if (appInstalledOrNot(context, "com.facebook.katana")) {
            try {
                return "fb://group/" + FACEBOOK_GROUP_ID;
            } catch (Exception e) {
            }
        } else {
            return FACEBOOK_URL;
        }
        return null;
    }


    private static boolean appInstalledOrNot(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            drawer.openDrawer(Gravity.START);
            return true;
        }
        return false;
    }
}
