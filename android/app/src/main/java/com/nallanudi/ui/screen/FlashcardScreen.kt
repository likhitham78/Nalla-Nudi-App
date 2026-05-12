package com.nallanudi.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nallanudi.ui.theme.*
import com.nallanudi.viewmodel.FlashcardMode
import com.nallanudi.viewmodel.FlashcardViewModel

@Composable
fun FlashcardScreen(
    viewModel: FlashcardViewModel,
    mode: FlashcardMode,
    onBack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(mode) { viewModel.loadDeck(mode) }

    if (state.isFinished) {
        FlashcardCompleteScreen(
            totalCards = state.deck.size,
            knownCount = state.knownCount,
            hardCount = state.hardCount,
            againCount = state.againCount,
            onRestart = { viewModel.restart() },
            onDone = onBack,
        )
        return
    }

    if (state.isDeckEmpty) {
        NoCardsDueScreen(onBack = onBack)
        return
    }

    val currentWord = state.deck.getOrNull(state.currentIndex)
    if (currentWord == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        return
    }

    val accentColor = subjectColor(currentWord.subject)
    val accentLight = subjectLightColor(currentWord.subject)

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
            Text("Flashcards", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Surface(shape = RoundedCornerShape(12.dp), color = Blue100) {
                Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Filled.Bolt, contentDescription = null, tint = Blue600, modifier = Modifier.size(14.dp))
                    Text("${state.currentIndex + 1}/${state.deck.size}", fontWeight = FontWeight.SemiBold, color = Blue600, fontSize = 13.sp)
                }
            }
        }

        LinearProgressIndicator(
            progress = { (state.currentIndex + 1).toFloat() / state.deck.size.toFloat() },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).height(6.dp),
            color = Blue600, trackColor = Slate200,
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text("Tap card to reveal meaning", style = MaterialTheme.typography.bodyMedium, color = Slate600, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(20.dp))

        // Flip card
        val rotation by animateFloatAsState(
            targetValue = if (state.isFlipped) 180f else 0f,
            animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
        )

        val scale by animateFloatAsState(
            targetValue = 1f,
            animationSpec = spring(dampingRatio = 0.8f, stiffness = Spring.StiffnessLow),
        )

        Box(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 28.dp).weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            if (rotation <= 90f) {
                // Front face
                Card(
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.7f)
                        .graphicsLayer {
                            rotationY = rotation
                            cameraDistance = 12f * density
                            scaleX = scale
                            scaleY = scale
                        },
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(28.dp),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Surface(shape = RoundedCornerShape(10.dp), color = accentLight) {
                            Text(text = currentWord.subject, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp), color = accentColor, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(text = currentWord.english, style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.ExtraBold, color = Slate900)
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.size(8.dp).background(accentColor, RoundedCornerShape(4.dp)))
                            Text("Tap to see meaning", color = accentColor, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        }
                    }
                }
            } else {
                // Back face
                Card(
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.7f)
                        .graphicsLayer {
                            rotationY = 180f
                            cameraDistance = 12f * density
                            scaleX = scale
                            scaleY = scale
                        },
                    colors = CardDefaults.cardColors(containerColor = accentColor),
                ) {
                    Column(modifier = Modifier.fillMaxSize().padding(28.dp)) {
                        Surface(shape = RoundedCornerShape(10.dp), color = White.copy(alpha = 0.25f)) {
                            Text(text = currentWord.subject, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp), color = White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(text = currentWord.kannada, style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.ExtraBold, color = White)
                        Spacer(modifier = Modifier.height(18.dp))
                        Text(text = currentWord.explanation, style = MaterialTheme.typography.bodyLarge, color = White.copy(alpha = 0.9f), lineHeight = 26.sp, maxLines = 5)
                    }
                }
            }
        }

        // Flip button or action buttons
        if (!state.isFlipped) {
            Button(
                onClick = { viewModel.flipCard() },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 28.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Text("Flip Card", fontWeight = FontWeight.Bold)
            }
        } else {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 28.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedButton(
                    onClick = { viewModel.markAgain() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Red600),
                ) {
                    Icon(Icons.Filled.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Again", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                OutlinedButton(
                    onClick = { viewModel.markHard() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Amber600),
                ) {
                    Icon(Icons.Filled.HelpOutline, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Hard", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                Button(
                    onClick = { viewModel.markEasy() },
                    modifier = Modifier.weight(1.5f),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Green600),
                ) {
                    Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Easy", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun FlashcardCompleteScreen(
    totalCards: Int,
    knownCount: Int,
    hardCount: Int,
    againCount: Int,
    onRestart: () -> Unit,
    onDone: () -> Unit,
) {
    val pct = if (totalCards > 0) (knownCount * 100 / totalCards) else 0

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Surface(shape = RoundedCornerShape(50.dp), color = Amber100, modifier = Modifier.size(100.dp)) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = Amber600, modifier = Modifier.size(48.dp))
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("Great work!", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.ExtraBold, color = Slate900)
        Text("You reviewed $totalCards words", style = MaterialTheme.typography.bodyLarge, color = Slate600)
        Spacer(modifier = Modifier.height(28.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatBox(count = knownCount, label = "Easy", color = Green600, bgColor = Green100, modifier = Modifier.weight(1f))
            StatBox(count = hardCount, label = "Hard", color = Amber600, bgColor = Amber100, modifier = Modifier.weight(1f))
            StatBox(count = againCount, label = "Again", color = Red600, bgColor = Red100, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(12.dp))
        Surface(shape = RoundedCornerShape(16.dp), color = Blue100) {
            Column(modifier = Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("$pct%", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.ExtraBold, color = Blue600)
                Text("Score", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = Blue600)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onRestart, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Blue600)) {
            Icon(Icons.Filled.Replay, contentDescription = null)
            Spacer(modifier = Modifier.width(10.dp))
            Text("Study Again", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(12.dp))
        TextButton(onClick = onDone) { Text("Done", fontWeight = FontWeight.SemiBold, color = Slate600, fontSize = 16.sp) }
    }
}

@Composable
private fun StatBox(count: Int, label: String, color: Color, bgColor: Color, modifier: Modifier = Modifier) {
    Surface(shape = RoundedCornerShape(16.dp), color = bgColor, modifier = modifier) {
        Column(modifier = Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(count.toString(), style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.ExtraBold, color = color)
            Text(label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = color)
        }
    }
}

@Composable
private fun NoCardsDueScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Surface(shape = RoundedCornerShape(50.dp), color = Green100, modifier = Modifier.size(100.dp)) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Green600, modifier = Modifier.size(48.dp))
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("All caught up!", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.ExtraBold, color = Slate900)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "No cards are due for review right now.\nCome back later to continue learning.",
            style = MaterialTheme.typography.bodyLarge, color = Slate600, textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onBack, shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Blue600)) {
            Text("Go Back", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}
