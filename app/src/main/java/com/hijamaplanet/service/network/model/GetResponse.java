package com.hijamaplanet.service.network.model;

import com.google.gson.annotations.SerializedName;

public class GetResponse {

    @SerializedName("response")
    private String Response;

    public String getResponse() {
        return Response;
    }
}
