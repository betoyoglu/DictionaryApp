package com.example.dictionaryapp.model

data class DictionaryData (
    val word : String,
    val meanings: List<Meaning>,
    val phonetics: List<Phonetic>
)

data class Phonetic(
    val text: String?,
    val audio: String?
)

data class Meaning (
    val partOfSpeech: String,
    val definitions: List<Definition>,
    val synonyms: List<String>,
    val antonyms: List<String>
)

data class Definition(
    val definition: String,
    val example: String?,
)