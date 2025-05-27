package com.example.flocka.ui.profile.subscription

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.flocka.ui.components.alexandriaFontFamily
import com.example.flocka.ui.components.payment.PaymentMethods
import com.example.flocka.ui.components.sansationFontFamily
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun SubscriptionPayment(
    onDismiss: () -> Unit,
){
    var showSuccessDialog by remember { mutableStateOf(false) }

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
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 34.dp, bottom = 39.dp)
                    .padding(horizontal = 40.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back_task),
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(18.dp)
                            .clickable { onDismiss() }
                    )
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            text = "Payment Summary",
                            fontFamily = sansationFontFamily,
                            fontSize = 20.sp,
                            color = BluePrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(7.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Review your order and pay",
                        fontFamily = sansationFontFamily,
                        fontSize = 12.sp,
                        color = OrangePrimary,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Image(
                        painter = painterResource(R.drawable.img_order_sum),
                        contentDescription = "order summary",
                        modifier = Modifier
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    PaymentMethods()
                }

                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    "Subscription terms",
                    fontFamily = alexandriaFontFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    "Your subscription renews automatically based on the chosen billing cycle. You can cancel anytime through your account settings. Cancellations take effect at the end of the current period. No partial refunds. Pricing and terms may change with prior notice.",
                    fontFamily = alexandriaFontFamily,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Light,
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = Modifier.height(15.dp))
                Button(
                    onClick = { showSuccessDialog = true }, // Show the success dialog
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                    shape = RoundedCornerShape(10.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Text(
                        text = "Pay",
                        color = Color.White,
                        fontFamily = sansationFontFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
    if (showSuccessDialog) {
        SubscriptionSuccessDialog(onDismiss = {
            showSuccessDialog = false
            onDismiss() // Dismiss the payment dialog when success dialog is dismissed
        })
    }
}

// Helper composable to wrap SubscriptionPayment in an AlertDialog
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionSuccessDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        content = { SubscriptionSuccess(onFinishClick = onDismiss) }
    )
}

@Preview
@Composable
fun PreviewSubscriptionPayment(){
    SubscriptionPayment(
        onDismiss = {},
    )
}