package com.app.skinCancerDetection.ui.skin_cancer;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.app.skinCancerDetection.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class CameraActivity extends AppCompatActivity {
    File imageFile;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.skin_cancer_activity_camera);
        loadFragment(new CameraFragment());

        backButton = findViewById(R.id.backButtonCamera);
        backButton.setOnClickListener(v -> finish());
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.skin_cancer_host_fragment, fragment);
        ft.commit();
    }


    // save bitmap to file
    public void saveBitmap(Bitmap bitmap, File imageFile) {
        this.imageFile = imageFile;

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, os);
            os.flush();
            os.close();
        } catch (Exception e) {}
    }

    public File getPhotoFile() {
        return imageFile;
    }
}
