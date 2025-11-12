package com.faj.myb.screens

import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.faj.myb.model.FinancialReport
import com.faj.myb.model.MonthlySummary
import com.faj.myb.model.TransactionReport
import com.faj.myb.model.TransactionType
import com.faj.myb.viewmodel.FinancialReportViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialReportScreen(viewModel: FinancialReportViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchFinancialReport()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Relatório Financeiro") })
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.error != null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = uiState.error!!)
            }
        } else if (uiState.financialReport != null) {
            FinancialReportContent(
                report = uiState.financialReport!!,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FinancialReportContent(report: FinancialReport, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            SummaryCard(report)
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            MonthlySummaryChart(report.summaryByMonth)
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Text("Transações", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
        }

        try {
            items(report.transactions) {
                TransactionItem(transaction = it)
                Spacer(modifier = Modifier.height(8.dp))
            }
        } catch (e: Throwable) {

        }

    }
}

@Composable
fun SummaryCard(report: FinancialReport) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            SummaryRow("Renda Total", report.totalIncome)
            SummaryRow("Despesa Total", report.totalExpense)
            SummaryRow("Saldo", report.balance, isBalance = true)
            Spacer(modifier = Modifier.height(8.dp))
            SummaryRow("Renda Média", report.averageIncome)
            SummaryRow("Despesa Média", report.averageExpense)
        }
    }
}

@Composable
fun MonthlySummaryChart(summaryByMonth: List<MonthlySummary>) {
    val entriesIncome = summaryByMonth.mapIndexed { index, summary ->
        BarEntry(index.toFloat(), summary.income.toFloat())
    }
    val entriesExpense = summaryByMonth.mapIndexed { index, summary ->
        BarEntry(index.toFloat(), summary.expense.toFloat())
    }

    val incomeDataSet = BarDataSet(entriesIncome, "Renda").apply {
        color = Color.GREEN
    }
    val expenseDataSet = BarDataSet(entriesExpense, "Despesa").apply {
        color = Color.RED
    }

    val groupSpace = 0.4f
    val barSpace = 0.05f
    val barWidth = 0.25f

    val barData = BarData(incomeDataSet, expenseDataSet).apply {
        this.barWidth = barWidth
    }

    val months = summaryByMonth.map { it.month }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Resumo Mensal", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            AndroidView(
                modifier = Modifier.fillMaxWidth().height(300.dp),
                factory = { context ->
                    BarChart(context).apply {
                        data = barData
                        description.isEnabled = false
                        xAxis.valueFormatter = IndexAxisValueFormatter(months)
                        xAxis.position = XAxis.XAxisPosition.BOTTOM
                        xAxis.granularity = 1f
                        xAxis.setCenterAxisLabels(true)
                        axisLeft.axisMinimum = 0f
                        axisRight.isEnabled = false
                        groupBars(0f, groupSpace, barSpace)
                        invalidate()
                    }
                },
                update = { chart ->
                    chart.data = barData
                    (chart.xAxis.valueFormatter as IndexAxisValueFormatter).values = months.toTypedArray()
                    chart.groupBars(0f, groupSpace, barSpace)
                    chart.invalidate()
                }
            )
        }
    }
}


@Composable
fun SummaryRow(label: String, amount: Double?, isBalance: Boolean = false) {
    val finalAmount = amount ?: 0.0
    val color = if (isBalance) {
        when {
            finalAmount > 0.0 -> androidx.compose.ui.graphics.Color.Green
            finalAmount < 0.0 -> androidx.compose.ui.graphics.Color.Red
            else -> androidx.compose.ui.graphics.Color.Unspecified
        }
    } else {
        androidx.compose.ui.graphics.Color.Unspecified
    }

    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(finalAmount),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = color
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionItem(transaction: TransactionReport) {
    val amountColor = if (transaction.type == TransactionType.INCOME) androidx.compose.ui.graphics.Color.Green else androidx.compose.ui.graphics.Color.Red
    val date = LocalDate.parse(transaction.date)
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = transaction.description, fontWeight = FontWeight.Bold)
                Text(text = date.format(formatter), style = MaterialTheme.typography.bodySmall)
            }
            Text(
                text = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(transaction.amount),
                color = amountColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
