package com.group4.expensi.ui.pages.auth

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.expensi.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.group4.expensi.viewModel.auth.AuthState
import com.group4.expensi.viewModel.auth.AuthViewModel
import com.group4.expensi.navigation.expensiAppNavigation.ExpensiRoutes
import com.group4.expensi.ui.pages.welcomeScreen.components.TopScreenAnimation
import com.group4.expensi.ui.theme.Black
import com.group4.expensi.ui.theme.GrayText
import com.group4.expensi.ui.theme.OnPrimary
import com.group4.expensi.ui.theme.PrimaryBlue
import com.group4.expensi.ui.theme.TranslucentPrimary
import com.group4.expensi.ui.theme.White
import java.util.concurrent.TimeUnit

@Composable
fun PhoneLogin(modifier: Modifier = Modifier,
               authViewModel: AuthViewModel,
               navController: NavHostController
               ) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var phoneError by rememberSaveable { mutableStateOf<String?>(null) }

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to PrimaryBlue,   // very top
                            0.1f to PrimaryBlue,   // top 10%
                            1.0f to Black       // fade to black/white
                        )
                    )
                )
                .padding(horizontal = 32.dp)
                .verticalScroll(scrollState)
                .imePadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            TopScreenAnimation(height = 250.dp)
            Text(
                text = "Expensi - Expense Manager",
                style = MaterialTheme.typography.headlineSmall,
                color = White
            )
            Text(
                text = "Enter number to continue",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))


            OutlinedTextField(
                value = authViewModel.phoneNumber,
                onValueChange = {
                    authViewModel.onPhoneNumberChange(it)
                    phoneError = validatePhoneNumber(it)
                                },
                label = { Text("Phone Number") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone icon"
                    )
                },
                singleLine = true,
                supportingText = {
                    phoneError?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                isError = phoneError != null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryBlue,
                    focusedLabelColor = PrimaryBlue,
                    focusedLeadingIconColor = PrimaryBlue,
                    cursorColor = PrimaryBlue,
                    unfocusedBorderColor = GrayText,
                    unfocusedLabelColor = GrayText,
                    unfocusedLeadingIconColor = GrayText
                ),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    validatePhoneNumberAndSendOTP(authViewModel.phoneNumber, context, authViewModel,navController)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = OnPrimary,
                    disabledContainerColor = TranslucentPrimary,
                    disabledContentColor = OnPrimary.copy(alpha = 0.6f)
                )
            ) {
                Text("Send OTP")
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = {
                    navController.navigate(ExpensiRoutes.LOGIN.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = OnPrimary,
                    disabledContainerColor = TranslucentPrimary,
                    disabledContentColor = OnPrimary.copy(alpha = 0.6f)
                )
            ) {
                Text("Login using Email")
            }

            TextButton(
                onClick = {
                    navController.navigate(ExpensiRoutes.SIGNUP.route)
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(R.string.don_t_have_an_account_signup), color = White)
            }
        }
}

fun validatePhoneNumber(number: String): String? {
    if (!number.startsWith("+91")) {
        return "Phone number must start with +91"
    }

    val digits = number.removePrefix("+91")

    if (digits.length != 10) {
        return "Phone number must contain 10 digits after +91"
    }

    if (!digits.all { it.isDigit() }) {
        return "Phone number must contain only digits"
    }

    return null // valid
}

private fun validatePhoneNumberAndSendOTP(
    phoneNumber: String,
    context: Context,
    authViewModel: AuthViewModel,
    navController: NavHostController
) {
    val options = PhoneAuthOptions.newBuilder(Firebase.auth)
        .setPhoneNumber(phoneNumber)
        .setTimeout(60L, TimeUnit.SECONDS)
        .setActivity(context as Activity)
        .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Firebase.auth.signInWithCredential(credential).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT)
                        authViewModel.updateAuthenticationStatus(AuthState.Authenticated)
                        navController.navigate(ExpensiRoutes.HOME.route) {
                            popUpTo(ExpensiRoutes.LOGIN.route) { inclusive = true }
                        }
                    }
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(context, "OTP Verification Failed", Toast.LENGTH_SHORT)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verificationId, token)
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    "verificationID",
                    verificationId
                )
                navController.navigate("otp")
            }
        }).build()
    PhoneAuthProvider.verifyPhoneNumber(options)
}

