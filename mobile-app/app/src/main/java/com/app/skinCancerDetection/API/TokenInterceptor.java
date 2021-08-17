package com.app.skinCancerDetection.API;

import android.content.Context;

import com.app.skinCancerDetection.Storage.SharedPrefManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {
    private final SharedPrefManager tokenRepository;

    public TokenInterceptor(Context context) {
        this.tokenRepository = SharedPrefManager.getInstance(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String accessToken = tokenRepository.getAccessToken();

        Request request = chain.request();
        Request authenticatedRequest = request
                .newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .build();

        return chain.proceed(authenticatedRequest);
    }
}

