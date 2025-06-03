package com.example.flocka.ui.home.event

import android.app.DatePickerDialog
import android.util.Log
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.flocka.R
import com.example.flocka.data.remote.RetrofitClient
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.OrangePrimary
import com.example.flocka.ui.components.payment.PaymentMethods
import com.example.flocka.ui.components.sansationFontFamily
import com.example.flocka.viewmodel.OrderViewModel
import com.example.flocka.viewmodel.space.SpaceViewModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Calendar
import java.util.Locale

@Composable
fun InfoSpaceUI(
    spaceId: String,
    token: String,
    spaceViewModel: SpaceViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    var showOrderDialog by remember { mutableStateOf(false) }
    var showPaymentDialog by remember { mutableStateOf(false) }
    var showPaymentSuccess by remember { mutableStateOf(false) }

    val selectedSpace by spaceViewModel.selectedSpace.collectAsState()
    val errorMessage by spaceViewModel.errorMessage.collectAsState()
    var isLoading by remember { mutableStateOf(true) }

    val isDialogActive = showOrderDialog || showPaymentDialog || showPaymentSuccess

    val symbols = remember { DecimalFormatSymbols(Locale.GERMANY) }
    val idrFormat = remember { DecimalFormat("Rp #,##0", symbols) }

    LaunchedEffect(key1 = spaceId, key2 = token) {
        if (token.isNotBlank() && spaceId.isNotBlank()) {
            spaceViewModel.fetchSpaceById(token, spaceId)
        } else {
            isLoading = false
        }
    }

    LaunchedEffect(selectedSpace, errorMessage){
        isLoading = false
    }


    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (errorMessage != null) {
            Text(
                "Error: $errorMessage",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            selectedSpace?.let { space ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color(0xFFEDF1F6))
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AsyncImage(
                                model = space.image?.let { RetrofitClient.BASE_URL.removeSuffix("/") + it },
                                contentDescription = space.name,
                                contentScale = ContentScale.FillWidth,
                                placeholder = painterResource(id = R.drawable.seminar1), // Replace with a generic space placeholder
                                error = painterResource(id = R.drawable.seminar1),
                                modifier = Modifier.fillMaxWidth().height(300.dp) // Adjusted height
                            )
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow),
                                contentDescription = "Back",
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp).offset(x = 32.dp, y = 54.dp)
                                    .clickable(onClick = onBackClick)
                            )
                        }

                        Column(modifier = Modifier.padding(30.dp)) { // Matched EventUI padding
                            Text(
                                text = space.name, // Dynamic
                                fontFamily = sansationFontFamily,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Justify,
                            )

                            Spacer(modifier = Modifier.height(12.dp))


                            Text(
                                text = space.location ?: "Location not specified",
                                fontFamily = sansationFontFamily,
                                color = BluePrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))


                            Row {
                                val costText = space.costPerHour?.let { cost ->
                                    if (cost > 0.0) "${idrFormat.format(cost)} / Hour" else "Free"
                                } ?: "Price not available"
                                InfoDetailRow(icon = Icons.Rounded.AttachMoney, text = costText)
                                Spacer(modifier = Modifier.width(5.dp))

                                val openingTime = spaceViewModel.formatDisplayTime(space.openingTime)
                                val closingTime = spaceViewModel.formatDisplayTime(space.closingTime)
                                InfoDetailRow(icon = Icons.Rounded.Schedule, text = "$openingTime - $closingTime")
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Text("Description", fontWeight = FontWeight.Bold, fontFamily = sansationFontFamily, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = space.description ?: "No description available.", // Dynamic
                                fontSize = 14.sp, fontFamily = sansationFontFamily, textAlign = TextAlign.Justify, lineHeight = 20.sp
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }


                    Button(
                        onClick = { showOrderDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp)
                            .padding(bottom = 74.dp)
                            .height(35.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "Book Now",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontFamily = sansationFontFamily
                        )
                    }
                }
            }
        }
    }

    if (isDialogActive) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(enabled = true, onClick = {})
        )
    }

    if (showOrderDialog) {
        OrderDetailsDialog(
            onDismiss = { showOrderDialog = false },
            onNext = {
                showOrderDialog = false
                showPaymentDialog = true
            }
        )
    }

    if (showPaymentDialog && selectedSpace != null) {
        val orderViewModel: OrderViewModel = viewModel()
        val calendar = Calendar.getInstance()

        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        val bookedDateStr = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month + 1, year)

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE) // Use current minute
        val startTimeStr = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)

        val durationHoursInt = 1

        PaymentMethodDialog(
            onDismiss = { showPaymentDialog = false },
            onPay = {
                Log.d("InfoSpaceUI", "Attempting to create space order with date: $bookedDateStr, time: $startTimeStr, duration: $durationHoursInt hrs")
                orderViewModel.createOrderForSpace(
                    token = token,
                    space = selectedSpace!!,
                    bookedDate = bookedDateStr,
                    startTime = startTimeStr,
                    durationHours = durationHoursInt
                )
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

@Composable
private fun InfoDetailRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null, // Decorative
            tint = Color.Gray,
            modifier = Modifier.size(19.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            fontFamily = sansationFontFamily,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold, // Made bold for clarity
            color = Color.Gray,
        )
    }
}


@Preview(showBackground = true)
@Composable
fun InfoSpacePreview() {
    InfoSpaceUI(
        spaceId = "previewEventId",
        token = "previewToken",
        onBackClick = {}
    )
}
