package com.hijamaplanet.drawer.maps;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hijamaplanet.R;
import com.hijamaplanet.service.network.model.BranchLocation;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ActionBar actionBar;
    private TextView branchName, branchAddress, branchPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.nav_maps_activity);
        actionBar = getSupportActionBar();

        branchName = findViewById(R.id.branchNameTv);
        branchAddress = findViewById(R.id.branchAddressTv);
        branchPhone = findViewById(R.id.branchPhoneTv);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap mMap = googleMap;
        final BranchLocation branchLocation = getIntent().getParcelableExtra("branch");
        LatLng coOrdinate;

        try {
            coOrdinate = new LatLng(Double.parseDouble(branchLocation.getLatitude()), Double.parseDouble(branchLocation.getLongitude()));
            Marker marker = mMap.addMarker(new MarkerOptions().position(coOrdinate).title(branchLocation.getBranch_location()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(coOrdinate));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coOrdinate, 14));
            marker.showInfoWindow();
            actionBar.setTitle(branchLocation.getBranch_name() + " Branch");
            branchName.setText(branchLocation.getBranch_location());
            branchAddress.setText(branchLocation.getAddress());
            branchPhone.setText(branchLocation.getPhone());

            findViewById(R.id.googleMaps).setOnClickListener((View v) -> {
                String maplLabel = branchLocation.getBranch_location();
                Double lat = Double.parseDouble(branchLocation.getLatitude());
                Double lon = Double.parseDouble(branchLocation.getLongitude());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "geo:" + lat + "," + lon + "?q=" + Uri.encode(lat + "," + lon + "(" + maplLabel + ")")));
                if (intent.resolveActivity(getPackageManager()) != null) startActivity(intent);
            });

            mMap.setOnMarkerClickListener((Marker mMarker) -> {
                mMarker.showInfoWindow();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(mMarker.getPosition()));
                return true;
            });

        } catch (Exception e) {
            branchName.setText("No Branch Name Available");
            branchAddress.setText("No Address Available");
            branchPhone.setText("Mobile Number not available");
            Toast.makeText(this, "Value not correct", Toast.LENGTH_SHORT).show();
        }
    }
}
