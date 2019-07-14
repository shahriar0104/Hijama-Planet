package com.hijamaplanet.service.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class UserAppointmentHistory implements Parcelable{

    @SerializedName("appointment_id")
    private String appointment_id;

    @SerializedName("name")
    private String Name;

    @SerializedName("mobile_number")
    private String mobile;

    @SerializedName("sex")
    private String gender;

    @SerializedName("date")
    private String date;

    @SerializedName("time")
    private String time;

    @SerializedName("treatment_method")
    private String treatment_metod;

    @SerializedName("branch")
    private String branch;

    @SerializedName("approved_status")
    private String approved_status;

    @SerializedName("problem")
    private String problem;

    protected UserAppointmentHistory(Parcel in) {
        appointment_id = in.readString();
        Name = in.readString();
        mobile = in.readString();
        gender = in.readString();
        date = in.readString();
        time = in.readString();
        treatment_metod = in.readString();
        branch = in.readString();
        approved_status = in.readString();
        problem = in.readString();
    }

    public static final Creator<UserAppointmentHistory> CREATOR = new Creator<UserAppointmentHistory>() {
        @Override
        public UserAppointmentHistory createFromParcel(Parcel in) {
            return new UserAppointmentHistory(in);
        }

        @Override
        public UserAppointmentHistory[] newArray(int size) {
            return new UserAppointmentHistory[size];
        }
    };

    public String getAppointment_id() {
        return appointment_id;
    }

    public String getName() {
        return Name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getGender() {
        return gender;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getTreatment_metod() {
        return treatment_metod;
    }

    public String getBranch() {
        return branch;
    }

    public String getApproved_status() {
        return approved_status;
    }

    public String getProblem() {
        return problem;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(appointment_id);
        dest.writeString(Name);
        dest.writeString(mobile);
        dest.writeString(gender);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(treatment_metod);
        dest.writeString(branch);
        dest.writeString(approved_status);
        dest.writeString(problem);
    }
}
