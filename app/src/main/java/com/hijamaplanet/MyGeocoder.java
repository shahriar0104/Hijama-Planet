package com.hijamaplanet;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.hijamaplanet.service.UpdateLocationService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class MyGeocoder extends AsyncTask<Location, Void, String> {

    private final static String GEOCODE_API_ENDPOINT_BASE = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";
    private final static String JSON_PROPERTY_RESULTS = "results";
    private final static String JSON_PROPERTY_FORMATTED_ADDRESS = "formatted_address";
    private final static String JSON_PROPERTY_REQUEST_STATUS = "status";
    private final static String STATUS_OK = "OK";
    public final static String TAG = "RESULT:->";
    private String apiKey;
    Context context;
    String add;

    public MyGeocoder(String apiKey, Context context) {
        this.apiKey = apiKey;
        this.context = context;
    }

    @Override
    protected String doInBackground(Location... params) {

        /*if (apiKey == null) {
            throw new IllegalStateException("Pass in a geocode api key in the ReverseGeoCoder constructor");
        }
*/
        Location location = params[0];
        Locale locale = new Locale("en", "us");
        Geocoder geocoder = new Geocoder(context, locale);

        String googleGeocodeEndpoint = GEOCODE_API_ENDPOINT_BASE + location.getLatitude() + "," + location.getLongitude()
                + " & key=" + apiKey + " & language=en";
        Log.d(TAG, "Requesting gecoding endpoint : " + googleGeocodeEndpoint);
        try {
            URL url = new URL(googleGeocodeEndpoint);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            JSONObject json = new JSONObject(result.toString());
            String requestStatus = json.getString(JSON_PROPERTY_REQUEST_STATUS);
            if (requestStatus.equals(STATUS_OK)) {
                JSONArray results = json.getJSONArray(JSON_PROPERTY_RESULTS);
                if (results.length() > 0) {
                    JSONObject result1 = results.getJSONObject(6);
                    String address = result1.getString(JSON_PROPERTY_FORMATTED_ADDRESS);
                    Log.d(TAG, "First result's address : " + address);

                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    Address obj = addresses.get(0);
                    add = "";//obj.getAddressLine(0);
                    add = add + obj.getLocality();
                    //add = add + obj.getSubAdminArea();
                    add = add + "," + obj.getCountryName();

                    return address;


                } else {
                    Log.d(TAG, "There were no results.");
                }
            } else {
                Log.w(TAG, "Geocode request status not " + STATUS_OK + ", it was " + requestStatus);
                Log.w(TAG, "Did you enable the geocode in the google cloud api console? Is it the right api key?");
            }


        } catch (IOException | JSONException e) {

            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String address) {
        super.onPostExecute(address);
        if (address != null) {
            /*String[] parts = address.split(",");
            String part1 = parts[0];
            String part2 = parts[1];*/

            //if (isAlpha(part1) == true && isAlpha(part2) == true) {
                UpdateLocationService updateLocationStatus = new UpdateLocationService(context);
                updateLocationStatus.execute(add);
            //}
            //Toast.makeText(context, address+"\n"+add, Toast.LENGTH_SHORT).show();
            Log.i("DATA: ", add);

        } else {
            Log.d(TAG, "Did not find an address, UI not being updated");
        }
    }

    public boolean isAlpha(String name) {
        char[] chars = name.toCharArray();
        for (char c : chars) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }
}