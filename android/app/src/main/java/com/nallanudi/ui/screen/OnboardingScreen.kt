package com.nallanudi.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nallanudi.ui.theme.*

data class OnboardingPage(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val iconBg: Color,
    val title: String,
    val subtitle: String,
    val gradient: List<Color>,
)

private val pages = listOf(
    OnboardingPage(
        icon = Icons.Filled.MenuBook,
        iconBg = Blue600,
        title = "Welcome to Nalla-Nudi",
        subtitle = "Your bridge to understanding English technical terms through Kannada. Learn Science, Mathematics, and Commerce vocabulary with clear meanings and examples.",
        gradient = listOf(Blue700, Blue600),
    ),
    OnboardingPage(
        icon = Icons.Filled.Psychology,
        iconBg = Green600,
        title = "Smart Flashcards",
        subtitle = "Learn with spaced repetition flashcards. Mark words as Easy, Hard, or Again. The app automatically schedules your reviews so you never forget.",
        gradient = listOf(Green700, Green600),
    ),
    OnboardingPage(
        icon = Icons.Filled.TrendingUp,
        iconBg = Amber600,
        title = "Track Your Progress",
        subtitle = "Build daily streaks, track accuracy, and see how many words you've mastered. Consistent practice leads to lasting knowledge.",
        gradient = listOf(Amber700, Amber600),
    ),
)

@Composable
fun OnboardingScreen(onComplete: () -> Unit) {
    var currentPage by remember { mutableIntStateOf(0) }
    val page = pages[currentPage]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(page.gradient)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // Icon
            Surface(
                shape = RoundedCornerShape(32.dp),
                color = White.copy(alpha = 0.2f),
                modifier = Modifier.size(120.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        page.icon,
                        contentDescription = null,
                        tint = White,
                        modifier = Modifier.size(56.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = page.title,
                style = MaterialTheme.typography.displayMedium,
                color = White,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = page.subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = White.copy(alpha = 0.85f),
                textAlign = TextAlign.Center,
                lineHeight = 26.sp,
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Page indicators
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                repeat(pages.size) { index ->
                    Box(
                        modifier = Modifier
                            .height(if (index == currentPage) 8.dp else 6.dp)
                            .width(if (index == currentPage) 24.dp else 6.dp)
                            .background(
                                White.copy(alpha = if (index == currentPage) 1f else 0.4f),
                                RoundedCornerShape(4.dp),
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Button
            Button(
                onClick = {
                    if (currentPage < pages.size - 1) {
                        currentPage++
                    } else {
                        onComplete()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = White),
            ) {
                Text(
                    text = if (currentPage < pages.size - 1) "Next" else "Get Started",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = page.gradient.first(),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    Icons.Filled.ArrowForward,
                    contentDescription = null,
                    tint = page.gradient.first(),
                    modifier = Modifier.size(20.dp),
                )
            }

            if (currentPage > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = { onComplete() }) {
                    Text("Skip", color = White.copy(alpha = 0.7f), fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
