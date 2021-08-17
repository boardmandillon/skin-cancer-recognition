package com.app.skinCancerDetection.API;

import android.content.Context;

import com.app.skinCancerDetection.Storage.SharedPrefManager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.Route;


public class TokenAuthenticator implements Authenticator {
    private final SharedPrefManager tokenRepository;

    public TokenAuthenticator(Context context) {
        this.tokenRepository = SharedPrefManager.getInstance(context);
    }

    @Override
    public Request authenticate(@NotNull Route route, @NotNull Response response) throws IOException {
        String refreshToken = tokenRepository.getRefreshToken();
        retrofit2.Response<ResponseBody> refreshTokenResponse =
                RetrofitClient.getInstance().getApi().refreshToken("Bearer " + refreshToken).execute();

        if (refreshTokenResponse.isSuccessful()) {
            try {
                assert refreshTokenResponse.body() != null;
                JSONObject json = new JSONObject(refreshTokenResponse.body().string());
                tokenRepository.saveAccessToken(json.getString("access_token"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            String accessToken = tokenRepository.getAccessToken();

            // retry request with the new tokens
            return response.request()
                    .newBuilder()
                    .header("Authorization", "Bearer " + accessToken)
                    .build();
        }

        // return the request with 401 error
        return null;
    }
}

