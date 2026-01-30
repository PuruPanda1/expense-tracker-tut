package com.group4.expensi.ui.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.expensi.R
import com.group4.expensi.auth.AuthState
import com.group4.expensi.auth.AuthViewModel
import com.group4.expensi.navigation.ExpensiRoutes
import com.group4.expensi.utils.isFormValid
import com.group4.expensi.utils.isValidEmail
import com.group4.expensi.utils.validatePassword

@Composable
fun SignUpPage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }
    var cnfPasswordError by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Error -> {
                Toast.makeText(
                    context,
                    (authState.value as AuthState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            is AuthState.Authenticated -> {
                navController.navigate(ExpensiRoutes.HOME.route) {
                    popUpTo(ExpensiRoutes.SIGNUP.route) { inclusive = true }
                }
            }
            else -> Unit
        }
    }
    Column(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.hello),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.create_an_account_to_track_your_expenses),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = authViewModel.email,
                onValueChange = {
                    authViewModel.onEmailChange(it)
                    emailError = if (isValidEmail(it)) null else "Invalid email address"
                },
                label = { Text("Email") },
                singleLine = true,
                isError = emailError != null,
                supportingText = {
                    emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = authViewModel.password,
                onValueChange = {
                    authViewModel.onPasswordChange(it)
                    passwordError = validatePassword(it)
                },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = passwordError != null,
                supportingText = {
                    passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = authViewModel.cnfPassword,
                onValueChange = {
                    authViewModel.onCnfPasswordChange(it)
                    cnfPasswordError =     if (authViewModel.cnfPassword.isNotEmpty() &&
                        authViewModel.password != it
                    ) "Passwords do not match" else null
                },
                label = { Text("Confirm Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = cnfPasswordError != null,
                supportingText = {
                    cnfPasswordError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { authViewModel.signup() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = isFormValid(
                    authViewModel.email,
                    authViewModel.password,
                    emailError,
                    passwordError,
                    cnfPasswordError
                )

            ) {
                Text("Sign Up")
            }
            TextButton(
                onClick = {
                    navigateToLoginPage(navController)
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(R.string.already_have_an_account_login))
            }
        }
    }
}
private fun navigateToLoginPage(navController: NavHostController) {
    navController.navigate(ExpensiRoutes.LOGIN.route)
}