package com.group4.expensi.ui.pages

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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.expensi.R
import com.group4.expensi.auth.AuthState
import com.group4.expensi.auth.AuthViewModel
import com.group4.expensi.navigation.ExpensiRoutes
import com.group4.expensi.ui.pages.welcomeScreen.components.TopScreenAnimation
import com.group4.expensi.ui.theme.Black
import com.group4.expensi.ui.theme.GrayText
import com.group4.expensi.ui.theme.OffWhite
import com.group4.expensi.ui.theme.OnPrimary
import com.group4.expensi.ui.theme.PrimaryBlue
import com.group4.expensi.ui.theme.TranslucentPrimary
import com.group4.expensi.ui.theme.White
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
    val scrollState = rememberScrollState()

    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }
    var cnfPasswordError by rememberSaveable { mutableStateOf<String?>(null) }

    var passwordVisible by remember { mutableStateOf(false) }
    var cnfPasswordVisible by remember { mutableStateOf(false) }


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
    )
    {
        Spacer(modifier = Modifier.height(32.dp))
        TopScreenAnimation(height = 250.dp)
        Text(
            text = "Expensi - Expense Manager",
            style = MaterialTheme.typography.headlineSmall,
            color = White
        )
        Text(
            text = "Create new account",
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
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email icon"
                )
            },
            singleLine = true,
            isError = emailError != null,
            supportingText = {
                emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            },
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
        OutlinedTextField(
            value = authViewModel.password,
            onValueChange = {
                authViewModel.onPasswordChange(it)
                passwordError = validatePassword(it)
            },
            label = { Text("Password") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Password icon"
                )
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Default.Visibility
                        else
                            Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible)
                            "Hide password"
                        else
                            "Show password"
                    )
                }
            },
            singleLine = true,
            visualTransformation =
                if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
            isError = passwordError != null,
            supportingText = {
                passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            },
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
        OutlinedTextField(
            value = authViewModel.cnfPassword,
            onValueChange = {
                authViewModel.onCnfPasswordChange(it)
                cnfPasswordError = validatePassword(it)
            },
            label = { Text("Password") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Password icon"
                )
            },
            trailingIcon = {
                IconButton(onClick = { cnfPasswordVisible = !cnfPasswordVisible }) {
                    Icon(
                        imageVector = if (cnfPasswordVisible)
                            Icons.Default.Visibility
                        else
                            Icons.Default.VisibilityOff,
                        contentDescription = if (cnfPasswordVisible)
                            "Hide password"
                        else
                            "Show password"
                    )
                }
            },
            singleLine = true,
            visualTransformation =
                if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
            isError = cnfPasswordError != null,
            supportingText = {
                cnfPasswordError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            },
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
                null
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue,
                contentColor = OnPrimary,
                disabledContainerColor = TranslucentPrimary,
                disabledContentColor = OnPrimary.copy(alpha = 0.6f)
            )
        ) {
            Text("Signup")
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                navController.navigate(ExpensiRoutes.PHONE_LOGIN.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = OffWhite,
                contentColor = PrimaryBlue
            ),
        ) {
            Text("Signup using Phone number")
        }
        TextButton(
            onClick = {
                authViewModel.onPasswordChange("")
                authViewModel.onCnfPasswordChange("")
                navigateToLoginPage(navController) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(stringResource(R.string.already_have_an_account_login),  color = PrimaryBlue)
        }
    }
}
private fun navigateToLoginPage(navController: NavHostController) {
    navController.navigate(ExpensiRoutes.LOGIN.route)
}