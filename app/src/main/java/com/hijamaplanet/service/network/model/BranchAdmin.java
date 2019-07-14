package com.hijamaplanet.service.network.model;

import com.google.gson.annotations.SerializedName;

public class BranchAdmin {

    @SerializedName("branch_name")
    private String branch_name;

    public String getBranch_name() {
        return branch_name;
    }
}
