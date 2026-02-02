package com.group4.expensi.ui.pages.applicationScreen.settings.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable
fun PaymentModeDialog(
    title: String,
    initialName: String,
    initialDescription: String,
    isEditMode: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (
        name: String,
        description: String,
        startingBalanceText: String
    ) -> Unit,
    errorMessage: String?,
    onNameChange: () -> Unit,
    onBalanceChange : ()->Unit,
    errorBalanceMessage: String?,
) {
    var name by rememberSaveable { mutableStateOf(initialName) }
    var description by rememberSaveable { mutableStateOf(initialDescription) }
    var balanceText by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange={ name = it
                        onNameChange()},
                    label={ Text("Name") },
                    singleLine=true,
                    modifier=Modifier.fillMaxWidth(),
                    isError=errorMessage != null
                )

                if (errorMessage != null) {
                    Text(
                        text=errorMessage,
                        color=MaterialTheme.colorScheme.error,
                        style=MaterialTheme.typography.bodySmall
                    )
                }

                OutlinedTextField(
                    value=description,
                    onValueChange={ description = it },
                    label={ Text("Description") },
                    singleLine=true,
                    modifier=Modifier.fillMaxWidth()
                )

                if (!isEditMode) {
                    OutlinedTextField(
                        value=balanceText,
                        onValueChange={ balanceText = it
                            onBalanceChange()},
                        label={ Text("Starting balance") },
                        singleLine=true,
                        modifier=Modifier.fillMaxWidth(),
                        isError =errorBalanceMessage != null
                    )
                }
                if (errorBalanceMessage != null) {
                    Text(
                        text = errorBalanceMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled=name.isNotBlank(),
                onClick={
                    onConfirm(
                        name.trim(),
                        description.trim(),
                        balanceText
                    )
//                    onDismiss()
                }
            ) {
                Text(if (isEditMode)"Save" else "Add")
            }
        },
        dismissButton={
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}