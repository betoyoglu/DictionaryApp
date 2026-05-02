package com.example.dictionaryapp.entity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Query("SELECT * FROM favorite_words")
    fun getAllFavoriteWords(): Flow<List<WordEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteWord (word: WordEntity)

    @Delete
    suspend fun deleteFavoriteWord(word: WordEntity)

    @Query("SELECT EXISTS(SELECT * FROM favorite_words WHERE word = :word)")
    suspend fun isWordFavorite(word: String): Boolean
}