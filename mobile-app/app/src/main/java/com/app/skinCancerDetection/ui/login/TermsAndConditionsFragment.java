package com.app.skinCancerDetection.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.app.skinCancerDetection.LoginActivity;
import com.app.skinCancerDetection.R;

public class TermsAndConditionsFragment extends Fragment {
    LoginActivity main;
    RegisterViewModel viewModel;
    View view;
    Button acceptButton;
    ScrollView scrollView;
    TextView tsAndCs;
    TextView backButton;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.login_fragment_terms_conditions, container, false);
        main = (LoginActivity)getActivity();
        backButton = view.findViewById(R.id.return_to_login);
        acceptButton = view.findViewById(R.id.nextButton);
        scrollView = view.findViewById(R.id.scrollView);
        tsAndCs = view.findViewById(R.id.tsandcs);


        viewModel = ViewModelProviders.of(getActivity()).get(RegisterViewModel.class);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backButton.setOnClickListener(v -> main.getSupportFragmentManager().popBackStack());

        acceptButton.setOnClickListener(v -> {
            viewModel.acceptTerms();
            main.getSupportFragmentManager().popBackStack();
        });

        String[] terms = getResources().getStringArray(R.array.terms_and_conditions);

        for(String term : terms){
            tsAndCs.append(term);
        }
    }
}
