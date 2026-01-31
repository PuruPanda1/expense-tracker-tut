package com.group4.expensi.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.group4.expensi.data.local.entity.Category
import com.group4.expensi.data.local.entity.PaymentMode
import com.group4.expensi.ui.components.TopBar
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryTransactionScreenUI(
    transactionId: Long?,
    amount: String,
    title: String,
    description: String,
    isExpense: Boolean,
    categories: List<Category>,
    selectedCategory: Category?,
    paymentModes: List<PaymentMode>,
    selectedPaymentMode: PaymentMode?,
    onAmountChange: (String) -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onExpenseChange: (Boolean) -> Unit,
    onCategorySelected: (Category) -> Unit,
    onPaymentModeSelected: (PaymentMode) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    selectedDate: Date,
    onDateSelected: (Date) -> Unit,
) {
    val screenTitle = if (transactionId == null) "Add Transaction" else "Edit Transaction"
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate.time,
        selectableDates = PastOrTodaySelectableDates
    )
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onDateSelected(Date(it))
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
    Scaffold(
        topBar = {
            TopBar(
                title = screenTitle,
                showBack = true,
                showCalendar = true,
                onBack = onBack,
                onCalendarClick = { showDatePicker = true }
            )
        }
    ) { padding ->
        EntryTransactionContent(
            modifier = Modifier.padding(padding),
            amount = amount,
            onAmountChange = onAmountChange,
            title = title,
            onTitleChange = onTitleChange,
            description = description,
            onDescriptionChange = onDescriptionChange,
            isExpense = isExpense,
            onExpenseChange = onExpenseChange,
            categories = categories,
            selectedCategory = selectedCategory,
            paymentModes = paymentModes,
            selectedPaymentMode = selectedPaymentMode,
            onCategorySelected = onCategorySelected,
            onPaymentModeSelected = onPaymentModeSelected,
            onSave = onSave,
            selectedDate = selectedDate
        )

    }
}

