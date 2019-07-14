package com.hijamaplanet.service.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Dua implements Parcelable {

    @SerializedName("duas_id")
    private String duas_id;

    @SerializedName("duas_name")
    private String duas_name;

    @SerializedName("duas_link")
    private String duas_link;

    protected Dua(Parcel in) {
        duas_id = in.readString();
        duas_name = in.readString();
        duas_link = in.readString();
    }

    public static final Creator<Dua> CREATOR = new Creator<Dua>() {
        @Override
        public Dua createFromParcel(Parcel in) {
            return new Dua(in);
        }

        @Override
        public Dua[] newArray(int size) {
            return new Dua[size];
        }
    };

    public String getDuas_id() {
        return duas_id;
    }

    public String getDuas_name() {
        return duas_name;
    }

    public String getDuas_link() {
        return duas_link;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(duas_id);
        dest.writeString(duas_name);
        dest.writeString(duas_link);
    }
}
