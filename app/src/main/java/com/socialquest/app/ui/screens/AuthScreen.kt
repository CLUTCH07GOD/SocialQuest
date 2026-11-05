package com.socialquest.app.ui.screens

import android.util.Patterns
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.socialquest.app.ui.components.Gap
import com.socialquest.app.ui.components.PrimaryButton
import com.socialquest.app.ui.components.QuestPrimaryButton
import com.socialquest.app.ui.components.QuietButton
import com.socialquest.app.ui.components.ScreenFrame

/** One screen for sign-up and sign-in. TODO: call the auth API inside onSubmit once the backend exists. */
@Composable
fun AuthScreen(
    register: Boolean,
    onBack: () -> Unit,
    onSubmit: (email: String, nickname: String) -> Unit,
    onSwitch: () -> Unit,
) {
    var isLoading by remember { mutableStateOf(false) }
    var email by rememberSaveable { mutableStateOf("") }
    var nickname by rememberSaveable { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var tried by remember { mutableStateOf(false) }

    val emailOk = Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    val passwordOk = if (register) password.length >= 8 else password.isNotEmpty()
    val shape = RoundedCornerShape(14.dp)

    ScreenFrame(onBack = onBack) {
        Gap(16)
        Text(if (register) "Create your account" else "Welcome back", style = MaterialTheme.typography.headlineMedium)
        Gap(8)
        Text(
            "Just an email and a password. We don’t ask for your full name, phone number or location.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Gap(24)

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            isError = tried && !emailOk,
            supportingText = { if (tried && !emailOk) Text("Enter a valid email address") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            shape = shape,
            modifier = Modifier.fillMaxWidth(),
        )
        Gap(8)

        if (register) {
            OutlinedTextField(
                value = nickname,
                onValueChange = { nickname = it },
                label = { Text("Nickname (optional)") },
                supportingText = { Text("What should we call you? A nickname is fine.") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                shape = shape,
                modifier = Modifier.fillMaxWidth(),
            )
            Gap(8)
        }

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            isError = tried && !passwordOk,
            supportingText = {
                if (tried && !passwordOk) Text(if (register) "Use at least 8 characters" else "Enter your password")
            },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        if (showPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                        contentDescription = if (showPassword) "Hide password" else "Show password",
                    )
                }
            },
            shape = shape,
            modifier = Modifier.fillMaxWidth(),
        )
        Gap(24)

        QuestPrimaryButton(
            text = if (register) "Create account" else "Sign in",
            loading = isLoading,
            enabled = emailOk && passwordOk && !isLoading,
            onClick = {
                tried = true

                if (emailOk && passwordOk) {
                    isLoading = true

                    onSubmit(
                        email.trim(),
                        nickname.trim()
                    )

                    isLoading = false
                }
            }
        )

        QuietButton(
            if (register) "I already have an account"
            else "I'm new here",
            onSwitch
        )

        Gap(16)
    }
}

