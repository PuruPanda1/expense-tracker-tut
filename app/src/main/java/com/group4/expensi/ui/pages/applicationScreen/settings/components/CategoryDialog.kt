package com.group4.expensi.ui.pages.applicationScreen.settings.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CategoryDialog(
    title: String,
    initialValue: String,
    confirmText: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    errorMessage: String?,
    onNameChange: () -> Unit
) {
    var categoryName by rememberSaveable { mutableStateOf(initialValue) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title ={ Text(title) },
        text ={
            Column(
                modifier=Modifier.fillMaxWidth(),
                verticalArrangement=Arrangement.spacedBy(8.dp)
            ) {
            OutlinedTextField(
                value =categoryName,
                onValueChange = { categoryName = it
                    onNameChange() },

                label ={ Text("Category name") },
                singleLine= true,
                modifier =Modifier.fillMaxWidth(),
                isError=errorMessage!= null
            )
                if (errorMessage!= null) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton ={
            TextButton(
                enabled=categoryName.isNotBlank(),
                onClick={
                    onConfirm(categoryName.trim())
//                    onDismiss()
                }
            ) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


