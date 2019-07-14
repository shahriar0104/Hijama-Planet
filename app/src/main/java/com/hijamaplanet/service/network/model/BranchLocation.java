package com.hijamaplanet.service.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class BranchLocation implements Parcelable {

    @SerializedName("branch_location_id")
    private String branch_location_id;

    @SerializedName("branch_location")
    private String branch_location;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("branch_name")
    private String branch_name;

    @SerializedName("address")
    private String address;

    @SerializedName("phone")
    private String phone;

    protected BranchLocation(Parcel in) {
        branch_location_id = in.readString();
        branch_location = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        branch_name = in.readString();
        address = in.readString();
        phone = in.readString();
    }

    public static final Creator<BranchLocation> CREATOR = new Creator<BranchLocation>() {
        @Override
        public BranchLocation createFromParcel(Parcel in) {
            return new BranchLocation(in);
        }

        @Override
        public BranchLocation[] newArray(int size) {
            return new BranchLocation[size];
        }
    };

    public String getBranch_location_id() {
        return branch_location_id;
    }

    public String getBranch_location() {
        return branch_location;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(branch_location_id);
        dest.writeString(branch_location);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(branch_name);
        dest.writeString(address);
        dest.writeString(phone);
    }
}
