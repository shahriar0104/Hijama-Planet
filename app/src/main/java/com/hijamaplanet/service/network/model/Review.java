package com.hijamaplanet.service.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Review implements Parcelable {

    @SerializedName("review_id")
    private String review_id;

    @SerializedName("date")
    private String date;

    @SerializedName("review_star")
    private String review_star;

    @SerializedName("review_text")
    private String review_text;

    @SerializedName("reviewer_name")
    private String reviewer_name;

    protected Review(Parcel in) {
        review_id = in.readString();
        date = in.readString();
        review_star = in.readString();
        review_text = in.readString();
        reviewer_name = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getReview_id() {
        return review_id;
    }

    public String getDate() {
        return date;
    }

    public String getReview_star() {
        return review_star;
    }

    public String getReview_text() {
        return review_text;
    }

    public String getReviewer_name() {
        return reviewer_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(review_id);
        dest.writeString(date);
        dest.writeString(review_star);
        dest.writeString(review_text);
        dest.writeString(reviewer_name);
    }
}
