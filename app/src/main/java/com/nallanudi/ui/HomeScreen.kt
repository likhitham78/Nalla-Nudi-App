package com.nallanudi.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nallanudi.models.GlossaryItem
import com.nallanudi.viewmodel.MainViewModel
import com.nallanudi.viewmodel.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val recentSearches by viewModel.recentSearches.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()
    val bookmarkedItems by viewModel.bookmarkedItems.collectAsState()
    val items by viewModel.filteredGlossary.collectAsState()
    val appLanguage by viewModel.appLanguage.collectAsState()

    val categories = listOf("Science", "Mathematics", "Commerce", "Computer Science", "General")

    Scaffold(
        topBar = {
            if (currentScreen == Screen.HOME) {
                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp)) {
                    Text(
                        text = "Nalla-Nudi",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Developed by Likhitha M",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Text(
                        text = if (appLanguage == "Kannada") "ಭಾಷಾ ಅಂತರವನ್ನು ಕಡಿಮೆ ಮಾಡಿ, ಜ್ಞಾನವನ್ನು ಹೆಚ್ಚಿಸಿ" else "Bridging Language, Empowering Knowledge",
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                    )
                }
            } else {
                CenterAlignedTopAppBar(
                    title = { 
                        Text(
                            text = when(currentScreen) {
                                Screen.LIST -> "My List"
                                Screen.FLASHCARDS -> "Flashcards"
                                Screen.SETTINGS -> "Settings"
                                else -> "Nalla-Nudi"
                            },
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 0.dp,
                modifier = Modifier.padding(top = 1.dp).drawBehind {
                    drawLine(
                        color = Color.LightGray.copy(alpha = 0.5f),
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = 1f
                    )
                }
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text(if (appLanguage == "Kannada") "ಮುಖಪುಟ" else "Home", style = MaterialTheme.typography.labelSmall) },
                    selected = currentScreen == Screen.HOME,
                    onClick = { viewModel.setScreen(Screen.HOME) },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, selectedTextColor = MaterialTheme.colorScheme.primary)
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                    label = { Text(if (appLanguage == "Kannada") "ನನ್ನ ಪಟ್ಟಿ" else "My List", style = MaterialTheme.typography.labelSmall) },
                    selected = currentScreen == Screen.LIST,
                    onClick = { viewModel.setScreen(Screen.LIST) },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, selectedTextColor = MaterialTheme.colorScheme.primary)
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Layers, contentDescription = null) },
                    label = { Text(if (appLanguage == "Kannada") "ಕಾರ್ಡ್‌ಗಳು" else "Cards", style = MaterialTheme.typography.labelSmall) },
                    selected = currentScreen == Screen.FLASHCARDS,
                    onClick = { viewModel.setScreen(Screen.FLASHCARDS) },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, selectedTextColor = MaterialTheme.colorScheme.primary)
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text(if (appLanguage == "Kannada") "ಸೆಟ್ಟಿಂಗ್ಸ್" else "Settings", style = MaterialTheme.typography.labelSmall) },
                    selected = currentScreen == Screen.SETTINGS,
                    onClick = { viewModel.setScreen(Screen.SETTINGS) },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.primary, selectedTextColor = MaterialTheme.colorScheme.primary)
                )

            }
        }
    ) { padding ->
        when (currentScreen) {
            Screen.HOME -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        SearchBar(
                            query = searchQuery,
                            onQueryChange = { viewModel.onSearch(it) },
                            onSearch = {},
                            active = false,
                            onActiveChange = {},
                            placeholder = { Text("Search 500+ technical terms...") },
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                            shape = MaterialTheme.shapes.extraLarge,
                            colors = SearchBarDefaults.colors(containerColor = Color.White)
                        ) {}
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = selectedCategory == null,
                                onClick = { viewModel.onCategorySelect(null) },
                                label = { Text("All Subjects") },
                                shape = MaterialTheme.shapes.large,
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = Color.White
                                )
                            )
                            categories.forEach { cat ->
                                val chipColor = when(cat) {
                                    "Science" -> Color(0xFF0EA5E9)
                                    "Mathematics" -> Color(0xFF8B5CF6)
                                    "Commerce" -> Color(0xFFEAB308)
                                    "Computer Science" -> Color(0xFF3B82F6)
                                    else -> Color(0xFF6366F1)
                                }
                                FilterChip(
                                    selected = selectedCategory == cat,
                                    onClick = { viewModel.onCategorySelect(cat) },
                                    label = { Text(cat) },
                                    shape = MaterialTheme.shapes.large,
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = chipColor,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }


                    if (searchQuery.isEmpty()) {
                        if (recentSearches.isNotEmpty() && selectedCategory == null) {
                            item {
                                Text("Recent Searches", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    recentSearches.forEach { item ->
                                        SuggestionChip(
                                            onClick = { viewModel.onSearch(item.term) },
                                            label = { Text(item.term) },
                                            shape = MaterialTheme.shapes.medium
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Text(
                                text = if (selectedCategory != null) "Featured" else "Word of the Day", 
                                style = MaterialTheme.typography.titleMedium, 
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        item {
                            val featuredItem = if (selectedCategory != null) items.firstOrNull() else items.lastOrNull()
                            if (featuredItem != null) {
                                GlossaryCard(
                                    item = featuredItem,
                                    onBookmark = { viewModel.toggleBookmark(featuredItem) },
                                    onClick = { viewModel.onItemClick(featuredItem) },
                                    onPronounce = { viewModel.speak(featuredItem.term) },
                                    isHighlighted = true,
                                    highlightColor = if (selectedCategory != null) MaterialTheme.colorScheme.primary else Color(0xFFFEFCE8)
                                )
                            }
                        }

                        
                        if (selectedCategory != null) {
                            item {
                                Text(
                                    text = "$selectedCategory Glossary", 
                                    style = MaterialTheme.typography.titleMedium, 
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }

                    items(items) { item ->
                        GlossaryCard(
                            item = item,
                            onBookmark = { viewModel.toggleBookmark(item) },
                            onClick = { viewModel.onItemClick(item) },
                            onPronounce = { viewModel.speak(item.term) }
                        )
                    }

                    if (items.isEmpty() && searchQuery.isNotEmpty()) {
                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(40.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
                                Text("No terms found", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("Try a broader keyword or check spelling", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            }
                        }
                    }
                    
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
            Screen.LIST -> {
                LazyColumn(
                    modifier = Modifier.padding(padding).fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item { Text("Your bookmarked terms", style = MaterialTheme.typography.bodyMedium, color = Color.Gray, modifier = Modifier.padding(vertical = 8.dp)) }
                    items(bookmarkedItems) { item ->
                        GlossaryCard(
                            item = item, 
                            onBookmark = { viewModel.toggleBookmark(item) }, 
                            onClick = { viewModel.onItemClick(item) },
                            onPronounce = { viewModel.speak(item.term) }
                        )
                    }
                    if (bookmarkedItems.isEmpty()) {
                        item {
                            Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                                Text("No bookmarks yet", color = Color.Gray)
                            }
                        }
                    }
                }
            }
            Screen.FLASHCARDS -> {
                FlashcardsScreen(items = items)
            }
            Screen.SETTINGS -> {
                SettingsScreen(viewModel = viewModel)
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlossaryCard(item: GlossaryItem, onBookmark: () -> Unit, onClick: () -> Unit, onPronounce: () -> Unit, isHighlighted: Boolean = false, highlightColor: Color = MaterialTheme.colorScheme.primary) {
    val isWordOfDay = isHighlighted && highlightColor == Color(0xFFFEFCE8)
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = if (isHighlighted) highlightColor else Color.White,
            contentColor = if (isWordOfDay) Color.Black else if (isHighlighted) Color.White else Color.Black
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isHighlighted) 8.dp else 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    val badgeColor = when(item.category) {
                        "Science" -> Color(0xFFE0F2FE)
                        "Mathematics" -> Color(0xFFF5F3FF)
                        "Commerce" -> Color(0xFFFEFCE8)
                        "Computer Science" -> Color(0xFFEFF6FF)
                        else -> Color(0xFFF3F4F6)
                    }
                    val badgeTextColor = when(item.category) {
                        "Science" -> Color(0xFF0369A1)
                        "Mathematics" -> Color(0xFF6D28D9)
                        "Commerce" -> Color(0xFFA16207)
                        "Computer Science" -> Color(0xFF1D4ED8)
                        else -> Color(0xFF374151)
                    }

                    Surface(
                        color = if (isHighlighted && !isWordOfDay) Color.White.copy(alpha = 0.2f) else badgeColor,
                        shape = CircleShape
                    ) {
                        Text(
                            text = item.category.uppercase(),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall, 
                            color = if (isHighlighted && !isWordOfDay) Color.White else badgeTextColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(item.term, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = onPronounce, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.VolumeUp, contentDescription = "Pronounce", tint = if (isWordOfDay) Color(0xFFEAB308) else if (isHighlighted) Color.White else MaterialTheme.colorScheme.primary)
                        }
                    }
                    Text(item.kannadaTerm, style = MaterialTheme.typography.titleMedium, color = if (isWordOfDay) Color(0xFFA16207) else if (isHighlighted) Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
                IconButton(onClick = onBookmark) {
                    Icon(
                        imageVector = if (item.isBookmarked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Bookmark",
                        tint = if (item.isBookmarked) Color(0xFFE11D48) else if (isHighlighted && !isWordOfDay) Color.White else Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = if (isHighlighted && !isWordOfDay) Color.White.copy(alpha = 0.1f) else Color(0xFFF3F4F6))
            Spacer(modifier = Modifier.height(12.dp))
            Text(item.kannadaDefinition, style = MaterialTheme.typography.bodyMedium, lineHeight = 20.sp, color = if (isWordOfDay) Color.DarkGray else if (isHighlighted) Color.White.copy(alpha = 0.8f) else Color.DarkGray)
        }
    }
}

