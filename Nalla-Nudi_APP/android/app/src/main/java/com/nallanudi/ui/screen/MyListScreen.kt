package com.nallanudi.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nallanudi.ui.component.WordCard
import com.nallanudi.ui.theme.*
import com.nallanudi.viewmodel.MyListViewModel

@Composable
fun MyListScreen(
    viewModel: MyListViewModel,
    onBack: () -> Unit,
    onWordClick: (String) -> Unit,
    onStartFlashcards: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text("My List", style = MaterialTheme.typography.displayMedium, color = Slate900, fontWeight = FontWeight.ExtraBold)
                Text("${state.savedWords.size} saved words", style = MaterialTheme.typography.bodyMedium, color = Slate600)
            }
            Surface(shape = RoundedCornerShape(16.dp), color = Red100, modifier = Modifier.size(48.dp)) {
                Icon(Icons.Filled.Favorite, contentDescription = null, tint = Red600, modifier = Modifier.padding(12.dp).size(24.dp))
            }
        }

        if (state.savedWords.isNotEmpty()) {
            Button(
                onClick = onStartFlashcards,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Green600),
            ) {
                Icon(Icons.Filled.Psychology, contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))
                Text("Start Flashcards", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
            }
        }

        if (state.savedWords.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(top = 56.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Surface(shape = RoundedCornerShape(48.dp), color = Slate100, modifier = Modifier.size(96.dp)) {
                    Icon(Icons.Filled.MenuBook, contentDescription = null, tint = Slate300, modifier = Modifier.padding(24.dp).size(48.dp))
                }
                Spacer(modifier = Modifier.height(14.dp))
                Text("No saved words yet", style = MaterialTheme.typography.headlineMedium, color = Slate900, fontWeight = FontWeight.Bold)
                Text(
                    "Search for words and tap the heart icon\nto save them here for quick review.",
                    style = MaterialTheme.typography.bodyMedium, color = Slate600, textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onBack, shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = Blue600)) {
                    Text("Explore Words", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                itemsIndexed(state.savedWords) { _, word ->
                    WordCard(word = word, onClick = { onWordClick(word.id) }, onToggleSave = { viewModel.toggleSaved(word.id) })
                }
            }
        }
    }
}
