package com.faj.myb.screens.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.vector.ImageVector
import com.faj.myb.NavFinancialReport
import com.faj.myb.NavHome
import com.faj.myb.NavTransactions

sealed class BottomNavItem(val title: String, val icon: ImageVector, val route: Any) {
    object Home : BottomNavItem("Inicio", Icons.Default.Home, NavHome)
    object Transactions : BottomNavItem("Transações", Icons.Default.List, NavTransactions)
    object Reports : BottomNavItem("Relatórios", Icons.Default.Assessment, NavFinancialReport)
}

@Composable
fun BottomNavigationBar(backStack: SnapshotStateList<Any>) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Transactions,
        BottomNavItem.Reports,
    )

    NavigationBar {
        val currentRoute = backStack.lastOrNull()
        items.forEachIndexed { _, item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = item.route == currentRoute,
                onClick = {
                    if (currentRoute != item.route) {
                        backStack.add(item.route)
                    }
                }
            )
        }
    }
}