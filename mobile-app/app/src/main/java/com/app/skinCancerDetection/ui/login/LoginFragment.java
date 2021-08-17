package com.app.skinCancerDetection.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.app.skinCancerDetection.MainActivity;
import com.app.skinCancerDetection.R;
import com.app.skinCancerDetection.API.RetrofitClient;
import com.app.skinCancerDetection.LoginActivity;
import com.app.skinCancerDetection.Model.LoginResponse;
import com.app.skinCancerDetection.Storage.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment {
    LoginActivity main;
    View view;
    EditText emailEditText;
    EditText passwordEditText;
    Button loginButton;
    TextView forgotPassword;
    TextView registerButton;
    String input_email;
    String input_password;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_fragment_login, container, false);
        emailEditText = view.findViewById(R.id.editTextEmail);
        passwordEditText = view.findViewById(R.id.editTextPassword2);
        registerButton = view.findViewById(R.id.registerButton);
        loginButton = view.findViewById(R.id.loginButton);
        forgotPassword = view.findViewById(R.id.forgotPassword);
        main = (LoginActivity)getActivity();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Button listeners
        loginButton.setOnClickListener(v -> {
            input_email = emailEditText.getText().toString();
            input_password = passwordEditText.getText().toString();
            login(input_email, input_password);
        });

        registerButton.setOnClickListener(v -> main.pushNewFragment(new RegisterFragment()));

        forgotPassword.setOnClickListener(v -> main.pushNewFragment(new PasswordResetFragment()));
    }

    private void login(String email, String password) {
        Call<LoginResponse> call = RetrofitClient
                .getInstanceAuth(email, password)
                .getApi()
                .userLogin();

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();

                if(response.code() == 200) {
                    if (loginResponse != null) {
                        String authToken = loginResponse.getAccessToken();
                        String refreshToken = loginResponse.getRefreshToken();
                        SharedPrefManager.getInstance(getActivity()).saveAccessToken(authToken);
                        SharedPrefManager.getInstance(getActivity()).saveRefreshToken(refreshToken);
                    }

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else if(response.code() == 401) {
                    Toast.makeText(getActivity(), "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Error Logging in", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Call Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
