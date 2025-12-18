package com.example.instalearnenglish.remote;

import com.example.instalearnenglish.model.Word;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DictionaryApiService {
    @GET("api/v2/entries/en/{word}")
    Call<List<Word>> getWordDefinition(@Path("word") String word);
}
