package com.app.skinCancerDetection.ui.login;

import androidx.lifecycle.ViewModel;

public class ResetPasswordViewModel extends ViewModel {

    private String code;

    public ResetPasswordViewModel() {
        code = "";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String token) {
        code = token;
    }

}
