package com.faj.myb.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.faj.myb.model.Transaction
import com.faj.myb.screens.composables.TransactionItem
import com.faj.myb.viewmodel.TransactionsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen() {
    val viewModel: TransactionsViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val addSheetState = rememberModalBottomSheetState()
    val deleteSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showAddBottomSheet by remember { mutableStateOf(false) }
    var transactionToDelete by remember { mutableStateOf<Transaction?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddBottomSheet = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Transaction")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.error != null) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = uiState.error!!)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.fetchTransactions() }) {
                        Text(text = "Tentar novamente")
                    }
                }
            } else {
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    items(uiState.transactions) { transaction ->
                        TransactionItem(
                            transaction = transaction,
                            onClick = { transactionToDelete = transaction }
                        )
                    }
                }
            }
        }
    }

    if (showAddBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAddBottomSheet = false },
            sheetState = addSheetState
        ) {
            AddTransactionSheet {
                viewModel.addTransaction(it)
                scope.launch { addSheetState.hide(); showAddBottomSheet = false }
            }
        }
    }

    if (transactionToDelete != null) {
        ModalBottomSheet(
            onDismissRequest = { transactionToDelete = null },
            sheetState = deleteSheetState
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Deseja excluir esta transação?", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = {
                        transactionToDelete?.let { viewModel.deleteTransaction(it.id) }
                        scope.launch { deleteSheetState.hide(); transactionToDelete = null }
                    }) {
                        Text("Excluir")
                    }
                    Button(onClick = { scope.launch { deleteSheetState.hide(); transactionToDelete = null } }) {
                        Text("Cancelar")
                    }
                }
            }
        }
    }
}