package com.faj.myb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.faj.myb.screens.LoginScreen
import com.faj.myb.screens.SignUpScreen
import com.faj.myb.screens.SplashScreen
import com.faj.myb.ui.theme.MyBudgetTheme

data object NavLogin
data object NavSignUp
data object NavSplash

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val backStack: SnapshotStateList<Any> = remember { mutableStateListOf<Any>(NavSplash) }

            MyBudgetTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    NavDisplay(
                        backStack = backStack,
                        onBack = { backStack.removeLastOrNull() },
                        entryProvider = { key ->
                            when(key) {
                                is NavSplash -> NavEntry(key) { SplashScreen(backStack) }
                                is NavLogin -> NavEntry(key) { LoginScreen(backStack) }
                                is NavSignUp -> NavEntry(key) { SignUpScreen(backStack) }
                                else -> NavEntry(Unit) { Text("Unknown route") }
                            }
                        }
                    )
                }
            }
        }
    }
}