package com.example.instalearnenglish.core.data.remote

import com.example.instalearnenglish.core.data.model.MyMemoryResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface TranslationApiService {
    @GET("get")
    fun getTranslation(
        @Query("q") text: String,
        @Query("langpair") langPair: String
    ): Call<MyMemoryResponse>
}

object TranslationApiClient {
    private const val BASE_URL = "https://api.mymemory.translated.net/"
    
    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
