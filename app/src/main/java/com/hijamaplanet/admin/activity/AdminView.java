package com.hijamaplanet.admin.activity;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hijamaplanet.BaseActivity;
import com.hijamaplanet.R;
import com.hijamaplanet.admin.fragment.ApprovedAppointment;
import com.hijamaplanet.service.network.NetworkCallback;
import com.hijamaplanet.service.AdminService;
import com.hijamaplanet.service.parser.FetchBranchList;
import com.hijamaplanet.service.parser.FetchReview;
import com.hijamaplanet.service.parser.FetchUnApprovedAppointment;
import com.hijamaplanet.utils.DialogUtils;
import com.hijamaplanet.utils.SpinnerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.hijamaplanet.utils.AppConstants.FILL_FORM;
import static com.hijamaplanet.utils.AppConstants.NO_INTERNET_SNACKBAR;
import static com.hijamaplanet.utils.AppConstants.ONE;
import static com.hijamaplanet.utils.AppConstants.THREE;
import static com.hijamaplanet.utils.NetworkUtil.haveNetworkConnection;

public class AdminView extends BaseActivity implements View.OnClickListener, NetworkCallback {
    private static final String TAG = "AdminView";
    protected CardView appointmentPendingCv, appointmentApproveCv, addAdminCv, deleteReviewCv;
    protected TextView user_profile, mobileTv, branchTv, ageTv, genderTv, ageTextTv, genderTextTv;
    protected LinearLayout extendedLayout;
    protected List<String> BranchList = new ArrayList<>();
    protected String branch = null;
    protected Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.admin_activity_admin_view, null, false);
        drawer.addView(contentView, 1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) checkPermission();
        duaDownloadResume();

        viewInit();
        viewImplementation();
    }

    protected void viewInit() {
        extendedLayout = findViewById(R.id.extendedLayout);
        appointmentPendingCv = findViewById(R.id.appointmentPendingCv);
        appointmentApproveCv = findViewById(R.id.appointmentApproveCv);
        addAdminCv = findViewById(R.id.addAdminCv);
        deleteReviewCv = findViewById(R.id.deleteReviewCv);

        user_profile = findViewById(R.id.profileNameTv);
        mobileTv = findViewById(R.id.mobileTv);
        branchTv = findViewById(R.id.branchTv);
        ageTv = findViewById(R.id.ageTv);
        genderTv = findViewById(R.id.genderTv);
        ageTextTv = findViewById(R.id.ageTextTv);
        genderTextTv = findViewById(R.id.genderTextTv);
    }

    protected void viewImplementation() {
        user_profile.setText(prefUserInfo.getUserName());
        mobileTv.setText(prefUserInfo.getUserMobile());
        ageTv.setText(prefUserInfo.getUserAge());
        genderTv.setText(prefUserInfo.getUserGender());
        if (ONE.equalsIgnoreCase(prefUserInfo.getUserStatus())) {
            branchTv.setVisibility(View.VISIBLE);
            branchTv.setText(prefUserInfo.getAdminBranchName() + " Branch");
        }

        appointmentPendingCv.setOnClickListener(this);
        appointmentApproveCv.setOnClickListener(this);
        addAdminCv.setOnClickListener(this);
        deleteReviewCv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == appointmentPendingCv) {
            if (!haveNetworkConnection(this)) {
                showNoInternetSnackBar();
            } else {
                if (ONE.equalsIgnoreCase(prefUserInfo.getUserStatus())) {
                    FetchUnApprovedAppointment fetchUnApprovedAppointment = new FetchUnApprovedAppointment(this);
                    fetchUnApprovedAppointment.execute("admin", prefUserInfo.getUserId());
                } else if (THREE.equalsIgnoreCase(prefUserInfo.getUserStatus())) {
                    FetchUnApprovedAppointment fetchUnApprovedAppointment = new FetchUnApprovedAppointment(this);
                    fetchUnApprovedAppointment.execute("master", prefUserInfo.getUserId());
                }
            }

        } else if (view == appointmentApproveCv) {
            if (!haveNetworkConnection(this)) {
                showNoInternetSnackBar();
            } else {
                removeFragmentBackStack();
                openFragment(new ApprovedAppointment());
            }

        } else if (view == addAdminCv) {
            if (ONE.equalsIgnoreCase(prefUserInfo.getUserStatus())) addAdmin(BranchList);
            else if (THREE.equalsIgnoreCase(prefUserInfo.getUserStatus())) {
                if (!haveNetworkConnection(this)) {
                    showNoInternetSnackBar();
                } else {
                    FetchBranchList fetchBranchList = new FetchBranchList(this);
                    fetchBranchList.execute();
                }
            }

        } else if (view == deleteReviewCv) {
            if (!haveNetworkConnection(this)) {
                showNoInternetSnackBar();
            } else {
                FetchReview fetchReview = new FetchReview(this, "delete");
                fetchReview.execute();
                removeFragmentBackStack();
            }
        }
    }

    @Override
    public void onFetchData(List<?> listData) {
        BranchList = (List<String>) listData;
        if (BranchList.size() == 0)
            Toast.makeText(this, "No Branch Added Yet", Toast.LENGTH_SHORT).show();
        else addAdmin(BranchList);
    }

    protected void addAdmin(List<String> BranchList) {
        Dialog dialog = DialogUtils.showDialogTrans(this, R.layout.admin_dialog_add_admin, false, false);
        dialog.show();

        EditText adminNameEt = dialog.findViewById(R.id.adminNameEt);
        EditText doctorMobileEt = dialog.findViewById(R.id.doctorMobileEt);
        EditText doctorAgeEt = dialog.findViewById(R.id.doctorAgeEt);
        TextView branchDialogTv = dialog.findViewById(R.id.branchTv);
        TextView countryCode = dialog.findViewById(R.id.country_code);
        Spinner genderSn = dialog.findViewById(R.id.genderSn);
        Spinner branchSn = dialog.findViewById(R.id.branchSn);


        List<String> genderList = new ArrayList<>();
        genderList.add("Male");
        genderList.add("Female");
        SpinnerUtil.showSpinner(this, genderSn, genderList);

        if (BranchList.size() == 0) {
            branchDialogTv.setVisibility(View.VISIBLE);
            branchDialogTv.setText(prefUserInfo.getAdminBranchName());
        } else {
            branchSn.setVisibility(View.VISIBLE);
            SpinnerUtil.showSpinner(this, branchSn, BranchList);
        }

        dialog.findViewById(R.id.addDoc).setOnClickListener((View v) -> {
            String name = adminNameEt.getText().toString().trim();
            String age = doctorAgeEt.getText().toString().trim();
            String docMob = countryCode.getText().toString().trim() + doctorMobileEt.getText().toString().trim();
            String userMob = prefUserInfo.getUserMobile().trim();
            String gender = genderSn.getSelectedItem().toString().trim();
            String key = activityStatus();
            if (branchDialogTv.getVisibility() == View.VISIBLE) branch = branchDialogTv.getText().toString().trim();
            else if (branchSn.getVisibility() == View.VISIBLE) branch = branchSn.getSelectedItem().toString().trim();

            if (docMob.length() != 14)
                Toast.makeText(this, "Please Enter a Valid number", Toast.LENGTH_SHORT).show();
            else {
                if (name.isEmpty() || age.isEmpty())
                    Toast.makeText(this, FILL_FORM, Toast.LENGTH_SHORT).show();
                else {
                    AdminService adminService = new AdminService(AdminView.this);
                    adminService.execute("create new Admin", name, docMob, userMob, age, gender, branch, key);
                    dialog.dismiss();
                }
            }
        });
        dialog.findViewById(R.id.cancelDoc).setOnClickListener((View v) -> dialog.dismiss());
    }

    protected void showNoInternetSnackBar() {
        snackbar = Snackbar.make(findViewById(R.id.content_nav), NO_INTERNET_SNACKBAR, Snackbar.LENGTH_SHORT)
                .setAction("Close", (View v) -> snackbar.dismiss());
        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.snackbar));
        snackbar.show();
    }

    protected String activityStatus() {
        String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789$&@?<>~!%#()[]{}/=+^*|";
        Random r = new Random();
        while (true) {
            char[] genre = new char[r.nextBoolean() ? 49 : 50];
            boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;
            for (int i = 0; i < genre.length; i++) {
                char ch = symbols.charAt(r.nextInt(symbols.length()));
                if (Character.isUpperCase(ch)) hasUpper = true;
                else if (Character.isLowerCase(ch)) hasLower = true;
                else if (Character.isDigit(ch)) hasDigit = true;
                else hasSpecial = true;
                genre[i] = ch;
            }
            if (hasUpper && hasLower && hasDigit && hasSpecial) return new String(genre);
        }
    }
}
