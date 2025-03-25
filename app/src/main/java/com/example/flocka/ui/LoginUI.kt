package com.example.flocka.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.GoogleFont.Provider
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flocka.R

@Composable
fun LoginUI(onLoginClick: (String, String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )

    val alexandriaFontFamily = FontFamily(
        androidx.compose.ui.text.font.Font(R.font.alexandria)
    )

    val sansationFontFamily = FontFamily(
        androidx.compose.ui.text.font.Font(R.font.sansation_bold, FontWeight.Bold),
        androidx.compose.ui.text.font.Font(R.font.sansation_bold_italic, FontWeight.Bold, FontStyle.Italic),
        androidx.compose.ui.text.font.Font(R.font.sansation_italic, FontWeight.Normal, FontStyle.Italic),
        androidx.compose.ui.text.font.Font(R.font.sansation_light, FontWeight.Light),
        androidx.compose.ui.text.font.Font(R.font.sansation_light_italic, FontWeight.Light, FontStyle.Italic),
        androidx.compose.ui.text.font.Font(R.font.sansation_regular, FontWeight.Normal),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF172D9D))
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

        Row(
            modifier = Modifier
                .padding(top = 25.dp, start = 2.dp)
                .size(width = 100.dp, height = 40.dp)
        ){
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_rectangle),
                    contentDescription = "rectangle",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(width = 13.dp, height = 10.dp)
                )
            }
            Text(
                "Back",
                fontFamily = sansationFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 13.sp,
                color = Color.White,
                modifier = Modifier.offset(x = (-10).dp, y = 12.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 213.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource(R.drawable.ic_flocka_logo),
                "Flocka",
                modifier = Modifier
                    .size(100.dp)
            )
            Text(
                "Welcome Back",
                fontSize = 26.sp,
                fontFamily = alexandriaFontFamily,
                color = Color(0xFF172D9D),
                modifier = Modifier.offset(y = 15.dp)
            )

            Spacer(modifier = Modifier.height(22.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 33.dp)
                    .offset(y = 12.dp)
            ){
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = {
                        Text(
                            "Enter your email",
                            fontSize = 13.sp,
                            fontFamily = sansationFontFamily,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFFD1D0D0)
                        )},
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 43.dp),
                    shape = RoundedCornerShape(15.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.DarkGray,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.LightGray
                    ),
                )

                Box(
                    modifier = Modifier
                        .offset(x = 24.dp, y = (-6).dp)
                        .background(color = Color.White)
                        .padding(start = 4.dp, end = 3.dp)
                ) {
                    Text(
                        "Email",
                        fontSize = 11.sp,
                        fontFamily = sansationFontFamily,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 33.dp)
                    .offset(y = 20.dp)
            ){
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = {
                        Text(
                            "Enter your password",
                            fontSize = 13.sp,
                            fontFamily = sansationFontFamily,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFFD1D0D0)
                        ) },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    shape = RoundedCornerShape(15.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.DarkGray,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Gray,
                        unfocusedIndicatorColor = Color.LightGray
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 43.dp),
                )

                Box(
                    modifier = Modifier
                        .offset(x = 24.dp, y = (-6).dp)
                        .background(color = Color.White)
                        .padding(start = 4.dp, end = 3.dp)
                ) {
                    Text(
                        "Password",
                        fontSize = 11.sp,
                        fontFamily = sansationFontFamily,
                        fontWeight = FontWeight.Normal
                    )
                }

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 33.dp, vertical = 10.dp)
                    .offset(y = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF172D9D),
                            uncheckedColor = Color.Gray,
                            checkmarkColor = Color(0xFFFF7F00)
                        )
                    )
                    Text(
                        "Remember Me",
                        fontSize = 13.sp,
                        fontFamily = sansationFontFamily,
                        color = Color(0xFFB8B8B8)
                    )
                }
                Text(
                    "Forgot Password?",
                    color = Color(0xFFFF7F00),
                    fontSize = 13.sp,
                    fontFamily = sansationFontFamily,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .offset(x = (-13).dp, y = 17.5.dp)
                )
            }

            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(errorMessage, color = Color.Red, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        onLoginClick(email, password)
                    } else {
                        errorMessage = "Email and Password cannot be empty"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 33.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF172D9D),
                    contentColor = Color.White
                )

            ) {
                Text(
                    "Log In",
                    fontSize = 20.sp,
                    fontFamily = sansationFontFamily,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(39.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 33.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Color.LightGray,
                    thickness = 1.dp
                )
                Text(
                    "Log in with",
                    fontSize = 12.sp,
                    fontFamily = sansationFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFFB8B8B8),
                    modifier = Modifier.padding(horizontal = 23.dp)
                )
                Divider(
                    modifier = Modifier.weight(1f),
                    color = Color.LightGray,
                    thickness = 1.dp
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "Google",
                        tint = Color.Unspecified, // Keeps original icon colors
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_facebook),
                        contentDescription = "Facebook",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_x), // Assuming X logo is stored as ic_x
                        contentDescription = "X (Twitter)",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    "Don't have an account?",
                    color = Color(0xFF172D9D),
                    fontFamily = sansationFontFamily,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    "Sign Up",
                    color = Color(0xFFFF7F00),
                    fontFamily = sansationFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginUIPreview() {
    LoginUI { _, _ -> }
}
