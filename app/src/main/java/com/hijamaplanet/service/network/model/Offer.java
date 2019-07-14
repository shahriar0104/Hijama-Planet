package com.hijamaplanet.service.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Offer implements Parcelable {

    @SerializedName("Announcement_id")
    private String Announcement_id;

    @SerializedName("Announcement_title")
    private String Announcement_title;

    @SerializedName("Announcement_validDate")
    private String Announcement_validDate;

    @SerializedName("Announcement_desc")
    private String Announcement_desc;

    @SerializedName("image")
    private String image;

    protected Offer(Parcel in) {
        Announcement_id = in.readString();
        Announcement_title = in.readString();
        Announcement_validDate = in.readString();
        Announcement_desc = in.readString();
        image = in.readString();
    }

    public static final Creator<Offer> CREATOR = new Creator<Offer>() {
        @Override
        public Offer createFromParcel(Parcel in) {
            return new Offer(in);
        }

        @Override
        public Offer[] newArray(int size) {
            return new Offer[size];
        }
    };

    public String getAnnouncement_id() {
        return Announcement_id;
    }

    public String getAnnouncement_title() {
        return Announcement_title;
    }

    public String getAnnouncement_validDate() {
        return Announcement_validDate;
    }

    public String getAnnouncement_desc() {
        return Announcement_desc;
    }

    public String getImage() {
        return image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Announcement_id);
        dest.writeString(Announcement_title);
        dest.writeString(Announcement_validDate);
        dest.writeString(Announcement_desc);
        dest.writeString(image);
    }
}
