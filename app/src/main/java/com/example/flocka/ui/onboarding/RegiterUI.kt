package com.example.flocka.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flocka.viewmodel.auth.AuthViewModel
import com.example.flocka.R
import com.example.flocka.User
import com.example.flocka.ui.components.alexandriaFontFamily
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.OrangePrimary
import com.example.flocka.ui.components.sansationFontFamily


@Composable
fun RegisterUI(
    authViewModel: AuthViewModel = viewModel(),
    onRegisterSuccess: (User, String) -> Unit,
    onLoginClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(authViewModel.registerResult) {
        authViewModel.registerResult?.let { result ->
            isLoading = false

            result.onSuccess { authResponse ->
                val user = authResponse.data?.user
                val token = authResponse.data?.token
                if (user != null && token != null) {
                    onRegisterSuccess(user, token)
                } else {
                    showError = "Registration succeeded but user data was not returned."
                }
            }
            result.onFailure { exception ->
                "An unknown error occurred."
            }
            authViewModel.registerResult = null
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(BluePrimary)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 263.dp)
                .background(
                    Color.White,
                    shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 213.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource(R.drawable.ic_flocka_logo),
                contentDescription = "Flocka",
                modifier = Modifier.size(100.dp)
            )
            Text(
                "Get Started",
                fontSize = 26.sp,
                fontFamily = alexandriaFontFamily,
                fontWeight = FontWeight.Bold,
                color = BluePrimary,
                modifier = Modifier.offset(y = 15.dp)
            )

            Spacer(modifier = Modifier.height(38.dp))

            Input(
                value = username,
                onValueChange = { username = it },
                label = "Username",
                placeholder = "Enter your username"
            )

            Spacer(modifier = Modifier.height(26.dp))

            Input(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                placeholder = "Enter your email",
            )
            Spacer(modifier = Modifier.height(26.dp))

            Input(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                placeholder = "Enter your password",
                isPassword = true,
                passwordVisible = passwordVisible,
                onTogglePassword = { passwordVisible = !passwordVisible }
            )

            Spacer(modifier = Modifier.height(26.dp))

            Input(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Re-enter Password",
                placeholder = "Re-enter your password",
                isPassword = true,
                passwordVisible = passwordVisible,
                onTogglePassword = { passwordVisible = !passwordVisible }
            )

            Spacer(modifier = Modifier.height(38.dp))

            Button(
                onClick = {
                    when {
                        username.isBlank() || email.isBlank() || password.isBlank() -> {
                            showError = "All fields are required"
                        }
                        password != confirmPassword -> {
                            showError = "Passwords do not match"
                        }
                        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                            showError = "Invalid email format"
                        }
                        else -> {
                            isLoading = true
                            authViewModel.register(username, email, password)
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 33.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary, contentColor = Color.White)
            ) {
                Text("Sign Up", fontSize = 20.sp, fontFamily = sansationFontFamily)
            }

            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Already have an account?", fontFamily = sansationFontFamily)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "Log In",
                    fontFamily = sansationFontFamily,
                    color = OrangePrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onLoginClick() }
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(top = 25.dp, start = 2.dp)
                .size(width = 120.dp, height = 40.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_rectangle),
                    contentDescription = "Back",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(width = 20.dp, height = 20.dp)
                )
            }
            Text(
                "Back",
                fontFamily = alexandriaFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.padding(start = 3.dp, top = 8.dp)
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }

    showError?.let { message ->
        AlertDialog(
            onDismissRequest = { showError = null },
            title = { Text("Registration Failed") },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = { showError = null }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun Input(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePassword: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .padding(horizontal = 33.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 13.sp,
                fontFamily = sansationFontFamily,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            ),
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            modifier = Modifier
                .fillMaxSize()
                .border(1.dp, Color(0xFFD1D0D0), RoundedCornerShape(15.dp))
                .padding(horizontal = 24.dp, vertical = 14.dp)
        ) { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            fontSize = 13.sp,
                            fontFamily = sansationFontFamily,
                            color = Color(0xFFD1D0D0)
                        )
                    }
                    innerTextField()
                }

                if (isPassword && onTogglePassword != null) {
                    IconButton(
                        onClick = onTogglePassword,
                        modifier = Modifier.size(24.dp)
                    ) {
                        val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        val desc = if (passwordVisible) "Hide password" else "Show password"
                        Icon(
                            imageVector = icon,
                            contentDescription = desc,
                            tint = Color.Gray
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .offset(x = 16.dp, y = (-8).dp)
                .background(Color.White)
                .padding(horizontal = 4.dp)
        ) {
            Text(
                label,
                fontSize = 13.sp,
                fontFamily = sansationFontFamily,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RegisterUIPreview() {
    RegisterUI(
        onRegisterSuccess = {_, _ ->},
        onLoginClick = {},
        onBackClick = {}
    )
}