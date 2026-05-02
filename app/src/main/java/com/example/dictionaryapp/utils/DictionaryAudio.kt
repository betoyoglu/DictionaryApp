package com.example.dictionaryapp.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log

fun playDictionaryAudio(context: Context, audioUrl: String?) {
    if (audioUrl.isNullOrEmpty()) {
        Log.e("AudioPlayer", "Ses URL'i boş!")
        return
    }

    //  (// ile başlıyorsa https: ekle)
    val formattedUrl = if (audioUrl.startsWith("//")) "https:$audioUrl" else audioUrl

    try {
        val mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(formattedUrl)

            setOnPreparedListener { mp ->
                mp.start()
            }

            setOnCompletionListener { mp ->
                mp.release()
            }

            setOnErrorListener { mp, _, _ ->
                mp.release()
                true
            }

            prepareAsync()
        }
    } catch (e: Exception) {
        Log.e("AudioPlayer", "Ses çalınırken hata oluştu: ${e.message}")
    }
}