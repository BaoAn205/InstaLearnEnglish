package com.example.instalearnenglish.feature.home.remote;

import com.example.instalearnenglish.feature.home.model.MyMemoryResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TranslationApiService {
    @GET("get")
    Call<MyMemoryResponse> getTranslation(
        @Query("q") String query,
        @Query("langpair") String langPair
    );
}
