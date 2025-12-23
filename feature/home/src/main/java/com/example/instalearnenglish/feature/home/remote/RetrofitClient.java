package com.example.instalearnenglish.feature.home.remote;

import java.util.HashMap;
import java.util.Map;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final Map<String, Retrofit> clients = new HashMap<>();
    private static final String DICTIONARY_BASE_URL = "https://api.dictionaryapi.dev/";

    public static Retrofit getClient() {
        return getClient(DICTIONARY_BASE_URL);
    }

    public static Retrofit getClient(String baseUrl) {
        if (!clients.containsKey(baseUrl)) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            clients.put(baseUrl, retrofit);
        }
        return clients.get(baseUrl);
    }
}
