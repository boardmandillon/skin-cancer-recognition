package com.app.skinCancerDetection.API;

import com.app.skinCancerDetection.Model.DefaultResponse;
import com.app.skinCancerDetection.Model.LoginResponse;
import com.app.skinCancerDetection.Model.Skin_cancer.PredictResponse;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface Api {
    @FormUrlEncoded
    @POST("users")
    Call<DefaultResponse> createUser(
            @Field("email") String email,
            @Field("password") String password,
            @Field("name") String name,
            @Field("date_of_birth") String dateOfBirth
    );

    @POST("authenticate")
    Call<LoginResponse> userLogin();

    @POST("refresh")
    Call<ResponseBody> refreshToken(
            @Header("Authorization") String refreshToken
    );

    @FormUrlEncoded
    @PUT("users/password-reset")
    Call<ResponseBody> passwordReset(
            @Field("email") String email
    );

    @FormUrlEncoded
    @PUT("users/password")
    Call<ResponseBody> changePassword(
            @Field("token") String token,
            @Field("new_password") String newPassword
    );

    @Multipart
    @POST("skin-cancer-diagnosis")
    Call<PredictResponse> getPrediction(
            @Part MultipartBody.Part image
    );

    // refresh token bug workaround
    @POST("skin-cancer-diagnosis")
    Call<PredictResponse> check();

}
