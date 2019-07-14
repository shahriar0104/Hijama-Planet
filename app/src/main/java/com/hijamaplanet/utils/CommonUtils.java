package com.hijamaplanet.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;

import com.hijamaplanet.user.AllAppointment;

public class CommonUtils {

    public static String timeFormatter(String time) {
        String arr[] = time.trim().split(":");
        int timeCheck = Integer.parseInt(arr[0]);
        if (timeCheck < 12) return arr[0] + ":" + arr[1] + " AM";
        else if (timeCheck == 12) return timeCheck + ":" + arr[1] + " PM";
        else if (timeCheck > 12) return timeCheck - 12 + ":" + arr[1] + " PM";
        return null;
    }

    public static String dateFormatterMtoY(String date) {
        String[] parts = date.trim().split("/");
        if (parts[1].equalsIgnoreCase("1")) return "JAN " + parts[0] + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("2")) return "FEB " + parts[0] + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("3")) return "MAR " + parts[0] + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("4")) return "APR " + parts[0] + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("5")) return "MAY " + parts[0] + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("6")) return "JUN " + parts[0] + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("7")) return "JUL " + parts[0] + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("8")) return "AUG " + parts[0] + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("9")) return "SEP " + parts[0] + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("10")) return "OCT " + parts[0] + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("11")) return "NOV " + parts[0] + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("12")) return "DEC " + parts[0] + "," + parts[2];
        return null;
    }

    public static String dateFormatterD(String date) {
        String[] parts = date.trim().split("/");
        if (parts[1].equalsIgnoreCase("1")) return "JAN " + parts[0] + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("2")) return "FEB " + parts[0] + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("3")) return "MAR " + parts[0] + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("4")) return "APR " + parts[0] + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("5")) return "MAY " + parts[0] + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("6")) return "JUN " + parts[0] + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("7")) return "JUL " + parts[0] + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("8")) return "AUG " + parts[0] + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("9")) return "SEP " + parts[0] + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("10")) return "OCT " + parts[0] + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("11")) return "NOV " + parts[0] + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("12")) return "DEC " + parts[0] + "," + parts[2];
        return null;
    }

    public static String dateFormatterDtoY(String date) {
        String[] parts = date.trim().split("/");
        if (parts[1].equalsIgnoreCase("1")) return parts[0] +" JAN" + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("2")) return parts[0] +" FEB" + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("3")) return parts[0] +" MAR" + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("4")) return parts[0] +" APR" + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("5")) return parts[0] +" MAY" + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("6")) return parts[0] +" JUN" + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("7")) return parts[0] +" JUL" + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("8")) return parts[0] +" AUG" + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("9")) return parts[0] +" SEP" + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("10")) return parts[0] +" OCT" + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("11")) return parts[0] +" NOV" + "," + parts[2];
        else if (parts[1].equalsIgnoreCase("12")) return parts[0] +" DEC" + "," + parts[2];
        return null;
    }
}
