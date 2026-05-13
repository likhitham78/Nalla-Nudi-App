package com.nallanudi.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nallanudi.ui.component.GradientHeader
import com.nallanudi.ui.component.SubjectChip
import com.nallanudi.ui.theme.*
import com.nallanudi.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onSearchClick: () -> Unit,
    onWordClick: (String) -> Unit,
    onMyListClick: () -> Unit,
    onFlashcardsClick: (String) -> Unit,
    onProgressClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        GradientHeader(
            gradientColors = listOf(Blue700, Blue600),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 48.dp, bottom = 24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(
                            text = "\u0CA8\u0CB2\u0CCD\u0CB2-\u0CA8\u0CC1\u0CA1\u0CBF",
                            style = MaterialTheme.typography.displayMedium,
                            color = White,
                            fontWeight = FontWeight.ExtraBold,
                        )
                        Text(
                            text = "Nalla-Nudi \u00B7 Bridge Dictionary",
                            style = MaterialTheme.typography.bodyMedium,
                            color = White.copy(alpha = 0.75f),
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(22.dp),
                        color = White.copy(alpha = 0.2f),
                        modifier = Modifier.size(44.dp),
                    ) {
                        Icon(
                            Icons.Filled.Settings,
                            contentDescription = "Settings",
                            tint = White,
                            modifier = Modifier.padding(10.dp).clickable { onSettingsClick() },
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .offset(y = (-28).dp)
        ) {
            Surface(
                shape = RoundedCornerShape(18.dp),
                shadowElevation = 8.dp,
                color = White,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSearchClick() }
                        .padding(horizontal = 18.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Filled.Search, contentDescription = null, tint = Slate300, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Search English words...", color = Slate300, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 8.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                listOf("Science", "Mathematics", "Commerce").forEach { subject ->
                    SubjectChip(subject = subject, isSelected = false, onClick = { onSearchClick() })
                }
            }

            // Streak badge
            if (state.streak > 0) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Amber100,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Icon(Icons.Filled.LocalFireDepartment, contentDescription = null, tint = Amber600, modifier = Modifier.size(18.dp))
                        Text("${state.streak} day streak", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Amber700)
                    }
                }
            }

            if (state.wordOfTheDay != null) {
                val wotd = state.wordOfTheDay!!
                val gradient = listOf(subjectColor(wotd.subject), subjectColor(wotd.subject).copy(alpha = 0.8f))

                SectionHeader(title = "Word of the Day", icon = Icons.Filled.LocalFireDepartment)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clickable { onWordClick(wotd.id) },
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                ) {
                    Box(modifier = Modifier.background(Brush.horizontalGradient(gradient))) {
                        Column(modifier = Modifier.padding(22.dp)) {
                            Surface(shape = RoundedCornerShape(10.dp), color = White.copy(alpha = 0.25f)) {
                                Text(
                                    text = wotd.subject,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                                    color = White, fontWeight = FontWeight.Bold, fontSize = 11.sp,
                                )
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(text = wotd.english, style = MaterialTheme.typography.displayMedium, color = White, fontWeight = FontWeight.ExtraBold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = wotd.kannada, style = MaterialTheme.typography.titleLarge, color = White.copy(alpha = 0.9f), fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = wotd.explanation, style = MaterialTheme.typography.bodyMedium, color = White.copy(alpha = 0.75f), maxLines = 2)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            SectionHeader(title = "Quick Access")
            Row(
                modifier = Modifier.padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                QuickAccessCard("My List", "${state.savedCount} words", Icons.Filled.Favorite, listOf(Blue100, Blue50), Blue600, Blue700, onMyListClick, Modifier.weight(1f))
                QuickAccessCard("Flashcards", "Practice now", Icons.Filled.Psychology, listOf(Green100, Green50), Green600, Green700, { onFlashcardsClick("all") }, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                QuickAccessCard("Daily Review", "${state.wordsForReview} words", Icons.Filled.Replay, listOf(Amber100, Amber50), Amber600, Amber700, { onFlashcardsClick("review") }, Modifier.weight(1f))
                QuickAccessCard("Progress", "${state.totalWords} total", Icons.Filled.TrendingUp, listOf(Teal100, Teal50), Teal600, Teal700, onProgressClick, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SectionHeader(title: String, icon: ImageVector? = null) {
    Row(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) {
            Surface(shape = RoundedCornerShape(14.dp), color = Amber100, modifier = Modifier.size(28.dp)) {
                Icon(icon, contentDescription = null, tint = Amber600, modifier = Modifier.padding(6.dp).size(16.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
        }
        Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Slate900)
    }
}

@Composable
private fun QuickAccessCard(
    title: String, subtitle: String, icon: ImageVector,
    gradient: List<Color>, iconBgColor: Color, textColor: Color,
    onClick: () -> Unit, modifier: Modifier,
) {
    Card(modifier = modifier.clickable { onClick() }, shape = RoundedCornerShape(18.dp), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Box(modifier = Modifier.background(Brush.horizontalGradient(gradient))) {
            Column(modifier = Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(shape = RoundedCornerShape(14.dp), color = iconBgColor, modifier = Modifier.size(44.dp)) {
                    Icon(icon, contentDescription = null, tint = White, modifier = Modifier.padding(10.dp).size(24.dp))
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = textColor)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = textColor.copy(alpha = 0.7f))
            }
        }
    }
}
