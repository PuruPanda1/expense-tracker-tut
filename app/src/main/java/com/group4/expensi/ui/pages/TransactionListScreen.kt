package com.group4.expensi.ui.pages

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensi.R
import com.group4.expensi.data.local.entity.Category
import com.group4.expensi.data.local.entity.PaymentMode
import com.group4.expensi.data.local.entity.Transaction
import com.group4.expensi.ui.components.TopBar
import com.group4.expensi.ui.transaction.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TransactionListScreen(
    viewModel: TransactionViewModel,
    onAddClick: () -> Unit,
    onEditClick: (Transaction) -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val categoryMap by viewModel.categoryMap.collectAsState()
    val paymentModeMap by viewModel.paymentModeMap.collectAsState()

    TransactionListContent(
        transactions = uiState.transactions,
        categoryMap = categoryMap,
        paymentModeMap = paymentModeMap,
        onAddTransaction = onAddClick,
        onEditTransaction = onEditClick,
        onDeleteTransaction = { transaction ->
            viewModel.deleteTransaction(transaction)
        }
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListContent(
    transactions: List<Transaction>,
    onAddTransaction: () -> Unit,
    categoryMap: Map<Long, Category>,
    paymentModeMap: Map<Long, PaymentMode>,
    onDeleteTransaction: (Transaction) -> Unit,
    onEditTransaction: (Transaction) -> Unit
) {
    Scaffold(
        topBar = { TopBar(title = "Transactions") },
        floatingActionButton = {

            FloatingActionButton(onClick = onAddTransaction,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Transaction"
                )
            }
        }
    ) { paddingValues ->
        if (transactions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(32.dp).offset(y = (-16).dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No transactions yet",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(transactions) { transaction ->
                    SwipeableTransactionItem(
                        transaction = transaction,
                        category = categoryMap[transaction.tCategoryId],
                        paymentMode = paymentModeMap[transaction.tPaymentModeId],
                        onEdit = { onEditTransaction(transaction) },
                        onDelete = { onDeleteTransaction(transaction) }
                    )
                }
            }
        }
    }
}

@Composable
fun MetaChip(text: String) {
    Surface(
        shape = MaterialTheme.shapes.small,
        tonalElevation = 1.dp,
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(
            width = 0.5.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                letterSpacing = 0.5.sp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(
                horizontal = 8.dp,
                vertical = 3.dp
            )
        )
    }
}
private fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("dd MMM", Locale.getDefault())
    return formatter.format(date)
}
@DrawableRes
private fun categoryIconRes(iconKey: String?): Int {
    return when (iconKey) {
        "ic_fuel" -> R.drawable.ic_fuel
        "ic_food" -> R.drawable.ic_food
        "ic_home" -> R.drawable.ic_home
        "ic_salary" -> R.drawable.ic_salary
        "ic_shopping" -> R.drawable.ic_shopping
        else -> R.drawable.ic_other
    }
}



@Composable
fun TransactionCardContent(
    transaction: Transaction,
    category: Category?,
    paymentMode: PaymentMode?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(
                        id = categoryIconRes(category?.catIconUrl)
                    ),
                    contentDescription = category?.catTitle ?: "Category",
                    modifier = Modifier.size(22.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )

            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Row{
                    MetaChip(text = formatDate(transaction.tDate))
                    Spacer(modifier = Modifier.width(6.dp))
                    MetaChip(text = category?.catTitle ?: "Unknown")
                    Spacer(modifier = Modifier.width(6.dp))
                    MetaChip(text = paymentMode?.ptTitle ?: "Cash")
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = transaction.tTitle,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = transaction.tDescription,
                    style = MaterialTheme.typography.bodySmall,
                    //maxLines = 1
                )
            }
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom
            ) {
                val sign = if (transaction.tIsExpense) "-" else "+"
                val amountColor = if (transaction.tIsExpense) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.primary
                }
                Text(
                    text = "$sign ₹ %.2f".format(transaction.tAmount),
                    style = MaterialTheme.typography.titleMedium,
                    color = amountColor
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeBackground(dismissState: DismissState) {
    val direction = dismissState.dismissDirection ?: return
    val icon= when(direction) {
        DismissDirection.StartToEnd -> Icons.Default.Edit
        DismissDirection.EndToStart -> Icons.Default.Delete
    }
    val alignment = when(direction) {
        DismissDirection.StartToEnd -> Alignment.CenterStart
        DismissDirection.EndToStart -> Alignment.CenterEnd
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment = alignment
    ) {
        Icon(
            imageVector = icon,
            contentDescription = icon.name
        )
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableTransactionItem(
    transaction: Transaction,
    category: Category?,
    paymentMode: PaymentMode?,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val dismissState = rememberDismissState (
        confirmStateChange = { value ->
            when(value) {
                DismissValue.DismissedToStart -> {
                    showDeleteDialog = true
                    false
                }
                DismissValue.DismissedToEnd -> {
                    onEdit()
                    false
                }
                else -> false
            }
        }
    )
    SwipeToDismiss(
        state = dismissState,
        background = { SwipeBackground(dismissState) },
        dismissContent = {
            TransactionCardContent(
                transaction = transaction,
                category = category,
                paymentMode = paymentMode
            )
        }
    ) 
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Transaction") },
            text = { Text("Are you sure you want to delete this transaction?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
fun TransactionListContentPreview() {
    val sampleData = listOf(
        Transaction(
            tId = 1L,
            tTitle = "Groceries",
            tDescription = "Vegetables & fruits",
            tAmount = 450f,
            tDate = Date(),
            tIsExpense = true,
            tPaymentModeId = 1L,
            tCategoryId = 9
        ),
        Transaction(
            tId = 2L,
            tTitle = "Salary",
            tDescription = "February salary",
            tAmount = 30000f,
            tDate = Date(),
            tIsExpense = false,
            tPaymentModeId = 1L,
            tCategoryId = 9
        )
    )

    TransactionListContent(
        transactions = sampleData,
        onAddTransaction = {},
        categoryMap = emptyMap(),
        paymentModeMap = emptyMap(),
        onDeleteTransaction = TODO(),
        onEditTransaction = TODO(),
    )
}
