package com.hijamaplanet.service.network.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("user_id")
    private String id;

    @SerializedName("user_name")
    private String Name;

    @SerializedName("mobile_number")
    private String mobile;

    @SerializedName("age")
    private String age;

    @SerializedName("status")
    private String status;

    @SerializedName("sex")
    private String gender;

    @SerializedName("admin_branch_name")
    private String adminBranch;

    @SerializedName("review_status")
    private String reviewStatus;

    public String getId() {
        return id;
    }

    public String getName() {
        return Name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAge() {
        return age;
    }

    public String getStatus() {
        return status;
    }

    public String getGender() {
        return gender;
    }

    public String getAdminBranch() {
        return adminBranch;
    }

    public String getReviewStatus() {
        return reviewStatus;
    }
}
