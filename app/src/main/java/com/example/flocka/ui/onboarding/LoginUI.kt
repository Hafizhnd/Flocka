package com.example.flocka.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.flocka.AuthViewModel
import com.example.flocka.R
import com.example.flocka.User
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.OrangePrimary
import com.example.flocka.ui.components.alexandriaFontFamily
import com.example.flocka.ui.components.sansationFontFamily

@Composable
fun LoginUI(
    authViewModel: AuthViewModel = viewModel(),
    onLoginSuccess: (User, String) -> Unit,
    onSignUpClick: () -> Unit,
    onBackClick: () -> Unit,

) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(authViewModel.loginResult) {
        authViewModel.loginResult?.let { result ->
            isLoading = false
            result.onSuccess { authResponse ->
                val user = authResponse.data?.user
                val token = authResponse.data?.token
                if (user != null && token != null) {
                    onLoginSuccess(user, token)
                } else {
                    showError = "Login succeeded but no user data was returned."
                }
            }
            result.onFailure { exception ->
                showError = exception.message ?: "An unknown error occurred."
            }
            // Reset the result in the ViewModel to prevent re-triggering
            authViewModel.loginResult = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BluePrimary)
    ) {
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
                "Welcome Back",
                fontSize = 26.sp,
                fontFamily = alexandriaFontFamily,
                fontWeight = FontWeight.Bold,
                color = BluePrimary,
                modifier = Modifier.offset(y = 15.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            InputField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                placeholder = "Enter your email"
            )

            Spacer(modifier = Modifier.height(25.dp))

            InputField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                placeholder = "Enter your password",
                isPassword = true,
                passwordVisible = passwordVisible,
                onTogglePassword = { passwordVisible = !passwordVisible }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 33.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = BluePrimary,
                            uncheckedColor = Color.Gray,
                            checkmarkColor = OrangePrimary
                        )
                    )
                    Text("Remember Me", fontSize = 13.sp, fontFamily = sansationFontFamily)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        isLoading = true
                        authViewModel.login(email, password)
                    } else {
                        showError = "Email and Password cannot be empty"
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 33.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BluePrimary,
                    contentColor = Color.White
                )
            ) {
                Text("Log In", fontSize = 20.sp, fontFamily = sansationFontFamily)
            }

            Spacer(modifier = Modifier.height(38.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Don't have an account?", fontFamily = sansationFontFamily)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "Sign Up",
                    color = OrangePrimary,
                    fontWeight = FontWeight.Bold,
                    fontFamily = sansationFontFamily,
                    modifier = Modifier.clickable { onSignUpClick() }
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(top = 25.dp, start = 2.dp)
                .size(width = 100.dp, height = 40.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_rectangle),
                    contentDescription = "Back",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(width = 13.dp, height = 10.dp)
                )
            }
            Text(
                "Back",
                fontFamily = sansationFontFamily,
                fontSize = 13.sp,
                color = Color.White,
                modifier = Modifier.offset(x = (-10).dp, y = 12.dp)
            )
        }
    }

    showError?.let { message ->
        AlertDialog(
            onDismissRequest = { showError = null },
            title = { Text("Login Failed") },
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
fun InputField(
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
            .padding(horizontal = 33.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(fontSize = 13.sp, fontFamily = sansationFontFamily),
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            modifier = Modifier
                .fillMaxWidth()
                .height(43.dp)
                .border(1.dp, Color(0xFFD1D0D0), RoundedCornerShape(15.dp)),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 24.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
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
            }
        )

        Box(
            modifier = Modifier
                .offset(x = 24.dp, y = (-6).dp)
                .background(Color.White)
                .padding(start = 4.dp, end = 3.dp)
        ) {
            Text(label, fontSize = 11.sp, fontFamily = sansationFontFamily)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginUIPreview() {
    LoginUI(
        onLoginSuccess = { _, _ -> },
        onSignUpClick = { },
        onBackClick = { }
    )
}