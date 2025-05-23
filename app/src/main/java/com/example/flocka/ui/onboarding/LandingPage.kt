package com.example.flocka.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont.Provider
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flocka.R
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.alexandriaFontFamily
import com.example.flocka.ui.components.sansationFontFamily

@Composable
fun LandingPage(onLoginClick: () -> Unit, onSignUpClick: () -> Unit){

    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        Image(
            painter = painterResource(id = R.drawable.ic_landingbg),
            contentDescription = "Background Image",
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer { scaleX =1.1f }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 263.dp)
                .background(
                    BluePrimary,
                    shape = RoundedCornerShape(topStart = 150.dp)
                ),
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = 89.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Welcome to Flocka",
                        color = Color.White,
                        fontFamily = alexandriaFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        modifier = Modifier
                            .padding(horizontal = 68.dp)
                    )
                    Text(
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                        color = Color(0xFFD1D0D0),
                        textAlign = TextAlign.Justify,
                        fontFamily = sansationFontFamily,
                        fontSize = 15.sp,
                        modifier = Modifier
                            .offset(y = 38.dp)
                            .padding(horizontal = 80.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-89).dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    val imageWidthFraction = 0.55f

                    Image(
                        painter = painterResource(id = R.drawable.ic_sign_up_button),
                        contentDescription = "Sign Up",
                        modifier = Modifier
                            .fillMaxWidth(imageWidthFraction)
                            .clickable { onSignUpClick() }
                    )

                    Image(
                        painter = painterResource(id = R.drawable.ic_login_button),
                        contentDescription = "Login",
                        modifier = Modifier
                            .fillMaxWidth(imageWidthFraction)
                            .align(Alignment.CenterEnd)
                            .clickable { onLoginClick() }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LandingPagePreview() {
    LandingPage(
        onLoginClick = {},
        onSignUpClick = {}
    )
}
