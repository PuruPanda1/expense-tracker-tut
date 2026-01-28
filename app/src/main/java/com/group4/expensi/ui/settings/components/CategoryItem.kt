package com.group4.expensi.ui.settings.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable
fun CategoryItem(
    title: String,
    onDeleteClick: () -> Unit
) {
    Surface(
        shape=MaterialTheme.shapes.medium,
        tonalElevation=1.dp
    ) {
        Row(
            modifier=Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement=Arrangement.SpaceBetween
        ) {
            Text(
                text=title,
                style=MaterialTheme.typography.bodyLarge
            )

            IconButton(onClick=onDeleteClick) {
                Icon(
                    imageVector=Icons.Default.Delete,
                    contentDescription="Delete category",
                    tint=MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
