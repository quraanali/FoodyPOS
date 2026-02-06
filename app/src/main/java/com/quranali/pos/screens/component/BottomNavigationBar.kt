package com.quranali.pos.screens.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.quranali.pos.R
import com.quranali.pos.screens.Routes


@Composable
fun BottomNavigationBar(navController: NavController) {
    val listOfItems = listOf(
        BottomNavItem(
            Routes.HOME.name,
            R.string.home,
            R.drawable.ic_f1
        ),
        BottomNavItem(
            Routes.TABLES.name,
            R.string.tables,
            R.drawable.ic_tables
        ),
        BottomNavItem(
            Routes.ORDERS.name,
            R.string.orders,
            R.drawable.ic_orders
        ),
        BottomNavItem(
            Routes.MENU.name,
            R.string.menu,
            R.drawable.ic_menu
        ),
        BottomNavItem(
            Routes.SETTINGS.name,
            R.string.settings,
            R.drawable.ic_settings
        ),
    )


    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        listOfItems.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = stringResource(item.titleRes),
                        modifier = Modifier.size(24.dp) // Optional: Control size
                    )
                }, label = { Text(stringResource(item.titleRes)) },
                selected = currentRoute == stringResource(item.titleRes),
                onClick = {
                    navController.navigate(item.route) {
                        // Avoid building up a large stack of destinations
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    @StringRes val titleRes: Int,
    @DrawableRes val iconRes: Int
)