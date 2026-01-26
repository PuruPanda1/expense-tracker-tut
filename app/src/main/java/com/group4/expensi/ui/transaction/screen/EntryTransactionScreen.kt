package com.group4.expensi.ui.transaction.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.group4.expensi.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryTransactionScreenUI() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Transaction") },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Pick date")
                    }
                }
            )
        }
    ) { padding ->
        EntryTransactionContent(
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun EntryTransactionContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
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
                AmountInput()
                TitleInput()
                DescriptionInput()
                CategoryDropdown()
                ExpenseToggle()
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PaymentModeSelector()
            HelperText()
            SaveButton()
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun AmountInput() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp) ,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = "",
            onValueChange = { },
            modifier = Modifier
                .widthIn(min = 64.dp, max = 110.dp)
                .height(64.dp),
            textStyle = MaterialTheme.typography.headlineLarge.copy(
                textAlign = TextAlign.Start
            ),
            placeholder = {
                Text(
                    text = "0",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            leadingIcon = {
                Text(
                    text = "₹",
                    style = MaterialTheme.typography.headlineLarge
                )
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
    }
}


@Composable
fun TitleInput() {
    TextField(
        value = "",
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("What was this transaction for?") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.EditNote,
                contentDescription = null
            )
        },
        shape = RoundedCornerShape(12.dp)
    )
}
@Composable
fun DescriptionInput() {
    TextField(
        value = "",
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Add a note (optional)") },
        maxLines = 2,
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun CategoryDropdown() {
    TextField(
        value = "Choose a Category",
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        trailingIcon = {
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        },
        shape = RoundedCornerShape(12.dp)
    )
}
@Composable
fun ExpenseToggle() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Is this an expense?")
        Switch(
            checked = true,
            onCheckedChange = {}
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentModeSelector() {
    val modes = listOf("BANK", "CASH", "HDFC", "ICICI")
    var selected by remember { mutableStateOf("BANK") }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        modes.forEach { mode ->
            FilterChip(
                selected = selected == mode,
                onClick = { selected = mode },
                label = { Text(mode) },
                modifier = Modifier.padding(horizontal = 4.dp)
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
fun SaveButton() {
    Button(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(26.dp)
    ) {
        Text("SAVE TRANSACTION")
    }
}
@Preview
@Composable
fun EntryTransactionScreenUIPreview() {
    EntryTransactionScreenUI()
}





