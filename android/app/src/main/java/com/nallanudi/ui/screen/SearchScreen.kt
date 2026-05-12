package com.nallanudi.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nallanudi.ui.component.SubjectChip
import com.nallanudi.ui.component.WordCard
import com.nallanudi.ui.theme.*
import com.nallanudi.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onBack: () -> Unit,
    onWordClick: (String) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            OutlinedTextField(
                value = state.query,
                onValueChange = { viewModel.onQueryChange(it) },
                modifier = Modifier.weight(1f).focusRequester(focusRequester),
                placeholder = { Text("Search English words...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = Slate300) },
                trailingIcon = {
                    if (state.query.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onQueryChange("") }) {
                            Icon(Icons.Filled.Close, contentDescription = "Clear", tint = Slate300)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Blue600, unfocusedBorderColor = Slate200),
            )
        }

        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(shape = RoundedCornerShape(10.dp), color = Slate100, modifier = Modifier.size(30.dp)) {
                Icon(Icons.Filled.Tune, contentDescription = null, tint = Slate600, modifier = Modifier.padding(7.dp).size(16.dp))
            }
            listOf("Science", "Mathematics", "Commerce").forEach { subject ->
                SubjectChip(subject = subject, isSelected = state.subject == subject, onClick = { viewModel.onSubjectChange(subject) })
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("${state.results.size} results", style = MaterialTheme.typography.bodyMedium, color = Slate600, fontWeight = FontWeight.SemiBold)
            if (state.query.isNotEmpty() || state.subject.isNotEmpty()) {
                TextButton(onClick = { viewModel.onQueryChange(""); viewModel.onSubjectChange("") }) {
                    Text("Clear filters", color = Blue600, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        if (state.results.isEmpty() && !state.isLoading) {
            Column(
                modifier = Modifier.fillMaxSize().padding(top = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Surface(shape = RoundedCornerShape(40.dp), color = Slate100, modifier = Modifier.size(80.dp)) {
                    Icon(Icons.Filled.Search, contentDescription = null, tint = Slate300, modifier = Modifier.padding(16.dp).size(48.dp))
                }
                Spacer(modifier = Modifier.height(14.dp))
                Text("No results found", style = MaterialTheme.typography.headlineMedium, color = Slate900)
                Text("Try a different keyword or remove filters", style = MaterialTheme.typography.bodyMedium, color = Slate600)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                itemsIndexed(state.results) { _, word ->
                    WordCard(word = word, onClick = { onWordClick(word.id) }, onToggleSave = { viewModel.toggleSaved(word.id) })
                }
            }
        }
    }
}
