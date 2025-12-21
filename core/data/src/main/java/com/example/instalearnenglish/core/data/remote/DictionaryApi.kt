package com.example.instalearnenglish.core.data.remote

import com.example.instalearnenglish.core.data.model.Word
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApiService {
    @GET("api/v2/entries/en/{word}")
    fun getWordDefinition(@Path("word") word: String): Call<List<Word>>
}

object RetrofitClient {
    private const val BASE_URL = "https://api.dictionaryapi.dev/"
    
    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
