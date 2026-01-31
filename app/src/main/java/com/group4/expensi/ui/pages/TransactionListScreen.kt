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
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensi.R
import com.group4.expensi.data.local.entity.Category
import com.group4.expensi.data.local.entity.PaymentMode
import com.group4.expensi.data.local.entity.Transaction
import com.group4.expensi.ui.components.TopBar
import com.group4.expensi.ui.transaction.SortType
import com.group4.expensi.ui.transaction.TransactionUiState
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
        },
        uiState = uiState,
        viewModel = viewModel
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
    onEditTransaction: (Transaction) -> Unit,
    uiState: TransactionUiState,
    viewModel : TransactionViewModel
) {
    var showFilterSheet by rememberSaveable { mutableStateOf(false) }
    if (showFilterSheet) {
        FilterBottomSheet(
            uiState = uiState,
            categoryMap = categoryMap,
            paymentModeMap = paymentModeMap,
            onSortChange = viewModel::onSortTypeChanged,
            onCategoryChange = viewModel::onCategoryFilterSelected,
            onPaymentChange = viewModel::onPaymentModeFilterSelected,
            onDismiss = { showFilterSheet = false }
        )
    }
    Scaffold(
        topBar = { TopBar(title = "Transactions") },
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (transactions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(32.dp)
                        .offset(y = (-16).dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No transactions yet",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier,
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
                    item {
                        if (!uiState.isEndReached) {
                            LoadMoreFooter(
                                onClick = { viewModel.loadNextPage() }
                            )
                        }
                    }
                }
            }
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {
                FloatingActionButton(
                    onClick = { showFilterSheet = true },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.align(Alignment.BottomStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Filter"
                    )
                }
                FloatingActionButton(
                    onClick = onAddTransaction,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Transaction"
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun <T> FilterDropdown(
    label: String,
    selected: T?,
    options: List<T>,
    optionLabel: (T) -> String,
    onSelect: (T?) -> Unit,
    modifier: Modifier = Modifier,
    allowAll: Boolean = true
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded=!expanded},
        modifier = modifier
    ) {
        TextField(
            value = selected?.let(optionLabel)?:"All",
            onValueChange = {},
            singleLine = true,
            maxLines = 1,
            readOnly = true,
            label = {Text(label)},
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {expanded=false}
        ) {
            if (allowAll){
                DropdownMenuItem(
                    text = {Text("All")},
                    onClick = {
                        onSelect(null)
                        expanded=false
                    }
                )
            }
            options.forEach {
                option-> DropdownMenuItem(
                    text = {Text(optionLabel(option))},
                    onClick = {
                        onSelect(option)
                        expanded=false
                    }
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    uiState: TransactionUiState,
    categoryMap: Map<Long, Category>,
    paymentModeMap: Map<Long, PaymentMode>,
    onSortChange: (SortType) -> Unit,
    onCategoryChange: (Long?) -> Unit,
    onPaymentChange: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Filters",
                style = MaterialTheme.typography.titleMedium
            )
            FilterDropdown(
                label = "Sort by",
                selected = uiState.sortType,
                options = SortType.entries,
                optionLabel = {
                    when (it) {
                        SortType.DATE_DESC -> "Date (Newest)"
                        SortType.DATE_ASC -> "Date (Oldest)"
                        SortType.AMOUNT_DESC -> "Amount (High → Low)"
                        SortType.AMOUNT_ASC -> "Amount (Low → High)"
                    }
                },
                onSelect = { onSortChange(it ?: SortType.DATE_DESC) },
                allowAll = false
            )
            FilterDropdown(
                label = "Category",
                selected = uiState.selectedCategoryId,
                options = categoryMap.keys.toList(),
                optionLabel = { id ->
                    if (id == -1L) "Unknown"
                    else categoryMap[id]?.catTitle ?: "Unknown"
                },
                onSelect = onCategoryChange
            )
            FilterDropdown(
                label = "Payment mode",
                selected = uiState.selectedPaymentModeId,
                options = paymentModeMap.keys.toList(),
                optionLabel = { id ->
                    paymentModeMap[id]?.ptTitle ?: "Unknown"
                },
                onSelect = onPaymentChange
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Done")
            }
        }
    }
}
@Composable
fun LoadMoreFooter(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        OutlinedButton(
            onClick = onClick,
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Load more")
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