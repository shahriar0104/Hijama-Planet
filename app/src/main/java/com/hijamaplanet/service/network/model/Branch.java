package com.hijamaplanet.service.network.model;

import com.google.gson.annotations.SerializedName;

public class Branch {

    @SerializedName("branch_id")
    private String branch_id;

    @SerializedName("branch_name")
    private String branch_name;

    public String getBranch_id() {
        return branch_id;
    }

    public String getBranch_name() {
        return branch_name;
    }
}
