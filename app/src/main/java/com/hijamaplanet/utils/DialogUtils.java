package com.hijamaplanet.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hijamaplanet.R;
import com.hijamaplanet.user.UserView;

import java.util.Calendar;
import java.util.Date;

import dmax.dialog.SpotsDialog;

import static com.hijamaplanet.utils.CommonUtils.timeFormatter;

public class DialogUtils {

    public static void showExitDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", (DialogInterface dialog, int id) -> activity.finishAffinity())
                .setNegativeButton("No", (DialogInterface dialog, int id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showDateDialog(Context context, TextView tv) {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(
                context,
                android.R.style.Theme_Material_Light_Dialog,
                (datePicker, year, month, day) -> tv.setText(day + "/" + (month + 1) + "/" + year)
                , cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        Calendar c = Calendar.getInstance();
        c.set(c.get(Calendar.YEAR) + 1, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        dialog.show();
    }

    public static void showDateDialogFixedDate(Context context, TextView tv , String date) {
        String savedDate[] = date.split("/");
        int mYear = Integer.parseInt(savedDate[2]);
        int mMmonth = Integer.parseInt(savedDate[1]) - 1;
        int mDay = Integer.parseInt(savedDate[0]);
        DatePickerDialog dialog = new DatePickerDialog(
                context,
                android.R.style.Theme_Material_Light_Dialog,
                (datePicker, year, month, day) -> tv.setText(day + "/" + (month + 1) + "/" + year)
                , mYear, mMmonth, mDay);

        Calendar c = Calendar.getInstance();
        c.set(c.get(Calendar.YEAR) + 1, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        dialog.show();
    }

    public static void showTimeDialog(Context context,TextView tv) {
        Calendar cal = Calendar.getInstance();
        new TimePickerDialog(context, (TimePicker view, int hourOfDay, int minute) -> tv.setText(hourOfDay + ":" + minute)
                , cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show();
    }

    public static void showTimeDialogFormatter(Context context,TextView tv) {
        Calendar cal = Calendar.getInstance();
        new TimePickerDialog(context, (TimePicker view, int hourOfDay, int minute) -> tv.setText(timeFormatter(hourOfDay + ":" + minute))
                , cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show();
    }

    public static Dialog showDialogTrans (Context context,int layout,boolean cancelable,boolean getWindow) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(layout);
        dialog.setCancelable(cancelable);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        if (getWindow) {
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        return dialog;
    }

    public static Dialog showDialogWhite (Context context,int layout,boolean cancelable,boolean getWindow) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(layout);
        dialog.setCancelable(cancelable);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
        if (getWindow) {
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        return dialog;
    }

    public static AlertDialog showAlertBuilder(Activity activity,int layout) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        View mView = activity.getLayoutInflater().inflate(layout, null);
        AlertDialog alertDialog = mBuilder.setView(mView).create();
        return alertDialog;
    }

    public static AlertDialog showAlert(Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        return alertDialog;
    }

    public static SpotsDialog showFetchingDialog(Context context) {
        SpotsDialog spotsDialog = new SpotsDialog(context,R.style.spotsDialog);
        spotsDialog.setTitle("Fetching Data...");
        spotsDialog.setCancelable(false);
        return spotsDialog;
    }

    public static ProgressDialog showProgressDialog(Context context,String msg) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(msg);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    public static ProgressDialog showLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }
}
