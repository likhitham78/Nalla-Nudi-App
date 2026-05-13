package com.nallanudi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nallanudi.data.AppDatabase
import com.nallanudi.data.GlossaryRepository
import com.nallanudi.ui.HomeScreen
import com.nallanudi.viewmodel.MainViewModel
import com.nallanudi.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val database = AppDatabase.getDatabase(this)
        val repository = GlossaryRepository(database.glossaryDao())
        val factory = MainViewModelFactory(repository)

        setContent {
            val viewModel: MainViewModel = viewModel(factory = factory)
            val isDarkMode by viewModel.isDarkMode.collectAsState()
            
            com.nallanudi.ui.theme.NallaNudiTheme(
                darkTheme = when(isDarkMode) {
                    true -> true
                    false -> false
                    null -> androidx.compose.foundation.isSystemInDarkTheme()
                }
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    viewModel.initTts(this) 
                    HomeScreen(viewModel = viewModel)
                }
            }
        }

    }
}
