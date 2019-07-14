package com.hijamaplanet;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hijamaplanet.location.LocationClient;
import com.hijamaplanet.service.SignUpService;
import com.hijamaplanet.utils.AppConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.hijamaplanet.utils.AppConstants.FILL_FORM;
import static com.hijamaplanet.utils.AppConstants.NO_INTERNET_TOAST;
import static com.hijamaplanet.utils.NetworkUtil.haveNetworkConnection;

public class SignupActivity extends AppCompatActivity {

    private Spinner spinner;
    EditText ET_NAME, ET_AGE;
    String name, user_age, gender, device;
    public static String location;
    LocationClient locationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.common_activity_signup);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //mProgressBar = findViewById(R.id.progress_bar);
        locationClient = new LocationClient(this, SignupActivity.this);

        FontClass fontClass = new FontClass(this);
        Typeface custom_font3 = fontClass.getRegularFont();

        ET_NAME = findViewById(R.id.nameEdit);
        ET_AGE = findViewById(R.id.ageEdit);
        ET_NAME.setTypeface(custom_font3);
        ET_AGE.setTypeface(custom_font3);

        spinner = findViewById(R.id.spinner);

        List<String> genderList = new ArrayList<>();
        genderList.add("Select Gender");
        genderList.add("Male");
        genderList.add("Female");
        genderList.add("Others");

        spinner = new Spinner(this);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> genAdapter =
                new ArrayAdapter<String>(getBaseContext(), R.layout.z_spinner_item, genderList) {
                    @Override
                    public boolean isEnabled(int position) {
                        if (position == 0) {
                            return false;
                        } else {
                            return true;
                        }
                    }

                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        if (position == 0) {
                            tv.setTextColor(Color.GRAY);
                        } else {
                            tv.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };
        genAdapter.setDropDownViewResource(R.layout.z_spinner_item);
        spinner.setAdapter(genAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    String mobile_number;

    public void userReg(View view) {

        Intent intent = getIntent();
        mobile_number = intent.getStringExtra("mobile");
        name = ET_NAME.getText().toString();
        String NAME = "";
        try {
            name = name.replaceAll("\\s{2,}", " ").trim();
            for (String word : name.split(" ")) {
                NAME += Character.toUpperCase(word.charAt(0)) + word.substring(1) + " ";
            }
        } catch (Exception e) {
            NAME = name;
        }

        String key = activityStatus();
        user_age = ET_AGE.getText().toString();
        device = Build.BRAND + " " + Build.MODEL;

        if (gender.equalsIgnoreCase("Select Gender"))
            Toast.makeText(this, "Select your Gender", Toast.LENGTH_SHORT).show();
        else {
            if (!(name.isEmpty() || mobile_number.isEmpty() || user_age.isEmpty() || gender.isEmpty())) {
                if (haveNetworkConnection(this)) {
                    SignUpService signUpService = new SignUpService(this);
                    signUpService.execute(NAME.trim(), mobile_number, user_age, gender, device, location, key);
                } else
                    Toast.makeText(this, NO_INTERNET_TOAST, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, FILL_FORM, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        locationClient.activityResult(requestCode, resultCode, data);
    }

    private String activityStatus() {
        String symbols =
                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789$&@?<>~!%#()[]{}/=+^*|";
        Random r = new Random();
        while (true) {
            char[] genre = new char[r.nextBoolean() ? 49 : 50];
            boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;
            for (int i = 0; i < genre.length; i++) {
                char ch = symbols.charAt(r.nextInt(symbols.length()));
                if (Character.isUpperCase(ch))
                    hasUpper = true;
                else if (Character.isLowerCase(ch))
                    hasLower = true;
                else if (Character.isDigit(ch))
                    hasDigit = true;
                else
                    hasSpecial = true;
                genre[i] = ch;
            }
            if (hasUpper && hasLower && hasDigit && hasSpecial) {
                return new String(genre);
            }
        }
    }
}
