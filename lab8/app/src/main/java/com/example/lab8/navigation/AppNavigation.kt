package com.example.lab8.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.lab8.ui.screens.HomeScreen
import com.example.lab8.ui.screens.SignInScreen
import com.google.firebase.auth.FirebaseAuth

sealed class AppScreen {
    data object SignIn : AppScreen()
    data object Home : AppScreen()
}

@Composable
fun MyNavigation(
    auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    var currentScreen by remember(auth.currentUser) {
        mutableStateOf(
            if (auth.currentUser != null) AppScreen.Home else AppScreen.SignIn
        )
    }

    when (currentScreen) {
        AppScreen.SignIn -> {
            SignInScreen(
                auth = auth,
                onSignInSuccess = {
                    currentScreen = AppScreen.Home
                }
            )
        }

        AppScreen.Home -> {
            HomeScreen(
                onSignOut = {
                    auth.signOut()
                    currentScreen = AppScreen.SignIn
                }
            )
        }
    }
}
