package com.group4.expensi.ui.pages.homePageFragments

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.group4.expensi.data.local.entity.Transaction
import com.group4.expensi.data.model.CategoryExpense
import com.group4.expensi.ui.home.HomeUiState
import com.group4.expensi.ui.home.HomeViewModel
import com.group4.expensi.ui.theme.*

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel
) {
    val uiState by homeViewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> LoadingState()
        uiState.error != null -> ErrorState(uiState.error!!)
        else -> HomeContent(uiState)
    }
}
@Composable
private fun LoadingState() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = PrimaryBlue)
    }
}

@Composable
private fun ErrorState(message: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message, color = MaterialTheme.colorScheme.error)
    }
}
@Composable
private fun HomeContent(state: HomeUiState) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            BalanceCard(state.balance)
        }

        item {
            IncomeExpenseRow(state.totalIncome, state.totalExpense)
        }

        item {
            SectionTitle("Category-wise Expenses")
        }

        item {
            ExpenseDonutChart(
                data = state.categoryExpense,
                totalBalance = state.balance
            )
        }

        item {
            SectionTitle("Your Transactions")
        }

        items(state.transactions) {
            TransactionItem(it)
        }
    }
}
@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onBackground
    )
}
@Composable
private fun BalanceCard(balance: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = PrimaryBlue),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                "Total Balance",
                color = Ivory,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "₹ %.2f".format(balance),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium,
                color = Ivory
            )
        }
    }
}

@Composable
private fun IncomeExpenseRow(income: Double, expense: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SummaryCard("Income", income, Modifier.weight(1f))
        SummaryCard("Expense", expense, Modifier.weight(1f))
    }
}

@Composable
private fun SummaryCard(title: String, amount: Double, modifier: Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Ivory),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "₹ %.2f".format(amount),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
@Composable
fun ExpenseDonutChart(
    data: List<CategoryExpense>,
    totalBalance: Double
) {
    val totalSpent = data.sumOf { it.total }

    if (data.isEmpty() || totalSpent <= 0) {
        Text("No category expense data available", color = Color.DarkGray)
        return
    }
    val colors = listOf(
        Color(0xFF4F46E5),
        Color(0xFF22C55E),
        Color(0xFFF97316),
        Color(0xFFEF4444),
        Color(0xFF0EA5E9)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp),
        contentAlignment = Alignment.Center
    ) {

        Canvas(modifier = Modifier.fillMaxSize()) {
            var startAngle = -90f

            data.forEachIndexed { index, item ->
                val sweepAngle =
                    ((item.total / totalSpent) * 360).toFloat()

                drawArc(
                    color = colors[index % colors.size],
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(width = 56f)
                )

                startAngle += sweepAngle
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "Total Spent",
                color = Color.DarkGray,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "₹ %.0f".format(totalSpent),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Balance Left",
                color = Color.DarkGray,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "₹ %.0f".format(totalBalance),
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

    Spacer(Modifier.height(12.dp))

    Column {
        data.forEachIndexed { index, item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Box(
                    Modifier
                        .size(12.dp)
                        .background(colors[index % colors.size], CircleShape)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "${item.catTitle} • ${"%.1f".format((item.total / totalSpent) * 100)}%",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun TransactionList(transactions: List<Transaction>) {
    if (transactions.isEmpty()) {
        Text("No transactions available", color = Color.DarkGray)
        return
    }

    LazyColumn {
        items(transactions) {
            TransactionItem(it)
        }
    }
}

@Composable
private fun TransactionItem(transaction: Transaction) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Ivory),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    transaction.tTitle,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    transaction.tDescription,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = if (transaction.tIsExpense)
                    "- ₹%.2f".format(transaction.tAmount)
                else
                    "+ ₹%.2f".format(transaction.tAmount),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                color = if (transaction.tIsExpense)
                    MaterialTheme.colorScheme.error
                else
                    PrimaryBlue
            )
        }
    }
}
