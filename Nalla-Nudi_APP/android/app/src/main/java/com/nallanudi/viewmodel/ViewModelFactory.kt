package com.nallanudi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nallanudi.repository.WordRepository

class ViewModelFactory(private val repository: WordRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            HomeViewModel::class.java -> HomeViewModel(repository)
            SearchViewModel::class.java -> SearchViewModel(repository)
            WordDetailViewModel::class.java -> WordDetailViewModel(repository)
            MyListViewModel::class.java -> MyListViewModel(repository)
            FlashcardViewModel::class.java -> FlashcardViewModel(repository)
            ProgressViewModel::class.java -> ProgressViewModel(repository)
            SettingsViewModel::class.java -> SettingsViewModel(repository)
            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        } as T
    }
}
