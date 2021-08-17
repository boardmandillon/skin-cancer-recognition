package com.app.skinCancerDetection.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.app.skinCancerDetection.LoginActivity;
import com.app.skinCancerDetection.R;
import com.app.skinCancerDetection.API.RetrofitClient;
import com.app.skinCancerDetection.Model.DefaultResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {
    LoginActivity main;
    View view;
    RegisterViewModel viewModel;
    EditText nameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText passwordEditText2;
    TextView backToLogin;
    Button registerButton;
    Spinner yearSpinner;
    Spinner monthSpinner;
    Spinner daySpinner;
    String input_name;
    String input_email;
    String input_password;
    String birth_date;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_fragment_register, container, false);
        nameEditText = view.findViewById(R.id.editTextName);
        emailEditText = view.findViewById(R.id.editTextEmail);
        passwordEditText2 = view.findViewById(R.id.editTextPassword2);
        passwordEditText = view.findViewById(R.id.editTextPassword);
        backToLogin = view.findViewById(R.id.return_to_login);
        registerButton = view.findViewById(R.id.registerButton);
        yearSpinner = view.findViewById(R.id.yearSpinner);
        monthSpinner = view.findViewById(R.id.monthSpinner);
        daySpinner = view.findViewById(R.id.daySpinner);
        main = (LoginActivity)getActivity();

        viewModel = ViewModelProviders.of(getActivity()).get(RegisterViewModel.class);

        // Populating date spinners with actual numbers
        ArrayList<String> years = new ArrayList<String>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for(int i = currentYear; i >= 1900; i-=1){
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, years);
        yearSpinner.setAdapter(adapterYear);

        ArrayList<String> days = new ArrayList<String>();
        for(int i = 1; i <= 31; i++){
            days.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapterDay = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, days);
        daySpinner.setAdapter(adapterDay);

        ArrayList<String> months = new ArrayList<String>();
        for(int i = 1; i <= 12; i++){
            months.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapterMonth = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, months);
        monthSpinner.setAdapter(adapterMonth);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Button listeners
        backToLogin.setOnClickListener(v -> main.loadFragment(new LoginFragment()));

        registerButton.setOnClickListener(v -> {
            input_name = nameEditText.getText().toString();
            input_email = emailEditText.getText().toString();
            input_password = passwordEditText.getText().toString();

            String repeatedPassword = passwordEditText2.getText().toString();
            Boolean inputError = false;

            if (input_email.isEmpty()) {
                emailEditText.setError("Email is required");
                emailEditText.requestFocus();
                inputError = true;
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(input_email).matches()) {
                emailEditText.setError("Enter a valid email");
                emailEditText.requestFocus();
                inputError = true;
            }
            if (input_password.isEmpty()) {
                passwordEditText.setError("Password is required");
                passwordEditText.requestFocus();
                inputError = true;
            }
            if (input_name.isEmpty()) {
                nameEditText.setError("Name is required");
                nameEditText.requestFocus();
                inputError = true;
            }
            else if (input_password.length() < 6) {
                passwordEditText.setError("Password must contain at least 6 characters");
                passwordEditText.requestFocus();
                inputError = true;
            }else if (! input_password.equals(repeatedPassword)){
                passwordEditText2.setError("Passwords do not match");
                passwordEditText2.requestFocus();
                inputError = true;
            }

            if (inputError == true) return;

            birth_date = yearSpinner.getSelectedItem().toString() + "/" +
                         monthSpinner.getSelectedItem().toString() + "/" +
                         daySpinner.getSelectedItem().toString();

            main.pushNewFragment(new TermsAndConditionsFragment());
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel.isAcceptedTerms()){
            register();
            viewModel.resetAcceptedTerms();
        }

        Log.v("Register", "Resumed");
    }

    public void register() {
        Call<DefaultResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .createUser(input_email, input_password, input_name, birth_date);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.code() == 201) {
                    Toast.makeText(getActivity(), "Thank you for Registering", Toast.LENGTH_LONG).show();
                    main.loadFragment(new LoginFragment());
                }
                else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String errorMessage = jsonObject.getString("message");
                        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Log.d("debug", "Cause: " + t.getCause());
                Toast.makeText(getActivity(), "Call Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
