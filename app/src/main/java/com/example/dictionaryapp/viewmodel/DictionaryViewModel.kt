package com.example.dictionaryapp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionaryapp.entity.WordDatabase
import com.example.dictionaryapp.entity.WordEntity
import com.example.dictionaryapp.model.DictionaryData
import com.example.dictionaryapp.model.RetrofitInstance
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DictionaryViewModel (application: Application) : AndroidViewModel(application){

    var isLoading by mutableStateOf(false)
    private set // sadece burdan değiştirilebilmesi için

    var wordData by mutableStateOf<DictionaryData?>(null)
    private set // recomposition gerektiğinde

    private val db = WordDatabase.getDatabase(application)
    private val dao = db.wordDao

    val favoriteWords: StateFlow<List<WordEntity>> = dao.getAllFavoriteWords()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun saveWord(word: WordEntity) {
        viewModelScope.launch {
            dao.insertFavoriteWord(word)
        }
    }

    fun unsaveWord(word: WordEntity) {
        viewModelScope.launch {
            dao.deleteFavoriteWord(word)
        }
    }

    fun onFavoriteButtonClicked(word: WordEntity, currentlyFavorite: Boolean) {
        if (currentlyFavorite) {
            unsaveWord(word) // Zaten favoriyse sil
        } else {
            saveWord(word) // Favori değilse ekle
        }
    }

    fun searchWord(word: String){
        if (word.isBlank()) return

        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitInstance.api.getWordMeaning(word)
                wordData = response.firstOrNull()
            } catch (e : Exception){
                wordData = null
            }
            isLoading = false
        }
    }

    suspend fun checkIsFavorite(word: String): Boolean = dao.isWordFavorite(word)
}