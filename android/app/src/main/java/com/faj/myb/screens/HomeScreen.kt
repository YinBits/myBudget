package com.faj.myb.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.faj.myb.screens.composables.BalanceCard
import com.faj.myb.screens.composables.RecentTransactions
import com.faj.myb.viewmodel.HomeViewModel

@Composable
fun HomeScreen(backStack: SnapshotStateList<Any>) {
    val viewModel: HomeViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.load() }
    
    HomeContent(
        balance = uiState.balance,
        recentTransactions = uiState.recentTransactions,
        backStack = backStack
    )
}

@Composable
fun HomeContent(
    balance: Double,
    recentTransactions: List<com.faj.myb.model.Transaction>,
    backStack: SnapshotStateList<Any>
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Transparent)
                .padding(start = 16.dp)
        ) {
            BalanceCard(balance = balance)
            RecentTransactions(transactions = recentTransactions, backStack = backStack)
        }
    }
}
