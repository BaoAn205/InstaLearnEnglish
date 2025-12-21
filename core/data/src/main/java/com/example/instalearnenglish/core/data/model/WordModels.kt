package com.example.instalearnenglish.core.data.model

import com.google.gson.annotations.SerializedName

data class Word(
    @SerializedName("word") val word: String,
    @SerializedName("phonetics") val phonetics: List<Phonetic>?,
    @SerializedName("meanings") val meanings: List<Meaning>?
)

data class Phonetic(
    @SerializedName("text") val text: String?,
    @SerializedName("audio") val audio: String?
)

data class Meaning(
    @SerializedName("partOfSpeech") val partOfSpeech: String?,
    @SerializedName("definitions") val definitions: List<Definition>?
)

data class Definition(
    @SerializedName("definition") val definition: String?,
    @SerializedName("example") val example: String?
)

data class MyMemoryResponse(
    @SerializedName("responseData") val responseData: ResponseData?
)

data class ResponseData(
    @SerializedName("translatedText") val translatedText: String?
)
