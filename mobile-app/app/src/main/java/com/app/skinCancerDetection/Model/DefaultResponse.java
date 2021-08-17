package com.app.skinCancerDetection.Model;

import com.google.gson.annotations.SerializedName;


public class DefaultResponse {
    @SerializedName("error")
    private String err;

    @SerializedName("message")
    private String msg;

    @SerializedName("email")
    private String emailMsg;

    public DefaultResponse(String err, String msg, String emailMsg) {
        this.err = err;
        this.msg = msg;
        this.emailMsg = emailMsg;
    }
}
