package com.app.skinCancerDetection;

import android.os.Bundle;

import com.app.skinCancerDetection.ui.login.LoginFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_login);
        loadFragment(new LoginFragment());
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentFrame, fragment);
        ft.commit();
    }

    // loads a new fragment and keeps the previous one on the stack
    public void pushNewFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentFrame, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}