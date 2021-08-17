package com.app.skinCancerDetection.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.app.skinCancerDetection.R;
import com.app.skinCancerDetection.API.RetrofitClient;
import com.app.skinCancerDetection.LoginActivity;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordResetFragment extends Fragment {

    LoginActivity main;
    View view;
    TextView email;
    TextView backToLogin;
    Button sendCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main = (LoginActivity)getActivity();
        view = inflater.inflate(R.layout.login_fragment_reset_password, container, false);
        email = view.findViewById(R.id.textField);
        backToLogin = view.findViewById(R.id.return_to_login);
        sendCode = view.findViewById(R.id.registerButton);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //button listeners
        backToLogin.setOnClickListener(v -> main.getSupportFragmentManager().popBackStack());

        sendCode.setOnClickListener(v -> {
            String inputEmail = email.getText().toString();

            if (inputEmail.isEmpty()) {
                email.setError("Email is required");
                email.requestFocus();
                return;
            }
            resetPassword(inputEmail);
            main.pushNewFragment(new EnterCodeFragment());
        });
    }

    private void resetPassword(String email) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .passwordReset(email);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Email Sent", Toast.LENGTH_LONG).show();

//                    main.pushNewFragment(new EnterCodeFragment());
                } else {
                    Toast.makeText(getActivity(), "Please Enter a Valid Email", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Failed to Make the Request", Toast.LENGTH_LONG).show();
            }
        });
    }

}
