package com.group4.expensi.ui.pages.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.group4.expensi.viewModel.auth.AuthViewModel
import com.group4.expensi.navigation.expensiAppNavigation.ExpensiRoutes

@Composable
fun OTPScreen(
    modifier: Modifier,
    authViewModel: AuthViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current
    var otp by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    // Get verificationId from previous screen
    val verificationId =
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<String>("verificationID")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .padding(24.dp)
                .verticalScroll(scrollState)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Enter OTP",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "We’ve sent a verification code to your phone",
                style = MaterialTheme.typography.bodyMedium
            )

            OutlinedTextField(
                value = otp,
                onValueChange = { otp = it },
                label = { Text("OTP Code") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (verificationId == null) {
                        Toast.makeText(
                            context,
                            "Verification ID not found",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    val credential = PhoneAuthProvider.getCredential(
                        verificationId,
                        otp
                    )

                    Firebase.auth.signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    context,
                                    "Login Successful",
                                    Toast.LENGTH_SHORT
                                ).show()

                                navController.navigate(ExpensiRoutes.HOME.route) {
                                    popUpTo(ExpensiRoutes.LOGIN.route) {
                                        inclusive = true
                                    }
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Invalid OTP",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Verify OTP")
            }
        }
    }
}
