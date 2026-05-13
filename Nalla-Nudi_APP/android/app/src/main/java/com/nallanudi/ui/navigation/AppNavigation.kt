package com.nallanudi.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nallanudi.ui.screen.*
import com.nallanudi.viewmodel.*
import com.nallanudi.repository.WordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.MainScope

object Routes {
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val SEARCH = "search"
    const val WORD_DETAIL = "word/{wordId}"
    const val MY_LIST = "mylist"
    const val FLASHCARDS = "flashcards?mode={mode}"
    const val PROGRESS = "progress"
    const val SETTINGS = "settings"

    fun wordDetail(wordId: String) = "word/$wordId"
    fun flashcards(mode: String = "all") = "flashcards?mode=$mode"
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    repository: WordRepository,
    showOnboarding: Boolean,
) {
    val factory = ViewModelFactory(repository)
    val startDestination = if (showOnboarding) Routes.ONBOARDING else Routes.HOME

    NavHost(navController = navController, startDestination = startDestination) {

        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onComplete = {
                    kotlinx.coroutines.MainScope().launch { repository.setOnboardingCompleted() }
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                },
            )
        }

        composable(Routes.HOME) {
            val vm: HomeViewModel = viewModel(factory = factory)
            HomeScreen(
                viewModel = vm,
                onSearchClick = { navController.navigate(Routes.SEARCH) },
                onWordClick = { id -> navController.navigate(Routes.wordDetail(id)) },
                onMyListClick = { navController.navigate(Routes.MY_LIST) },
                onFlashcardsClick = { mode -> navController.navigate(Routes.flashcards(mode)) },
                onProgressClick = { navController.navigate(Routes.PROGRESS) },
                onSettingsClick = { navController.navigate(Routes.SETTINGS) },
            )
        }

        composable(Routes.SEARCH) {
            val vm: SearchViewModel = viewModel(factory = factory)
            SearchScreen(
                viewModel = vm,
                onBack = { navController.popBackStack() },
                onWordClick = { id -> navController.navigate(Routes.wordDetail(id)) },
            )
        }

        composable(
            route = Routes.WORD_DETAIL,
            arguments = listOf(navArgument("wordId") { type = NavType.StringType }),
        ) { entry ->
            val wordId = entry.arguments?.getString("wordId") ?: return@composable
            val vm: WordDetailViewModel = viewModel(factory = factory)
            val soundEnabled by repository.soundEnabled.collectAsStateWithLifecycle(initialValue = true)
            WordDetailScreen(
                viewModel = vm,
                wordId = wordId,
                onBack = { navController.popBackStack() },
                soundEnabled = soundEnabled,
            )
        }

        composable(Routes.MY_LIST) {
            val vm: MyListViewModel = viewModel(factory = factory)
            MyListScreen(
                viewModel = vm,
                onBack = { navController.popBackStack() },
                onWordClick = { id -> navController.navigate(Routes.wordDetail(id)) },
                onStartFlashcards = { navController.navigate(Routes.flashcards("saved")) },
            )
        }

        composable(
            route = Routes.FLASHCARDS,
            arguments = listOf(navArgument("mode") {
                type = NavType.StringType
                defaultValue = "all"
            }),
        ) { entry ->
            val mode = entry.arguments?.getString("mode") ?: "all"
            val vm: FlashcardViewModel = viewModel(factory = factory)
            FlashcardScreen(
                viewModel = vm,
                mode = when (mode) {
                    "saved" -> FlashcardMode.SAVED
                    "review" -> FlashcardMode.REVIEW
                    else -> FlashcardMode.ALL
                },
                onBack = { navController.popBackStack() },
            )
        }

        composable(Routes.PROGRESS) {
            val vm: ProgressViewModel = viewModel(factory = factory)
            ProgressScreen(
                viewModel = vm,
                onBack = { navController.popBackStack() },
            )
        }

        composable(Routes.SETTINGS) {
            val vm: SettingsViewModel = viewModel(factory = factory)
            SettingsScreen(
                viewModel = vm,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
