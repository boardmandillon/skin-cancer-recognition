package com.app.skinCancerDetection.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.app.skinCancerDetection.R;
import com.app.skinCancerDetection.LoginActivity;

public class EnterCodeFragment extends Fragment {

    LoginActivity main;
    ResetPasswordViewModel viewModel;
    View view;
    TextView codeField;
    TextView backToLogin;
    Button upload;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main = (LoginActivity)getActivity();
        view = inflater.inflate(R.layout.login_fragment_enter_code, container, false);
        backToLogin = view.findViewById(R.id.return_to_login);
        upload = view.findViewById(R.id.registerButton);
        codeField = view.findViewById(R.id.codeField);
        viewModel = ViewModelProviders.of(getActivity()).get(ResetPasswordViewModel.class);


        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //button listeners
        backToLogin.setOnClickListener(v -> main.getSupportFragmentManager().popBackStack());

        upload.setOnClickListener(v -> {
            String code = codeField.getText().toString();
            if(code.isEmpty()){
                codeField.setError("Enter the code");
                codeField.requestFocus();
                return;
            }

            viewModel.setCode(code);
            main.pushNewFragment(new NewPasswordFragment());
        });
    }
}
