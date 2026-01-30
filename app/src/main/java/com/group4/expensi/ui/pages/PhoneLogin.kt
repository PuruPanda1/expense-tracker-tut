package com.group4.expensi.ui.pages

import android.app.Activity
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.expensi.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.group4.expensi.auth.AuthState
import com.group4.expensi.auth.AuthViewModel
import com.group4.expensi.navigation.ExpensiRoutes
import java.util.concurrent.TimeUnit

@Composable
fun PhoneLogin(modifier: Modifier = Modifier,
               authViewModel: AuthViewModel,
               navController: NavHostController
               ) {
    val authState = authViewModel.authState.observeAsState()
    var phoneNumber by remember { mutableStateOf("") }
    val context = LocalContext.current

//    LaunchedEffect(authState.value) {
//        when (authState.value) {
//            is AuthState.Error -> {
//                Toast.makeText(
//                    context,
//                    (authState.value as AuthState.Error).message,
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//            is AuthState.Authenticated -> {
//                navController.navigate(ExpensiRoutes.HOME.route) {
//                    popUpTo(ExpensiRoutes.LOGIN.route) { inclusive = true }
//                }
//            }
//
//            else -> Unit
//        }
//    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
                text = stringResource(R.string.create_account),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.enter_number),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    val options = PhoneAuthOptions.newBuilder(Firebase.auth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(context as Activity)
                        .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                                Firebase.auth.signInWithCredential(credential).addOnCompleteListener {
                                    if(it.isSuccessful){
                                        Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT)
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
                                navController.currentBackStackEntry?.savedStateHandle?.set("verificationID", verificationId)
                                navController.navigate("otp")
                            }
                        }).build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Send OTP")
            }
            Button(
                onClick = {
                    navController.navigate(ExpensiRoutes.LOGIN.route)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Login using Email")
            }
            TextButton(
                onClick = {
                    navController.navigate(ExpensiRoutes.SIGNUP.route)
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(R.string.don_t_have_an_account_signup))
            }
        }
    }
}

