package com.app.skinCancerDetection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.app.skinCancerDetection.ui.skin_cancer.CameraActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private static BottomNavigationView navView;
    private static ImageView openCamera;
    private static ConstraintLayout OpenCameraLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        // Open camera with imageView button
        openCamera = findViewById(R.id.openCamera);
        openCamera.setOnClickListener(v -> {
            startActivity(new Intent(this, CameraActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.nothing);
        });
    }

    public static void hideBottomNav(){
        navView.setVisibility(View.GONE);
        OpenCameraLayout.setVisibility(View.GONE);

    }
    public static void showBottomNav(){
        navView.setVisibility(View.VISIBLE);
        OpenCameraLayout.setVisibility(View.VISIBLE);
    }
}