package com.example.dictionaryapp.view

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dictionaryapp.entity.WordEntity
import com.example.dictionaryapp.utils.playDictionaryAudio
import com.example.dictionaryapp.viewmodel.DictionaryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionaryScreen (viewModel: DictionaryViewModel){
    var searchWord by remember { mutableStateOf("") }
    val wordResult = viewModel.wordData
    var isFavorite by remember { mutableStateOf(false) }
    val favoriteList by viewModel.favoriteWords.collectAsState()

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Dictionary App",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Red
                )
            )
        }
    ) { paddingValues ->
        Column (
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
        ){
            Column (
                modifier = Modifier.fillMaxWidth()
                    .background(Color.Red)
                    .padding(16.dp)
            ){
                OutlinedTextField(
                    value = searchWord,
                    onValueChange = {searchWord = it},
                    placeholder = {
                        Text(
                            text = "Search for a word...",
                            color = Color.DarkGray
                        )
                    },
                    shape = RoundedCornerShape(26.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor =  Color.White,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                    ),
                    trailingIcon = {
                        IconButton(onClick = {
                            viewModel.searchWord(searchWord)
                        }) {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Red)
                        }
                    }
                )
            }
            Column (
                modifier = Modifier.fillMaxSize()
                    .background(Color(0xFFF5F5F5))
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ){
                wordResult?.let { wordData ->
                    val wordMeaning = wordData.meanings.first()

                    MeaningRoundedCard {
                        Row (
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                text = wordData.word,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Red
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            IconButton(
                                onClick = {
                                    viewModel.onFavoriteButtonClicked(
                                        WordEntity(
                                            word = wordData.word,
                                            meaning = wordMeaning.definitions.first().definition
                                        ),
                                        currentlyFavorite = isFavorite
                                    )

                                    isFavorite = !isFavorite
                                }
                            ) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Default.Star else Icons.Default.StarOutline,
                                    contentDescription = "favorite",
                                    tint = if (isFavorite) Color(0xFFFFD700) else Color.Black,
                                    modifier = Modifier.width(24.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row (
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            val context = LocalContext.current
                            IconButton(
                                onClick = {
                                    val validAudio = wordData.phonetics.find { !it.audio.isNullOrBlank() }?.audio

                                    if (validAudio != null) {
                                        playDictionaryAudio(context = context, audioUrl = validAudio)
                                    } else {
                                        android.widget.Toast.makeText(context, "No audio available", android.widget.Toast.LENGTH_SHORT).show()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Audio",
                                    tint = Color.Red,
                                    modifier = Modifier.width(24.dp)
                                )
                            }
                            Text(
                                text = wordData.phonetics.first().text ?: "",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = wordMeaning.partOfSpeech,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = wordMeaning.definitions.first().definition,
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                        }
                    Spacer(modifier = Modifier.height(12.dp))
                    // varsa göstersin
                    val synonymsAvailable = !wordMeaning.synonyms.isNullOrEmpty()
                    val antonymsAvailable = !wordMeaning.antonyms.isNullOrEmpty()

                    if(synonymsAvailable || antonymsAvailable){
                        if(synonymsAvailable){
                            RoundedCard {
                                Text(
                                    text = "Synonyms",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = wordMeaning.synonyms.joinToString(),
                                    color = Color.Black,
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            if(antonymsAvailable){
                                RoundedCard {
                                    Text(
                                        text = "Antonyms",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black,
                                        fontSize = 16.sp
                                    )
                                    Text(
                                        text = wordMeaning.antonyms.joinToString(),
                                        color = Color.Black,
                                    )
                                }

                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    val wordExample = wordMeaning.definitions.firstOrNull()?.example

                    if(!wordExample.isNullOrEmpty()){
                        RoundedCard {
                            Text(
                                text = "Example",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "\"$wordExample\"",
                                fontStyle = FontStyle.Italic,
                                color = Color.Black,
                                fontSize = 16.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
                else{

            }
            }
        }
    }
}

@Composable
fun RoundedCard(content: @Composable ColumnScope.() -> Unit){
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor =  Color.White)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            content = content
        )
    }
}

@Composable
fun MeaningRoundedCard(content: @Composable ColumnScope.() -> Unit){
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor =  Color.White)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            content = content
        )
    }
}