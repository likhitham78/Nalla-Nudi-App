package com.nallanudi.ui.screen

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nallanudi.ui.theme.*
import com.nallanudi.viewmodel.ProgressViewModel

@Composable
fun ProgressScreen(
    viewModel: ProgressViewModel,
    onBack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
            Text("Progress", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        }

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp),
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Accuracy", style = MaterialTheme.typography.titleMedium, color = Slate600, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(16.dp))
                    val accuracyPct = (state.accuracy * 100).toInt()
                    Surface(shape = RoundedCornerShape(50.dp), color = Blue100, modifier = Modifier.size(120.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("$accuracyPct%", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.ExtraBold, color = Blue600)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("${state.totalCorrect} correct / ${state.totalWrong} wrong", style = MaterialTheme.typography.bodyMedium, color = Slate600)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ProgressStatCard("Total Words", state.totalWords.toString(), Icons.Filled.MenuBook, Blue600, Blue100, Modifier.weight(1f))
                ProgressStatCard("Learned", state.learnedCount.toString(), Icons.Filled.CheckCircle, Green600, Green100, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ProgressStatCard("For Review", state.wordsForReview.toString(), Icons.Filled.Replay, Amber600, Amber100, Modifier.weight(1f))
                ProgressStatCard("Streak", "${state.streak}d", Icons.Filled.LocalFireDepartment, Amber700, Amber100, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Learning Progress", fontWeight = FontWeight.Bold, color = Slate900)
                        Text("${state.learnedCount}/${state.totalWords}", fontWeight = FontWeight.SemiBold, color = Blue600)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { if (state.totalWords > 0) state.learnedCount.toFloat() / state.totalWords.toFloat() else 0f },
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                        color = Green600, trackColor = Slate200,
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ProgressStatCard(
    title: String, value: String, icon: ImageVector,
    color: Color, bgColor: Color, modifier: Modifier,
) {
    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = bgColor), modifier = modifier) {
        Column(modifier = Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.ExtraBold, color = color)
            Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = color)
        }
    }
}
