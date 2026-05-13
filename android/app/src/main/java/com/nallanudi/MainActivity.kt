package com.nallanudi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.nallanudi.ui.navigation.AppNavigation
import com.nallanudi.ui.theme.NallaNudiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as NallaNudiApp

        setContent {
            NallaNudiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val onboardingCompleted by app.repository.onboardingCompleted.collectAsStateWithLifecycle(initialValue = false)
                    val navController = rememberNavController()
                    AppNavigation(
                        navController = navController,
                        repository = app.repository,
                        showOnboarding = !onboardingCompleted,
                    )
                }
            }
        }
    }
}
