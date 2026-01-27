package com.group4.expensi.ui.pages

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.drawscope.Stroke
import com.group4.expensi.data.local.entity.Transaction
import com.group4.expensi.data.model.CategoryExpense

@Composable
fun HomePage(
    homeViewModel: HomeViewModel
) {
    val uiState by homeViewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> LoadingState()
        uiState.error != null -> ErrorState(uiState.error!!)
        else -> HomeContent(uiState)
    }
}

/* -------------------- STATES -------------------- */

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = message)
    }
}

/* -------------------- CONTENT -------------------- */

@Composable
private fun HomeContent(state: HomeUiState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        BalanceCard(state.balance)

        Spacer(modifier = Modifier.height(12.dp))

        IncomeExpenseRow(
            income = state.totalIncome,
            expense = state.totalExpense
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Category-wise Expenses",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExpenseDonutChart(
            data = state.categoryExpense,
            totalBalance = state.balance
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Your Transactions",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        TransactionList(state.transactions)
    }
}

/* -------------------- UI COMPONENTS -------------------- */

@Composable
private fun BalanceCard(balance: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Total Balance",
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = "₹ %.2f".format(balance),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun IncomeExpenseRow(income: Double, expense: Double) {
    Row(modifier = Modifier.fillMaxWidth()) {
        SummaryCard(
            title = "Income",
            amount = income,
            modifier = Modifier.weight(1f)
        )
        SummaryCard(
            title = "Expense",
            amount = expense,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SummaryCard(
    title: String,
    amount: Double,
    modifier: Modifier
) {
    Card(
        modifier = modifier.padding(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title)
            Text(
                text = "₹ %.2f".format(amount),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/* -------------------- PIE CHART -------------------- */

@Composable
fun ExpenseDonutChart(
    data: List<CategoryExpense>,
    totalBalance: Double
) {
    val totalSpent = data.sumOf { it.total }

    if (data.isEmpty() || totalSpent <= 0.0) {
        Text("No category expense data available")
        return
    }


    val colors = listOf(
        Color(0xFFFF5252), // Red
        Color(0xFFFFA726), // Orange
        Color(0xFFFFEB3B), // Yellow
        Color(0xFF66BB6A), // Green
        Color(0xFF42A5F5), // Blue
        Color(0xFFAB47BC)  // Purple
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp),
        contentAlignment = Alignment.Center
    ) {

        // 🔵 DONUT
        Canvas(modifier = Modifier.fillMaxSize()) {
            var startAngle = -90f

            data.forEachIndexed { index, item ->
                val sweepAngle =
                    (item.total / totalSpent * 360).toFloat()

                drawArc(
                    color = colors[index % colors.size],
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = 50f),
                    size = Size(size.width, size.height),
                    topLeft = Offset.Zero
                )

                startAngle += sweepAngle
            }
        }

        // 🔵 CENTER TEXT
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Total Spent",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "₹ %.0f".format(totalSpent),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Balance Left",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "₹ %.0f".format(totalBalance),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // 🔵 LEGEND
    Column {
        data.forEachIndexed { index, item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(colors[index % colors.size], CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${item.catTitle} • ${"%.1f".format((item.total / totalSpent) * 100)}%"
                )
            }
        }
    }
}


/* -------------------- TRANSACTION LIST -------------------- */

@Composable
private fun TransactionList(transactions: List<Transaction>) {
    if (transactions.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No transactions available")
        }
        return
    }

    LazyColumn {
        items(transactions) { transaction ->
            TransactionItem(transaction)
        }
    }
}

@Composable
private fun TransactionItem(transaction: Transaction) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = transaction.tTitle,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = transaction.tDescription,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = if (transaction.tIsExpense)
                    "- ₹%.2f".format(transaction.tAmount)
                else
                    "+ ₹%.2f".format(transaction.tAmount),
                fontWeight = FontWeight.Bold,
                color = if (transaction.tIsExpense)
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.primary
            )
        }
    }
}
