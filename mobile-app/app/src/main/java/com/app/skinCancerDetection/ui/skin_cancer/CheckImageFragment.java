package com.app.skinCancerDetection.ui.skin_cancer;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.app.skinCancerDetection.API.RetrofitClient;
import com.app.skinCancerDetection.Model.Skin_cancer.PredictResponse;
import com.app.skinCancerDetection.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CheckImageFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.skin_cancer_image_fragment,
                container,
                false
        );
        File mPhotoFile = ((CameraActivity) requireActivity()).getPhotoFile();
        ImageView lesionImage = view.findViewById(R.id.resultImage);
        lesionImage.setImageBitmap(BitmapFactory.decodeFile(mPhotoFile.getPath()));

        Button sendImage = view.findViewById(R.id.sendImage);
        sendImage.setOnClickListener(v -> predictImage(mPhotoFile));

        return view;
    }

    private void predictImage(File file) {
        RequestBody requestFile = RequestBody.create(file, MediaType.parse("multipart/form-data"));
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", "lesion", requestFile);

        // dummy call due to refresh token bug
        Call<PredictResponse> call = RetrofitClient.getInstanceToken(getContext()).getApi().check();
        executeCall(call);

        // actual skin lesion prediction call
        call = RetrofitClient.getInstanceToken(getContext()).getApi().getPrediction(body);
        executeCall(call);
    }

    private void executeCall(Call<PredictResponse> call){
        call.enqueue(new Callback<PredictResponse>() {
            @Override
            public void onResponse(@NotNull Call<PredictResponse> call, @NotNull Response<PredictResponse> response) {
                PredictResponse predictResponse = response.body();

                if(response.code() == 201) {
                    assert predictResponse != null;
                    String classification = predictResponse.getClassification();
                    String percentage = predictResponse.getPercentage();

                    // replace current fragment
                    Fragment nextFrag = new ResultFragment().newInstance(classification, percentage);
                    getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(((ViewGroup)getView().getParent()).getId(), nextFrag, "CameraFragment")
                        .commit();
                }
            }

            @Override
            public void onFailure(@NotNull Call<PredictResponse> call, @NotNull Throwable t) {
                Toast.makeText(getActivity(), "Call Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}