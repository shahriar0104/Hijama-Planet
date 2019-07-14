package com.hijamaplanet.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.hijamaplanet.BuildConfig;
import com.hijamaplanet.MyGeocoder;
import com.hijamaplanet.R;
import com.hijamaplanet.SignupActivity;
import com.hijamaplanet.service.UpdateLocationService;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LocationClient extends Activity {

    private Context context;
    private Activity activity;
    //private ProgressBar mProgressBar;
    private static String TAG = "";
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    boolean subAdminCheck,localityCheck,countryNameCheck;

    private Boolean mRequestingLocationUpdates;
    private String mLastUpdateTime;

    public LocationClient(Context context, Activity activity ) {
        this.context = context;
        this.activity = activity;
        //this.mProgressBar = mProgressBar;
        TAG = activity.getClass().getSimpleName();

        //mProgressBar.setVisibility(ProgressBar.GONE);

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mSettingsClient = LocationServices.getSettingsClient(context);

        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
        startUpdatesButtonHandler();
    }

    public void startUpdatesButtonHandler() {
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
            startLocationUpdates();
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Creates a callback for receiving location events.
     */
    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateLocationUI();
            }
        };
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    @SuppressLint("MissingPermission")
    public void activityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        //mProgressBar.setVisibility(ProgressBar.VISIBLE);
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateUI();
                        break;
                    case Activity.RESULT_CANCELED:
                        startLocationUpdates();
                        /*mRequestingLocationUpdates = false;
                        updateUI();*/
                        break;
                }
                break;
        }
    }


    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        //mProgressBar.setVisibility(ProgressBar.VISIBLE);
                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateUI();
                    }
                })
                .addOnFailureListener(activity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "LocationData settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "LocationData settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);
                                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                                mRequestingLocationUpdates = false;
                        }

                        updateUI();
                    }
                });
    }

    /**
     * Updates all UI fields.
     */
    private void updateUI() {
        updateLocationUI();
    }

    /**
     * Sets the value of the UI fields for the location latitude, longitude and last update time.
     */
    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            getAddress(mCurrentLocation,mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude());
        }
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private void stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            Log.d(TAG, "stopLocationUpdates: updates never requested, no-op.");
            return;
        }
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mRequestingLocationUpdates = false;
                    }
                });
    }

    public void getAddress(Location location , double lat, double lng) {
        Locale locale = new Locale("en", "us");
        Geocoder geocoder = new Geocoder(context, locale);
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = "";//obj.getAddressLine(0);

            //String adminArea = add + obj.getAdminArea();
            String subAdminArea = add + obj.getSubAdminArea();
            String locality = add + obj.getLocality();
            String countryName = add + obj.getCountryName();
            String postalCode = add + obj.getPostalCode();
            //Toast.makeText(context, subAdminArea+locality+postalCode+countryName, Toast.LENGTH_SHORT).show();

            /*add = add + obj.getLocality();
            add = add + "," + obj.getCountryName();
            Toast.makeText(context, "" + add, Toast.LENGTH_LONG).show();*/

            if (obj.getSubAdminArea() != null){
                if (subAdminArea.indexOf(" ") > 0)
                    subAdminCheck = isAlpha(subAdminArea.substring(0,subAdminArea.indexOf(" ")));
                else{
                    subAdminCheck = isAlpha(subAdminArea);
                }
            }
            else
                subAdminCheck = true;

            if (obj.getLocality() != null){
                if (locality.indexOf(" ") > 0)
                    localityCheck = isAlpha(locality.substring(0,locality.indexOf(" ")));
                else{
                    localityCheck = isAlpha(locality);
                }
            } else
                localityCheck = true;

            if (obj.getCountryName() != null){
                if (countryName.indexOf(" ") > 0)
                    countryNameCheck = isAlpha(countryName.substring(0,countryName.indexOf(" ")));
                else{
                    countryNameCheck = isAlpha(countryName);
                }
            } else
                countryNameCheck = true;

            //mProgressBar.setVisibility(ProgressBar.GONE);

            if (subAdminCheck && localityCheck){
                if (TAG.startsWith("Sign")){
                    if (subAdminArea.equalsIgnoreCase("Dhaka District") ||
                            locality.equalsIgnoreCase("Dhaka")){
                        String retLocation;

                        if (obj.getPostalCode() == null)
                            retLocation = "UnKnown";
                        else {
                            LocationAddress locationAddress = new LocationAddress(postalCode);
                            retLocation = locationAddress.getPostalAddress();
                        }

                        if (retLocation.equalsIgnoreCase("UnKnown"))
                            SignupActivity.location = "Unknown";
                        else
                            SignupActivity.location = retLocation;
                    } else {
                        if (obj.getSubAdminArea() != null){
                            if (subAdminArea.indexOf(" ") > 0){
                                String lastWord = subAdminArea.substring(subAdminArea.lastIndexOf(" ")+1);
                                if (lastWord.equalsIgnoreCase("District")){
                                    subAdminArea = subAdminArea.substring(0, subAdminArea.lastIndexOf(" "));
                                    SignupActivity.location = subAdminArea;
                                } else
                                    SignupActivity.location = subAdminArea;
                            } else
                                SignupActivity.location = subAdminArea;
                        } else if (obj.getLocality() != null){
                            SignupActivity.location = locality;
                        } else
                            SignupActivity.location = "Unknown";
                    }
                } else if (TAG.startsWith("Main")){
                    if (countryNameCheck){
                        try {
                            if (subAdminArea.equalsIgnoreCase("Dhaka District") ||
                                    locality.equalsIgnoreCase("Dhaka")) {

                                String retLocation;

                                if (obj.getPostalCode() == null)
                                    retLocation = "UnKnown";
                                else {
                                    LocationAddress locationAddress = new LocationAddress(postalCode);
                                    retLocation = locationAddress.getPostalAddress();
                                }

                                if (retLocation.equalsIgnoreCase("UnKnown")){
                                    UpdateLocationService updateLocationStatus = new UpdateLocationService(context);
                                    updateLocationStatus.execute("Unknown,Unknown");
                                }
                                else{
                                    UpdateLocationService updateLocationStatus = new UpdateLocationService(context);
                                    updateLocationStatus.execute(retLocation+","+countryName);
                                }
                            } else {
                                if (obj.getSubAdminArea() != null){
                                    if (subAdminArea.indexOf(" ") > 0){
                                        String lastWord = subAdminArea.substring(subAdminArea.lastIndexOf(" ")+1);
                                        if (lastWord.equalsIgnoreCase("District")){
                                            subAdminArea = subAdminArea.substring(0, subAdminArea.lastIndexOf(" "));
                                            UpdateLocationService updateLocationStatus = new UpdateLocationService(context);
                                            updateLocationStatus.execute(subAdminArea+","+countryName);
                                        } else{
                                            UpdateLocationService updateLocationStatus = new UpdateLocationService(context);
                                            updateLocationStatus.execute(subAdminArea+","+countryName);
                                        }
                                    } else{
                                        UpdateLocationService updateLocationStatus = new UpdateLocationService(context);
                                        updateLocationStatus.execute(subAdminArea+","+countryName);
                                    }
                                } else if (obj.getLocality() != null){
                                    UpdateLocationService updateLocationStatus = new UpdateLocationService(context);
                                    updateLocationStatus.execute(locality+","+countryName);
                                } else {
                                    UpdateLocationService updateLocationStatus = new UpdateLocationService(context);
                                    updateLocationStatus.execute("Unknown,Unknown");
                                }
                            }
                        } catch (Exception e){
                            MyGeocoder myGeocoder = new MyGeocoder(getResources().getString(R.string.google_maps_key), getApplicationContext());
                            myGeocoder.execute(location);
                            //e.printStackTrace();
                        }
                    } else {
                        UpdateLocationService updateLocationStatus = new UpdateLocationService(context);
                        updateLocationStatus.execute("Unknown,Unknown");
                    }
                }
            } else {
                if (TAG.startsWith("Sign"))
                    SignupActivity.location = "Unknown";
                else if (TAG.startsWith("Main")){
                    UpdateLocationService updateLocationStatus = new UpdateLocationService(context);
                    updateLocationStatus.execute("Unknown,Unknown");
                }
            }

            stopLocationUpdates();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isAlpha(String alphaString){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            boolean allLetters = alphaString.chars().allMatch(Character::isLetter);
            return allLetters;
        } else {
            char[] chars = alphaString.toCharArray();
            for (char c : chars) {
                if (!Character.isLetter(c)) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        } else if (!checkPermissions()) {
            requestPermissions();
        }

        updateUI();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    /**
     * Stores activity data in the Bundle.
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                context.getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(context.getString(actionStringId), listener).show();
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mRequestingLocationUpdates) {
                    Log.i(TAG, "Permission granted, updates requested, starting location updates");
                    startLocationUpdates();
                }
            } else {
                showSnackbar(R.string.permission_denied_explanation, R.string.settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Build intent that displays the App settings screen.
                        Intent intent = new Intent();
                        intent.setAction(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",
                                BuildConfig.APPLICATION_ID, null);
                        intent.setData(uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
            }
        }
    }
}