@Composable
fun EntryTransactionContent(
    modifier: Modifier = Modifier,
    amount: String,
    onAmountChange: (String) -> Unit,
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    isExpense: Boolean,
    onExpenseChange: (Boolean) -> Unit,
    onSave: () -> Unit,
    categories: List<Category>,
    selectedCategory: Category?,
    paymentModes: List<PaymentMode>,
    selectedPaymentMode: PaymentMode?,
    onCategorySelected: (Category) -> Unit,
    onPaymentModeSelected: (PaymentMode) -> Unit,
    selectedDate: Date
) {
    val canSave = validate(amount, title, selectedCategory, selectedPaymentMode)
    Column(
        modifier = modifier
            .fillMaxSize().verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AmountInput(amount, onAmountChange)
                Text(
                    text = remember(selectedDate) {
                        SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                            .format(selectedDate)
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                TitleInput(title, onTitleChange)
                DescriptionInput(description, onDescriptionChange)
                CategoryDropdown(categories, selectedCategory, onCategorySelected)
                ExpenseToggle(isExpense, onExpenseChange)
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PaymentModeSelector(
                paymentModes = paymentModes,
                selectedPaymentMode = selectedPaymentMode,
                onPaymentModeSelected = onPaymentModeSelected
            )
            HelperText()
            SaveButton(
                onClick = onSave,
                enabled = canSave
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun AmountInput(
    value: String,
    onValueChange: (String) -> Unit
) {
    val displayText = if (value.isEmpty()) "0" else value

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "₹",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.width(6.dp))

        BasicTextField(
            value = value,
            onValueChange = {
                input -> val validInput = input.filter { it.isDigit() || it == '.' }.let {
                    if (it.count{c-> c=='.'}<=1) it else value
            }
                onValueChange(validInput)
            },
            singleLine = true,
            textStyle = MaterialTheme.typography.displaySmall.copy(
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Start
            ),
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            modifier = Modifier.width(IntrinsicSize.Min),
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = "0",
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}






@Composable
fun TitleInput(title: String, onTitleChange: (String) -> Unit) {
    TextField(
        value = title,
        onValueChange = onTitleChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("What was this transaction for?") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null
            )
        }
    )
}
@Composable
fun DescriptionInput(description: String, onDescriptionChange: (String) -> Unit) {
    TextField(
        value = description,
        onValueChange = onDescriptionChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Add a note (optional)") },
        maxLines = 2
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    categories: List<Category>,
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedCategory?.catTitle ?: "Choose Category",
            onValueChange = {},
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.catTitle) },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    }
                )
            }
        }
    }

}
@Composable
fun ExpenseToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Is this an expense?")
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentModeSelector(
    paymentModes: List<PaymentMode>,
    selectedPaymentMode: PaymentMode?,
    onPaymentModeSelected: (PaymentMode) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        items(paymentModes) { mode ->
            FilterChip(
                selected = selectedPaymentMode?.ptId == mode.ptId,
                onClick = { onPaymentModeSelected(mode) },
                label = { Text(mode.ptTitle) }
            )
        }
    }
}
@Composable
fun HelperText() {
    Text(
        text = "Note: To change the transaction date, click on the calendar icon",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
@Composable
fun SaveButton(onClick: () -> Unit, enabled: Boolean) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(26.dp),
        enabled = enabled
    ) {
        Text("SAVE TRANSACTION")
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EntryTransactionScreenPreview() {
    val sampleCategories = listOf(
        Category(catId = 1, catTitle = "Food", catIconUrl = "ic_food"),
        Category(catId = 2, catTitle = "Travel", catIconUrl = "ic_fuel"),
        Category(catId = 3, catTitle = "Salary", catIconUrl = "ic_salary")
    )

    val samplePaymentModes = listOf(
        PaymentMode(
            ptId = 1, ptTitle = "Cash",
            ptDescription = "Cash in hand",
            ptStartingBalance = 0f,
            ptIconUrl = "ic_cash"
        ),
        PaymentMode(
            ptId = 2, ptTitle = "Bank",
            ptDescription = "Bank account",
            ptStartingBalance = 0f,
            ptIconUrl = "ic_bank"
        ),
        PaymentMode(
            ptId = 3, ptTitle = "Card",
            ptDescription = "Credit card",
            ptStartingBalance = 0f,
            ptIconUrl = "ic_card"
        ),
        PaymentMode(
            ptId = 4, ptTitle = "BOB",
            ptDescription = "Credit card",
            ptStartingBalance = 0f,
            ptIconUrl = "ic_card"
        ),
        PaymentMode(
            ptId = 5, ptTitle = "AXIS",
            ptDescription = "Credit card",
            ptStartingBalance = 0f,
            ptIconUrl = "ic_card"
        ),
        PaymentMode(
            ptId = 6, ptTitle = "SBI",
            ptDescription = "Credit card",
            ptStartingBalance = 0f,
            ptIconUrl = "ic_card"
        ),
        PaymentMode(
            ptId = 7, ptTitle = "HDFC",
            ptDescription = "Credit card",
            ptStartingBalance = 0f,
            ptIconUrl = "ic_card"
        )


    )

    EntryTransactionScreenUI(
        transactionId = null,
        amount = "450",
        title = "Groceries",
        description = "Vegetables and fruits",
        isExpense = true,
        categories = sampleCategories,
        selectedCategory = sampleCategories.first(),
        paymentModes = samplePaymentModes,
        selectedPaymentMode = samplePaymentModes.first(),
        onAmountChange = {},
        onTitleChange = {},
        onDescriptionChange = {},
        onExpenseChange = {},
        onCategorySelected = {},
        onPaymentModeSelected = {},
        onSave = {},
        onBack = {},
        selectedDate = Date(),
        onDateSelected = {}
    )
}
fun validate(
    amount: String,
    title: String,
    selectedCategory: Category?,
    selectedPaymentMode: PaymentMode?
) = amount.isNotBlank() &&
        amount.toFloatOrNull()?.let { it > 0f } == true &&
        title.isNotBlank()
@OptIn(ExperimentalMaterial3Api::class)
private object PastOrTodaySelectableDates : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val selectedDate =
            Instant.ofEpochMilli(utcTimeMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()

        val today = LocalDate.now()
        val currentMonth = LocalDate.now().withDayOfMonth(1)

        return !selectedDate.isAfter(today) && !selectedDate.isBefore(currentMonth)
    }
}

