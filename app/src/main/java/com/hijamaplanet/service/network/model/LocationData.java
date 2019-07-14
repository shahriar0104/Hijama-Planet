package com.hijamaplanet.service.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class LocationData implements Parcelable {

    @SerializedName("id")
    private String id;

    @SerializedName("user")
    private String user;

    @SerializedName("place")
    private String place;

    protected LocationData(Parcel in) {
        id = in.readString();
        user = in.readString();
        place = in.readString();
    }

    public static final Creator<LocationData> CREATOR = new Creator<LocationData>() {
        @Override
        public LocationData createFromParcel(Parcel in) {
            return new LocationData(in);
        }

        @Override
        public LocationData[] newArray(int size) {
            return new LocationData[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getPlace() {
        return place;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user);
        dest.writeString(place);
    }
}
