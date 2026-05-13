package com.nallanudi.ui.screen

import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nallanudi.ui.component.GradientHeader
import com.nallanudi.ui.theme.*
import com.nallanudi.viewmodel.WordDetailViewModel
import java.util.*

@Composable
fun WordDetailScreen(
    viewModel: WordDetailViewModel,
    wordId: String,
    onBack: () -> Unit,
    soundEnabled: Boolean = true,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    LaunchedEffect(wordId) {
        viewModel.loadWord(wordId)
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { tts?.shutdown() }
    }

    val word = state.word
    if (word == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val subjectGradient = listOf(subjectColor(word.subject), subjectColor(word.subject).copy(alpha = 0.8f))

    Column(modifier = Modifier.fillMaxSize()) {
        GradientHeader(gradientColors = subjectGradient, modifier = Modifier.fillMaxWidth()) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = White)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(onClick = { viewModel.toggleSaved() }) {
                            Icon(
                                if (state.isSaved) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "Save",
                                tint = if (state.isSaved) White else White.copy(alpha = 0.7f),
                            )
                        }
                        IconButton(onClick = {
                            if (soundEnabled) tts?.speak(word.english, TextToSpeech.QUEUE_FLUSH, null, null)
                        }) {
                            Icon(Icons.Filled.VolumeUp, contentDescription = "Pronounce", tint = White.copy(alpha = 0.7f))
                        }
                    }
                }

                Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp, bottom = 32.dp)) {
                    Surface(shape = RoundedCornerShape(10.dp), color = White.copy(alpha = 0.25f)) {
                        Text(
                            text = word.subject,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                            color = White, fontWeight = FontWeight.Bold, fontSize = 12.sp,
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(text = word.english, style = MaterialTheme.typography.displayLarge, color = White, fontWeight = FontWeight.ExtraBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = word.kannada, style = MaterialTheme.typography.headlineLarge, color = White.copy(alpha = 0.9f), fontWeight = FontWeight.Bold)
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).offset(y = (-24).dp),
            contentAlignment = Alignment.Center,
        ) {
            Button(
                onClick = { if (soundEnabled) tts?.speak(word.english, TextToSpeech.QUEUE_FLUSH, null, null) },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = subjectColor(word.subject)),
            ) {
                Icon(Icons.Filled.VolumeUp, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Pronounce", fontWeight = FontWeight.Bold)
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(top = 12.dp)
        ) {
            AnimatedVisibility(visible = true, enter = fadeIn() + slideInVertically()) {
                SectionCard(
                    icon = Icons.Filled.MenuBook,
                    iconBgColor = Blue100,
                    iconTint = Blue600,
                    title = "\u0CB5\u0CBF\u0CB5\u0CB0\u0CA3\u0CC6 (Explanation)",
                ) {
                    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = White), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
                        Text(text = word.explanation, modifier = Modifier.padding(18.dp), style = MaterialTheme.typography.bodyLarge, color = Slate900, lineHeight = 26.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            AnimatedVisibility(visible = true, enter = fadeIn() + slideInVertically(initialOffsetY = { it / 4 })) {
                SectionCard(
                    icon = Icons.Filled.ChatBubbleOutline,
                    iconBgColor = Amber100,
                    iconTint = Amber600,
                    title = "\u0C89\u0CA6\u0CBE\u0CB9\u0CB0\u0CA3\u0CC6 (Example)",
                ) {
                    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Amber50), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Box(modifier = Modifier.width(4.dp).fillMaxHeight().clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)).background(Amber600))
                            Column(modifier = Modifier.padding(18.dp)) {
                                Text(text = "\u201C", style = MaterialTheme.typography.displayMedium, color = Amber600, fontWeight = FontWeight.ExtraBold)
                                Text(text = word.example, style = MaterialTheme.typography.bodyLarge, color = Slate900, fontWeight = FontWeight.SemiBold, lineHeight = 26.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { viewModel.toggleSaved() },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = if (state.isSaved) ButtonDefaults.outlinedButtonColors() else ButtonDefaults.buttonColors(containerColor = subjectColor(word.subject)),
            ) {
                Icon(if (state.isSaved) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = if (state.isSaved) "Saved to My List" else "Save to My List", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SectionCard(
    icon: ImageVector,
    iconBgColor: Color,
    iconTint: Color,
    title: String,
    content: @Composable () -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Surface(shape = RoundedCornerShape(10.dp), color = iconBgColor, modifier = Modifier.size(32.dp)) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.padding(8.dp).size(16.dp))
            }
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Slate900)
        }
        Spacer(modifier = Modifier.height(12.dp))
        content()
    }
}
