package com.example.flocka.ui.home.event

import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flocka.R
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.OrangePrimary
import com.example.flocka.ui.components.payment.PaymentMethods
import com.example.flocka.ui.components.sansationFontFamily
import java.util.Calendar

@Composable
fun InfoSpaceUI(
    onBackClick: () -> Unit
) {
    var showOrderDialog by remember { mutableStateOf(false) }
    var showPaymentDialog by remember { mutableStateOf(false) }
    var showPaymentSuccess by remember { mutableStateOf(false)}
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFEDF1F6))
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.seminar1),
                    contentDescription = "Webinar Cover",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(360.dp)
                )
                // Tombol back
                IconButton(
                    onClick = { onBackClick },
                    modifier = Modifier
                        .padding(16.dp)
                        .size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow),
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Making 3D Elements for Modern Site Design",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "George Sanchez",
                    color = Color.Blue,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "📅 20 May", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "🕒 10:00 - 12:00", fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Description",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Tombol Book Now
        Button(
            onClick = { showOrderDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9900)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .width(312.dp)
                .height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Book Now", color = Color.White, fontSize = 16.sp)
        }
    }

    // Tampilkan dialog jika showOrderDialog true
    if (showOrderDialog) {
        OrderDetailsDialog(
            onDismiss = { showOrderDialog = false },
            onNext = {
                showOrderDialog = false
                showPaymentDialog = true
            }
        )
    }
    if (showPaymentDialog) {
        PaymentMethodDialog(
            onDismiss = { showPaymentDialog = false },
            onPay = {
                showPaymentDialog = false
                showPaymentSuccess = true
            }
        )
    }
    if (showPaymentSuccess) {
        SuccessPopup(
            onFinish = { showPaymentSuccess = false }
        )
    }
}



@Preview(showBackground = true)
@Composable
fun InfoSpacePreview() {
    InfoSpaceUI(
        onBackClick = {}
    )
}
