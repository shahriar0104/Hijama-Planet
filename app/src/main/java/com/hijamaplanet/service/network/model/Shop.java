package com.hijamaplanet.service.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Shop implements Parcelable {

    @SerializedName("ser")
    private String serial_no;

    @SerializedName("header")
    private String header;

    @SerializedName("content")
    private String content;

    protected Shop(Parcel in) {
        serial_no = in.readString();
        header = in.readString();
        content = in.readString();
    }

    public static final Creator<Shop> CREATOR = new Creator<Shop>() {
        @Override
        public Shop createFromParcel(Parcel in) {
            return new Shop(in);
        }

        @Override
        public Shop[] newArray(int size) {
            return new Shop[size];
        }
    };

    public String getSerial_no() {
        return serial_no;
    }

    public String getHeader() {
        return header;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(serial_no);
        dest.writeString(header);
        dest.writeString(content);
    }
}
