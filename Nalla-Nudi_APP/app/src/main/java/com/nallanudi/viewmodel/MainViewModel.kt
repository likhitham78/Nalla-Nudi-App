package com.nallanudi.viewmodel

import android.speech.tts.TextToSpeech
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nallanudi.data.GlossaryRepository
import com.nallanudi.data.InitialData
import com.nallanudi.models.GlossaryItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Locale

enum class Screen { HOME, LIST, FLASHCARDS, SETTINGS }

class MainViewModel(private val repository: GlossaryRepository) : ViewModel() {
    private var tts: TextToSpeech? = null
    
    private val _currentScreen = MutableStateFlow(Screen.HOME)
    val currentScreen = _currentScreen.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _isDarkMode = MutableStateFlow<Boolean?>(null) // null means system default
    val isDarkMode = _isDarkMode.asStateFlow()

    private val _appLanguage = MutableStateFlow("Kannada")
    val appLanguage = _appLanguage.asStateFlow()

    private val _remindersEnabled = MutableStateFlow(true)
    val remindersEnabled = _remindersEnabled.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val filteredGlossary = combine(searchQuery, selectedCategory) { query, category ->
        Pair(query, category)
    }.flatMapLatest { (query, category) ->
        repository.searchItems(query, category)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookmarkedItems = repository.bookmarkedItems.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val recentSearches = repository.recentSearches.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    init {
        seedDatabaseIfNeeded()
    }

    private fun seedDatabaseIfNeeded() {
        viewModelScope.launch {
            repository.insertAll(InitialData.items)
        }
    }

    fun onSearch(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelect(category: String?) {
        _selectedCategory.value = category
    }

    fun onItemClick(item: GlossaryItem) {
        viewModelScope.launch {
            repository.markAsSearched(item)
        }
    }

    fun toggleBookmark(item: GlossaryItem) {
        viewModelScope.launch {
            repository.toggleBookmark(item)
        }
    }

    fun setScreen(screen: Screen) {
        _currentScreen.value = screen
    }

    fun toggleDarkMode() {
        _isDarkMode.value = when (_isDarkMode.value) {
            null -> true
            true -> false
            false -> null
        }
    }

    fun toggleLanguage() {
        _appLanguage.value = if (_appLanguage.value == "Kannada") "English" else "Kannada"
    }

    fun toggleReminders() {
        _remindersEnabled.value = !_remindersEnabled.value
    }

    fun shareApp(context: android.content.Context) {
        val sendIntent = android.content.Intent().apply {
            action = android.content.Intent.ACTION_SEND
            putExtra(android.content.Intent.EXTRA_TEXT, "Learn technical terms in Kannada with Nalla-Nudi! Download now: https://play.google.com/store/apps/details?id=com.nallanudi")
            type = "text/plain"
        }
        val shareIntent = android.content.Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    fun rateApp(context: android.content.Context) {
        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
            data = android.net.Uri.parse("https://play.google.com/store/apps/details?id=com.nallanudi")
        }
        context.startActivity(intent)
    }

    fun initTts(context: android.content.Context) {
        if (tts == null) {
            tts = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    tts?.language = Locale.ENGLISH
                }
            }
        }
    }

    fun speak(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onCleared() {
        super.onCleared()
        tts?.stop()
        tts?.shutdown()
    }
}
