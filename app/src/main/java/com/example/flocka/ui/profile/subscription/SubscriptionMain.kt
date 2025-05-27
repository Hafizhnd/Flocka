package com.example.flocka.ui.profile.subscription

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flocka.R
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.OrangePrimary
import com.example.flocka.ui.components.alexandriaFontFamily
import com.example.flocka.ui.components.sansationFontFamily
import androidx.compose.material3.AlertDialog // Import for Dialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun SubscriptionMain(
    onBackClick: () -> Unit,
){
    var showPaymentDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F6FC))
            .padding(16.dp)
            .padding(top = 32.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF002366)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Subscription",
                color = Color(0xFF002366),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(R.drawable.img_subs_pack_1),
            contentDescription = "subscription package 1"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box{
            Image(
                painter = painterResource(R.drawable.img_subs_pack_2),
                contentDescription = "subscription package 2"
            )

            Button(
                onClick = { showPaymentDialog = true }, // Show the dialog
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(30.dp)
                    .padding(horizontal = 50.dp)
                    .offset(y = (-30).dp),
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.buttonElevation(0.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Subscribe",
                    color = Color.White,
                    fontFamily = alexandriaFontFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    if (showPaymentDialog) {
        SubscriptionPaymentDialog(onDismiss = { showPaymentDialog = false })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionPaymentDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        content = { SubscriptionPayment(onDismiss = onDismiss) }
    )
}

@Preview
@Composable
fun PreviewSubscriptionMain(){
    SubscriptionMain (
        onBackClick = {}
    )
}