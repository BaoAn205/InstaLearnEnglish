package com.example.instalearnenglish.feature.home.model;

import com.google.gson.annotations.SerializedName;

public class MyMemoryResponse {
    @SerializedName("responseData")
    private ResponseData responseData;

    public ResponseData getResponseData() {
        return responseData;
    }
}
