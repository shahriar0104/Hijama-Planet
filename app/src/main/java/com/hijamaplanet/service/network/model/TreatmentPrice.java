package com.hijamaplanet.service.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TreatmentPrice implements Parcelable {

    @SerializedName("ser")
    private String serial_no;

    @SerializedName("method")
    private String method;

    @SerializedName("plan_name")
    private String nameOfPlan;

    @SerializedName("plan_price")
    private String priceOfPlan;

    @SerializedName("plan_details")
    private String detailsOfPlan;

    protected TreatmentPrice(Parcel in) {
        serial_no = in.readString();
        method = in.readString();
        nameOfPlan = in.readString();
        priceOfPlan = in.readString();
        detailsOfPlan = in.readString();
    }

    public static final Creator<TreatmentPrice> CREATOR = new Creator<TreatmentPrice>() {
        @Override
        public TreatmentPrice createFromParcel(Parcel in) {
            return new TreatmentPrice(in);
        }

        @Override
        public TreatmentPrice[] newArray(int size) {
            return new TreatmentPrice[size];
        }
    };

    public String getSerial_no() {
        return serial_no;
    }

    public String getMethod() {
        return method;
    }

    public String getNameOfPlan() {
        return nameOfPlan;
    }

    public String getPriceOfPlan() {
        return priceOfPlan;
    }

    public String getDetailsOfPlan() {
        return detailsOfPlan;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(serial_no);
        dest.writeString(method);
        dest.writeString(nameOfPlan);
        dest.writeString(priceOfPlan);
        dest.writeString(detailsOfPlan);
    }
}
