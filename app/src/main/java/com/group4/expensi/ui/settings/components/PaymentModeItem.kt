package com.group4.expensi.ui.settings.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable
fun PaymentModeItem(
    title: String,
    description: String,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier=Modifier.fillMaxWidth(),
        horizontalArrangement=Arrangement.SpaceBetween
    ) {
        Column {
            Text(text=title, style=MaterialTheme.typography.bodyLarge)
            Text(
                text=description,
                style=MaterialTheme.typography.bodySmall,
                color=MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        IconButton(onClick=onDeleteClick) {
            Icon(
                imageVector=Icons.Default.Delete,
                contentDescription="Delete payment mode",
                tint=MaterialTheme.colorScheme.error
            )
        }
    }
}
