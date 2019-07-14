package com.hijamaplanet.user;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hijamaplanet.BaseActivity;
import com.hijamaplanet.R;
import com.hijamaplanet.service.network.NetworkCallback;
import com.hijamaplanet.location.LocationClient;
import com.hijamaplanet.service.UserService;
import com.hijamaplanet.service.parser.FetchBranchList;
import com.hijamaplanet.service.parser.FetchUserAppointment;
import com.hijamaplanet.utils.DialogUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.hijamaplanet.utils.AppConstants.FILL_FORM;
import static com.hijamaplanet.utils.AppConstants.NO_INTERNET_TOAST;
import static com.hijamaplanet.utils.AppConstants.PREF_KEY;
import static com.hijamaplanet.utils.AppConstants.PROVIDE_CONNECTION;
import static com.hijamaplanet.utils.AppConstants.ONE;
import static com.hijamaplanet.utils.DialogUtils.showAlertBuilder;
import static com.hijamaplanet.utils.DialogUtils.showDialogTrans;
import static com.hijamaplanet.utils.NetworkUtil.haveNetworkConnection;
import static com.hijamaplanet.utils.SpinnerUtil.showSpinner;

public class UserView extends BaseActivity implements View.OnClickListener, NetworkCallback {

    private CardView makeAppointmentCv, allAppointmentCv, reviewCv;
    private TextView user_profile,mobileText, ageText, genderText;
    private List<String> branchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.user_activity_user_view, null, false);
        drawer.addView(contentView,1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) checkPermission();
        duaDownloadResume();

        locationUpdateMonthly();
        viewInit();
        viewImplementation();
    }

    private void viewInit() {
        user_profile = findViewById(R.id.profileNameTv);
        makeAppointmentCv = findViewById(R.id.makeAppointmentCv);
        allAppointmentCv = findViewById(R.id.allAppointmentCv);
        reviewCv = findViewById(R.id.reviewCv);
        mobileText = findViewById(R.id.mobileTv);
        ageText = findViewById(R.id.ageTv);
        genderText = findViewById(R.id.genderTv);
    }

    private void viewImplementation() {
        mobileText.setTypeface(getBoldFont());
        ageText.setTypeface(getBoldFont());
        genderText.setTypeface(getBoldFont());
        user_profile.setTypeface(getBoldFont());

        user_profile.setText(prefUserInfo.getUserName());
        mobileText.setText("● " + prefUserInfo.getUserMobile());
        ageText.setText("● " + prefUserInfo.getUserAge() + " yrs");
        genderText.setText("● " + prefUserInfo.getUserGender());

        makeAppointmentCv.setOnClickListener(this);
        allAppointmentCv.setOnClickListener(this);
        reviewCv.setOnClickListener(this);
    }

    private void locationUpdateMonthly() {
        //this is for location check in 30 days
        SharedPreferences sharedPreferences = this.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        final String date = sharedPreferences.getString("todaysDate", "");
        if (!date.isEmpty()) {
            Date dat = new Date();
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = df.format(dat);

            String currentDateArr[] = currentDate.split("/");
            String previousDateArr[] = date.split("/");

            if ((Integer.parseInt(currentDateArr[2]) != Integer.parseInt(previousDateArr[2])) ||
                    (Integer.parseInt(currentDateArr[1]) > Integer.parseInt(previousDateArr[1]))) {
                editor.putString("todaysDate", currentDate);
                editor.apply();
                checkPermission();
                new LocationClient(this, this);
            }
        }
    }

    private void createAppointment(boolean guestUserCheck) {
        final AlertDialog messageDialogue = new AlertDialog.Builder(UserView.this).create();
        Dialog appointmentDialog = showDialogTrans(this,R.layout.user_make_appointment,false,false);

        List<String> genderList = new ArrayList<>();
        genderList.add("Male");
        genderList.add("Female");

        List<String> treatMethodList = new ArrayList<>();
        treatMethodList.add("Hijama");
        treatMethodList.add("Ruqyah");

        //init
        Spinner genderSn = appointmentDialog.findViewById(R.id.genderSn);
        Spinner treatMethodSn = appointmentDialog.findViewById(R.id.treatMethodSn);
        Spinner BranchSpinner = appointmentDialog.findViewById(R.id.branchNameTv);
        EditText patientNameEt = appointmentDialog.findViewById(R.id.namePatient);
        EditText patientAgeEt = appointmentDialog.findViewById(R.id.agePatient);
        EditText patientPrbEt = appointmentDialog.findViewById(R.id.problemPatient);
        TextView patientMobileTv = appointmentDialog.findViewById(R.id.mobilePatient);
        TextView dateSet = appointmentDialog.findViewById(R.id.dateSet);
        TextView timeSet = appointmentDialog.findViewById(R.id.timeSet);
        Button makeAppointment = appointmentDialog.findViewById(R.id.date_time_set);
        Button cancelAppointment = appointmentDialog.findViewById(R.id.cancelAppointment);

        showSpinner(this, genderSn, genderList);
        showSpinner(this, treatMethodSn, treatMethodList);
        showSpinner(this, BranchSpinner, branchList);
        patientNameEt.setTypeface(getRegularFont());
        patientMobileTv.setText(prefUserInfo.getUserMobile());

        if (!guestUserCheck) {
            patientNameEt.setText(prefUserInfo.getUserName());
            patientAgeEt.setText(prefUserInfo.getUserAge());
            genderSn.setBackground(null);
            genderSn.setEnabled(false);
        } else {
            patientNameEt.setFocusable(true);
            patientNameEt.setFocusableInTouchMode(true);
            patientAgeEt.setFocusable(true);
            patientAgeEt.setFocusableInTouchMode(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            makeAppointment.setShadowLayer(50, 15, 15, R.color.indigo);
            cancelAppointment.setShadowLayer(50, 15, 15, R.color.indigo);
        }

        dateSet.setTypeface(getRegularFont());
        timeSet.setTypeface(getRegularFont());
        dateSet.setOnClickListener((View v) -> DialogUtils.showDateDialog(this,dateSet));
        timeSet.setOnClickListener((View v) -> DialogUtils.showTimeDialog(this,timeSet));

        makeAppointment.setOnClickListener((View view) -> {
            String name = patientNameEt.getText().toString().trim();
            String age = patientAgeEt.getText().toString().trim();
            String mobileNumber = patientMobileTv.getText().toString().trim();
            String problem = patientPrbEt.getText().toString().trim();
            String date = dateSet.getText().toString().trim();
            String currentTime = timeSet.getText().toString().trim();
            String gender = genderSn.getSelectedItem().toString().trim();
            String treatment_method = treatMethodSn.getSelectedItem().toString().trim();
            String branch = BranchSpinner.getSelectedItem().toString().trim();

            if (date.isEmpty() || currentTime.isEmpty()) {
                Toast.makeText(UserView.this, FILL_FORM, Toast.LENGTH_SHORT).show();
            } else {
                int hour = Integer.parseInt(currentTime.split(":")[0]);
                if (hour < 9 || hour > 22) {
                    messageDialogue.setMessage("We Provided Services 9 AM To 10 PM,Please Select Time Between 9 AM To 10 PM");
                    messageDialogue.show();
                } else {
                    if (!(name.isEmpty() || age.isEmpty() || mobileNumber.isEmpty() || gender.isEmpty() || date.isEmpty() || currentTime.isEmpty())) {
                        if (haveNetworkConnection(getApplicationContext())) {
                            UserService userService = new UserService(UserView.this);
                            userService.execute("make_appointment", name, age, mobileNumber, gender, date, currentTime, treatment_method, branch, problem);
                            appointmentDialog.dismiss();
                        } else Toast.makeText(UserView.this, PROVIDE_CONNECTION, Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(UserView.this, FILL_FORM, Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancelAppointment.setOnClickListener((View v) -> appointmentDialog.dismiss());
        appointmentDialog.show();
    }

    @Override
    public void onClick(View view) {
        if (view == makeAppointmentCv) {
            if (!haveNetworkConnection(this)) Toast.makeText(this, NO_INTERNET_TOAST, Toast.LENGTH_SHORT).show();
            else {
                FetchBranchList fetchBranchList = new FetchBranchList(this);
                fetchBranchList.execute();
            }
        } else if (view == allAppointmentCv) {
            if (!haveNetworkConnection(this)) Toast.makeText(this, NO_INTERNET_TOAST, Toast.LENGTH_SHORT).show();
             else {
                FetchUserAppointment fetchUserAppointment = new FetchUserAppointment(this);
                fetchUserAppointment.execute(prefUserInfo.getUserMobile());
            }
        } else if (view == reviewCv) {
            if (prefUserInfo.getReviewStatus().equalsIgnoreCase(ONE)) {
                AlertDialog reviewPost = new AlertDialog.Builder(UserView.this).create();
                reviewPost.setMessage("You Posted Review Already");
                reviewPost.show();
            } else {
                Dialog reviewDialog = showDialogTrans(this,R.layout.user_dialog_give_review,false,true);
                reviewDialog.show();

                Button sendReview = reviewDialog.findViewById(R.id.sendReview);
                Button cancelReview = reviewDialog.findViewById(R.id.cancelReview);
                EditText descriptionBox = reviewDialog.findViewById(R.id.descriptionBox);
                RatingBar ratingBar = reviewDialog.findViewById(R.id.rating);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    sendReview.setShadowLayer(50, 15, 15, R.color.indigo);
                    cancelReview.setShadowLayer(50, 15, 15, R.color.indigo);
                }

                sendReview.setOnClickListener((View v) -> {
                    String review = descriptionBox.getText().toString();
                    double rating = ratingBar.getRating();
                    if (!review.isEmpty() && review.length() >= 4) {
                        UserService userService = new UserService(UserView.this);
                        userService.execute("review", prefUserInfo.getUserName(), "" + rating, review);
                        reviewDialog.dismiss();
                    } else if (review.length() < 4) {
                        Toast.makeText(UserView.this, "Review Must be more than 3 Character", Toast.LENGTH_SHORT).show();
                    }
                });
                cancelReview.setOnClickListener((View v) -> reviewDialog.dismiss());
            }
        }
    }

    private void guestUserCheck (AlertDialog dialog,boolean guest){
        if (!haveNetworkConnection(getApplicationContext())) {
            Toast.makeText(UserView.this, PROVIDE_CONNECTION, Toast.LENGTH_LONG).show();
        } else {
            dialog.dismiss();
            createAppointment(guest);
        }
    }

    @Override
    public void onFetchData(List<?> listData) {
        branchList = new ArrayList<>();
        branchList = (List<String>) listData;

        AlertDialog dialog = showAlertBuilder(this,R.layout.user_dialog_appointment);
        dialog.show();

        TextView forMyself = dialog.findViewById(R.id.forMyself);
        TextView forOthers = dialog.findViewById(R.id.forOthers);
        TextView textChoice = dialog.findViewById(R.id.textChoice);

        forMyself.setTypeface(getBoldFont());
        forOthers.setTypeface(getBoldFont());
        textChoice.setTypeface(getRegularFont());

        forMyself.setOnClickListener((View v) -> guestUserCheck(dialog,false));
        forOthers.setOnClickListener((View v) -> guestUserCheck(dialog,true));
    }
}
