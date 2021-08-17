package com.app.skinCancerDetection.ui.login;

import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel {
    private boolean acceptedTerms;

    public RegisterViewModel(){
        acceptedTerms = false;
    }

    public boolean isAcceptedTerms() {
        return acceptedTerms;
    }

    public void acceptTerms(){
        acceptedTerms = true;
    }

    public void resetAcceptedTerms(){
        acceptedTerms = false;
    }
}
