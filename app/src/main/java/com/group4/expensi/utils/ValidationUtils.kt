package com.group4.expensi.utils

import android.util.Patterns

fun isValidEmail(email: String): Boolean {
    return email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun validatePassword(password: String): String? {
    return when {
        password.isBlank() -> "Password cannot be empty"
        password.length < 8 -> "Password must be at least 8 characters"
        !password.any { it.isUpperCase() } -> "Must contain at least one uppercase letter"
        !password.any { it.isLowerCase() } -> "Must contain at least one lowercase letter"
        !password.any { it.isDigit() } -> "Must contain at least one number"
        !password.any { !it.isLetterOrDigit() } -> "Must contain at least one special character"
        else -> null
    }
}

fun isFormValid(
    email: String,
    password: String,
    emailError: String?,
    passwordError: String?,
    cnfPasswordError: String?
): Boolean {
    return email.isNotBlank() &&
            password.isNotBlank() &&
            emailError == null &&
            passwordError == null &&
            cnfPasswordError == null
}