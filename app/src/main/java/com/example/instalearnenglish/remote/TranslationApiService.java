package com.example.instalearnenglish.remote;

import com.example.instalearnenglish.model.MyMemoryResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TranslationApiService {
    @GET("get")
    Call<MyMemoryResponse> getTranslation(@Query("q") String word, @Query("langpair") String langPair);
}
