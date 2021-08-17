package com.app.skinCancerDetection.ui.skin_cancer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.app.skinCancerDetection.R;
import com.app.skinCancerDetection.ui.skin_cancer.ui.PreviewOverlay;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;


public class CameraFragment extends Fragment {
    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_REQUEST_CODE = 10;
    private Camera camera;
    private PreviewOverlay maskSquare;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!hasCameraPermission()) {
            requestPermission();
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(
            R.layout.skin_cancer_camera_fragment,
            container,
            false
        );

//        View flashScreen = view.findViewById(R.id.imageFlash);
        previewView = view.findViewById(R.id.previewView);
        maskSquare = view.findViewById(R.id.maskSquare);

        ImageView captureImage = view.findViewById(R.id.takePhoto);
        Animation mAnimation = new AlphaAnimation(1, 0);
        mAnimation.setDuration(200);
        mAnimation.setInterpolator(new LinearInterpolator());
        captureImage.setOnClickListener(v -> {
            takePicture();
            captureImage.setVisibility(View.INVISIBLE);
        });

        cameraProviderFuture = ProcessCameraProvider.getInstance(getActivity());
        cameraProviderFuture.addListener(() -> {
            ProcessCameraProvider cameraProvider = null;
            try {
                cameraProvider = cameraProviderFuture.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setupCamera(cameraProvider);
        }, ContextCompat.getMainExecutor(requireContext()));

        return view;
    }

    private void setupCamera(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview);
    }

    public void takePicture() {
        // get bitmap from camera preview
        Bitmap bitmap = previewView.getBitmap();
        bitmap = cropBitmap(bitmap);

        File photoFile = new File(getContext().getCacheDir(), "lesion");
        ((CameraActivity) getActivity()).saveBitmap(bitmap, photoFile);

        // open new fragment
        Fragment nextFrag = new CheckImageFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentFrame, nextFrag, "CameraFragment")
                .commit();
    }

    // Crop bitmap to preview overlay
    private Bitmap cropBitmap(Bitmap bitmap) {
        int newSize = maskSquare.getHeight();
        int x = maskSquare.getLeft();
        int y = maskSquare.getTop();

        // scale bitmap to `previewView`
        bitmap = Bitmap.createScaledBitmap(
            bitmap, previewView.getWidth(), previewView.getHeight(), false
        );

        // crop bitmap to `maskSquare`
        return Bitmap.createBitmap(bitmap, x, y, newSize, newSize);
    }

    // get camera permission
    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(
                getContext(),
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }
    // request camera permission
    private void requestPermission() {
        ActivityCompat.requestPermissions(
                getActivity(),
                CAMERA_PERMISSION,
                CAMERA_REQUEST_CODE
        );
    }
}