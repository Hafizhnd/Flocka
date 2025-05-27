package com.example.flocka.ui.profile.subscription

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flocka.R
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.OrangePrimary
import com.example.flocka.ui.components.sansationFontFamily

@Composable
fun SubscriptionSuccess(
    onFinishClick: () -> Unit // This is called when the "Finish" button is clicked
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight(Alignment.Bottom)
            .background(Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(740.dp)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                .background(Color.White),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 34.dp, bottom = 100.dp)
                    .padding(horizontal = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column (horizontalAlignment = Alignment.CenterHorizontally){
                    Image(
                        painter = painterResource(R.drawable.ic_success),
                        contentDescription = "payment success",
                        modifier = Modifier
                            .size(100.dp)
                    )
                    Spacer(modifier = Modifier.height(50.dp))
                    Text(
                        "SUCCESS!!",
                        fontFamily = sansationFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = BluePrimary
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        "Payment Confirmed!",
                        fontFamily = sansationFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = BluePrimary
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        "Your subscription is active. We’ve sent the details to your email; you can manage it anytime you want.",
                        fontFamily = sansationFontFamily,
                        fontSize = 13.sp,
                        color = OrangePrimary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 34.dp, bottom = 100.dp)
                    .padding(horizontal = 40.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(
                    onClick = onFinishClick, // This will call the onDismiss of the SubscriptionSuccessDialog
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                    shape = RoundedCornerShape(10.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Text(
                        text = "Finish",
                        color = Color.White,
                        fontFamily = sansationFontFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewSubscriptionSuccess(){
    SubscriptionSuccess(onFinishClick = {})
}