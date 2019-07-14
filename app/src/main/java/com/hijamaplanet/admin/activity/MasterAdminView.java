package com.hijamaplanet.admin.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hijamaplanet.R;
import com.hijamaplanet.service.AdminService;
import com.hijamaplanet.service.UploadOfferService;
import com.hijamaplanet.service.parser.FetchLocationStatus;
import com.hijamaplanet.service.parser.FetchOffer;
import com.hijamaplanet.utils.DialogUtils;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dmax.dialog.SpotsDialog;
import pub.devrel.easypermissions.AppSettingsDialog;

import static com.hijamaplanet.utils.AppConstants.FILL_FORM;
import static com.hijamaplanet.utils.DialogUtils.showDateDialog;
import static com.hijamaplanet.utils.SpinnerUtil.showSpinner;

public class MasterAdminView extends AdminView {

    private TextView imageUpload;
    private final int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    //private static final String UPLOAD_URL = "https://hijama.com.bd/hijamaapp/hijama/addOffer.php";
    private final String UPLOAD_URL = "http://192.168.1.2/hijama/addOffer.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.admin_master_admin_view, null, false);
        extendedLayout.addView(contentView, 0);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        viewImp();
    }

    private void viewImp() {
        findViewById(R.id.addBranchCv).setOnClickListener((View v) -> {
            Dialog dialog = DialogUtils.showDialogTrans(this, R.layout.admin_dialog_add_branch, false, true);
            dialog.show();

            EditText newBranchName = dialog.findViewById(R.id.newBranchName);
            EditText newBranchLocation = dialog.findViewById(R.id.newBranchLocation);
            EditText newBranchLatitude = dialog.findViewById(R.id.newBranchLatitude);
            EditText newBranchLongitude = dialog.findViewById(R.id.newBranchLongitude);
            EditText newBranchAddress = dialog.findViewById(R.id.newBranchAddress);
            EditText newBranchPhone = dialog.findViewById(R.id.newBranchPhone);

            dialog.findViewById(R.id.confirmButton).setOnClickListener((View view) -> {
                String branchLoc = newBranchLocation.getText().toString().trim();
                String branchName = newBranchName.getText().toString().trim();
                String branchLat = newBranchLatitude.getText().toString().trim();
                String branchLong = newBranchLongitude.getText().toString().trim();
                String branchAdd = newBranchAddress.getText().toString().trim();
                String branchPhone = newBranchPhone.getText().toString().trim();
                String userPhone = prefUserInfo.getUserMobile();

                if ((branchLoc.isEmpty() || branchName.isEmpty() || branchLat.isEmpty() || branchLong.isEmpty() || branchAdd.isEmpty() || branchPhone.isEmpty()))
                    Toast.makeText(MasterAdminView.this, FILL_FORM, Toast.LENGTH_SHORT).show();
                else {
                    AdminService adminService = new AdminService(MasterAdminView.this);
                    adminService.execute("add_branch", branchLoc, branchName, branchLat, branchLong, branchAdd, branchPhone, userPhone);
                    dialog.dismiss();
                }
            });
            dialog.findViewById(R.id.cancelButton).setOnClickListener((View view) -> dialog.dismiss());
        });

        findViewById(R.id.addMasterAdminCv).setOnClickListener((View v) -> {
            Dialog dialog = DialogUtils.showDialogTrans(this, R.layout.admin_dialog_master_add_admin, false, false);
            dialog.show();

            EditText doctorName = dialog.findViewById(R.id.adminNameEt);
            EditText doctorMobile = dialog.findViewById(R.id.doctorMobileEt);
            EditText doctorAge = dialog.findViewById(R.id.doctorAgeEt);
            TextView countryCode = dialog.findViewById(R.id.country_code);
            Spinner docGenSpinner = dialog.findViewById(R.id.genderSn);

            List<String> genderList = new ArrayList<>();
            genderList.add("Male");
            genderList.add("Female");

            showSpinner(this, docGenSpinner, genderList);

            dialog.findViewById(R.id.addDoc).setOnClickListener((View view) -> {
                String name = doctorName.getText().toString().trim();
                String age = doctorAge.getText().toString().trim();
                String mobileNumber = countryCode.getText().toString().trim() + doctorMobile.getText().toString().trim();
                String userMob = prefUserInfo.getUserMobile();
                String key = activityStatus();
                String gender = docGenSpinner.getSelectedItem().toString().trim();

                if (mobileNumber.length() != 14)
                    Toast.makeText(this, "Please Enter a Valid number", Toast.LENGTH_SHORT).show();
                else {
                    if (name.isEmpty() || age.isEmpty())
                        Toast.makeText(this, FILL_FORM, Toast.LENGTH_SHORT).show();
                    else {
                        AdminService masterAdminService = new AdminService(MasterAdminView.this);
                        masterAdminService.execute("create master Admin", name, mobileNumber, userMob, age, gender, key);
                        dialog.dismiss();
                    }
                }
            });
            dialog.findViewById(R.id.cancelDoc).setOnClickListener((View view) -> dialog.dismiss());
        });

        findViewById(R.id.userStatisticsCv).setOnClickListener((View v) -> {
            FetchLocationStatus fetchLocationStatus = new FetchLocationStatus(this);
            fetchLocationStatus.execute();
        });

        findViewById(R.id.deleteOfferCv).setOnClickListener(v -> {
            FetchOffer fetchOffer = new FetchOffer(this, "delete");
            fetchOffer.execute();
        });

        findViewById(R.id.offerUploadCv).setOnClickListener((View v) -> {
            Dialog dialog = DialogUtils.showDialogTrans(this, R.layout.admin_dialog_upload_notice, false, false);
            dialog.show();

            EditText offerTitleTv = dialog.findViewById(R.id.offerTitleTv);
            imageUpload = dialog.findViewById(R.id.imageUpload);
            TextView dateSetTv = dialog.findViewById(R.id.dateSet);
            EditText offerDetailsTv = dialog.findViewById(R.id.offerDetailsTv);

            dateSetTv.setOnClickListener((View view) -> showDateDialog(this, dateSetTv));
            imageUpload.setOnClickListener((View view) -> showFileChooser());

            dialog.findViewById(R.id.uploadButton).setOnClickListener((View view) -> {
                String title = offerTitleTv.getText().toString().trim();
                String descriptionOfTitle = offerDetailsTv.getText().toString().trim();
                String date = dateSetTv.getText().toString().trim();

                if (imageUpload.getText().toString().trim().isEmpty())
                    Toast.makeText(MasterAdminView.this, "Please select a image", Toast.LENGTH_SHORT).show();
                else if (date.isEmpty() || title.isEmpty() || descriptionOfTitle.isEmpty())
                    Toast.makeText(this, FILL_FORM, Toast.LENGTH_SHORT).show();
                else {
                    dialog.dismiss();
                    uploadMultipart(title, date, descriptionOfTitle);
                    //new UploadOfferService(this).execute(title,date,descriptionOfTitle,getPath(filePath));
                }
            });
            dialog.findViewById(R.id.cancelButton).setOnClickListener((View view) -> dialog.dismiss());
        });
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                filePath = data.getData();
                String path = getPath(filePath);
                String[] arr = path.split("/");
                imageUpload.setText(arr[arr.length - 1]);
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
        }
    }

    //method to get the file path from uri
    private String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    private void uploadMultipart(String title, String dat, String description) {
        //getting the actual path of the image
        String path = getPath(filePath);
        SpotsDialog spotsDialog = new SpotsDialog(this);
        spotsDialog.setTitle("Uploading...");
        AlertDialog alertDialog = new AlertDialog.Builder(MasterAdminView.this).create();
        alertDialog.setTitle(getResources().getString(R.string.networkTitle));
        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();
            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                    .addParameter("offerTitle", title)
                    .addParameter("offerValidDate", dat)
                    .addParameter("offerDetails", description)
                    .addFileToUpload(path, "image")
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setUtf8Charset()
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                            spotsDialog.show();
                            spotsDialog.setMessage("Uploading...");
                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                            spotsDialog.dismiss();
                            alertDialog.setMessage(getResources().getString(R.string.networkResult));
                            alertDialog.show();
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                            spotsDialog.dismiss();
                            alertDialog.setMessage("Offer Uploaded");
                            alertDialog.show();
                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                            spotsDialog.dismiss();
                            alertDialog.setMessage(getResources().getString(R.string.networkResult));
                            alertDialog.show();
                        }
                    }).startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
