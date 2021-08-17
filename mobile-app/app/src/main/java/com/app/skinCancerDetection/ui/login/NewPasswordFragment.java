package com.app.skinCancerDetection.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.app.skinCancerDetection.API.RetrofitClient;
import com.app.skinCancerDetection.LoginActivity;
import com.app.skinCancerDetection.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPasswordFragment extends Fragment {

    LoginActivity main;
    ResetPasswordViewModel viewModel;
    View view;
    TextView password;
    TextView repeatedPassword;
    TextView backToLogin;
    Button upload;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main = (LoginActivity)getActivity();
        view = inflater.inflate(R.layout.login_fragment_new_password, container, false);
        backToLogin = view.findViewById(R.id.back);
        password = view.findViewById(R.id.editTextName);
        repeatedPassword = view.findViewById(R.id.editTextEmail);
        upload = view.findViewById(R.id.registerButton);

        viewModel = ViewModelProviders.of(getActivity()).get(ResetPasswordViewModel.class);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //button listeners
        backToLogin.setOnClickListener(v -> main.getSupportFragmentManager().popBackStack());

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = viewModel.getCode();
                String inputPassword = password.getText().toString();
                String inputRepeatedPassword = repeatedPassword.getText().toString();
                if (inputPassword.isEmpty()) {
                    password.setError("Password is required");
                    password.requestFocus();
                    return;
                }
                else if(code.isEmpty()){
                    password.setError("Enter the code");
                    password.requestFocus();
                    return;
                }
                else if (inputPassword.length() < 6) {
                    password.setError("Password must contain at least 6 characters");
                    password.requestFocus();
                    return;
                }else if (!checkPassword(inputPassword)) {
                    password.setError("Password must contain at least one lowercase and uppercase letter and a digit");
                    password.requestFocus();
                    return;
                }
                else if (! inputPassword.equals(inputRepeatedPassword)){
                    repeatedPassword.setError("Passwords do not match");
                    repeatedPassword.requestFocus();
                }

                resetPassword(code, inputPassword);
            }
        });
    }

    private void resetPassword(String token, String newPassword) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .changePassword(token, newPassword);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Your Password Has Been Updated", Toast.LENGTH_LONG).show();
                    main.loadFragment(new LoginFragment());

                    // Clear fragments stack
                    main.getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else {
                    Toast.makeText(getActivity(), "Please Enter a Valid Password and Code", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Failed to update password", Toast.LENGTH_LONG).show();
            }
        });
    }

    private static boolean checkPassword(String password) {
        char currentCharacter;
        boolean numberPresent = false;
        boolean upperCasePresent = false;
        boolean lowerCasePresent = false;

        for (int i = 0; i < password.length(); i++) {
            currentCharacter = password.charAt(i);
            if (Character.isDigit(currentCharacter)) {
                numberPresent = true;
            } else if (Character.isUpperCase(currentCharacter)) {
                upperCasePresent = true;
            } else if (Character.isLowerCase(currentCharacter)) {
                lowerCasePresent = true;
            }
        }
        return numberPresent && upperCasePresent && lowerCasePresent;
    }
}
