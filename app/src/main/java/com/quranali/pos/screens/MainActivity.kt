package com.quranali.pos.screens

import android.os.Bundle
import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.quranali.pos.screens.component.BottomNavigationBar
import com.quranali.pos.screens.component.MenuScreen
import com.quranali.pos.screens.component.OrdersScreen
import com.quranali.pos.screens.component.SettingsScreen
import com.quranali.pos.screens.component.TablesScreen
import com.quranali.pos.screens.home.HomeScreen
import com.quranali.pos.screens.splash.SplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
        )
        setContent {
            LaunchedEffect(isSystemInDarkTheme()) {
                enableEdgeToEdge()
            }
            InitScaffold()
        }
    }
}

@Composable
fun InitScaffold() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            // show BottomBar just in splash screen
            if (currentRoute != Routes.SPLASH.name) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.SPLASH.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.SPLASH.name) { SplashScreen(navController) }
            composable(Routes.HOME.name) { HomeScreen() }
            composable(Routes.TABLES.name) { TablesScreen(navController) }
            composable(Routes.ORDERS.name) { OrdersScreen(navController) }
            composable(Routes.MENU.name) { MenuScreen(navController) }
            composable(Routes.SETTINGS.name) { SettingsScreen(navController) }
        }
    }
}