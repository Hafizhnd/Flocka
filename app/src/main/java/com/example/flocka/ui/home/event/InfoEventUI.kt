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
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Paid
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import com.example.flocka.viewmodel.event.EventViewModel
import java.util.Calendar

@Composable
fun InfoEventUI(
    eventId: String,
    token: String,
    eventViewModel: EventViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    Log.d("EventFlow", "InfoEventUI composable launched with eventId: $eventId")

    var showOrderDialog by remember { mutableStateOf(false) }
    var showPaymentDialog by remember { mutableStateOf(false) }
    var showPaymentSuccess by remember { mutableStateOf(false) }

    val isDialogActive = showOrderDialog || showPaymentDialog || showPaymentSuccess

    val selectedEvent by eventViewModel.selectedEvent.collectAsState()
    val errorMessage by eventViewModel.errorMessage.collectAsState()
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = eventId, key2 = token) {
        if (token.isNotBlank() && eventId.isNotBlank()) {
            isLoading = true
            eventViewModel.fetchEventById(token, eventId)
        }
    }

    LaunchedEffect(selectedEvent, errorMessage) {
        if (selectedEvent != null || errorMessage != null) {
            isLoading = false
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {

        if (isLoading && selectedEvent == null) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (errorMessage != null) {
            Text(
                text = "Error: $errorMessage",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Center).padding(16.dp)
            )
        } else {
            selectedEvent?.let { event ->
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
                                model = event.image?.let { RetrofitClient.BASE_URL.removeSuffix("/") + it },
                                contentDescription = event.name,
                                contentScale = ContentScale.FillWidth,
                                placeholder = painterResource(id = R.drawable.seminar1),
                                error = painterResource(id = R.drawable.seminar1),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(360.dp)
                            )
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow),
                                contentDescription = "Back",
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp).offset(x = 32.dp, y = 54.dp)
                                    .clickable(onClick = onBackClick)
                            )
                        }

                        Column(
                            modifier = Modifier.padding(30.dp)
                        ) {
                            Text(
                                text = event.name,
                                fontFamily = sansationFontFamily,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Justify,
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "by ${event.organizerName ?: event.organizerUsername ?: "Unknown"}", // Dynamic
                                fontFamily = sansationFontFamily,
                                color = BluePrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    Icons.Rounded.CalendarToday,
                                    contentDescription = "Calendar",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(19.dp)
                                )

                                Spacer(modifier = Modifier.width(5.dp))

                                Text(
                                    text = eventViewModel.formatEventDisplayDate(event.eventDate)
                                        .ifBlank {
                                            eventViewModel.getDisplayDateFromDateTimeString(event.startTime)
                                        },
                                    fontFamily = sansationFontFamily,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.End,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(end = 8.dp)
                                )

                                Spacer(modifier = Modifier.width(15.dp))

                                Icon(
                                    Icons.Rounded.Schedule,
                                    contentDescription = "Clock",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(19.dp)
                                )

                                val startTime =
                                    eventViewModel.formatEventDisplayTime(event.startTime)
                                val endTime = eventViewModel.formatEventDisplayTime(event.endTime)

                                Spacer(modifier = Modifier.width(5.dp))

                                Text(
                                    text = if (endTime.isNotBlank()) "$startTime - $endTime" else startTime,
                                    fontFamily = sansationFontFamily,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.End,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(end = 8.dp)
                                )

                                Spacer(modifier = Modifier.width(15.dp))

                                Icon(
                                    Icons.Rounded.Paid,
                                    contentDescription = "Cost",
                                    tint = Color(0xFFEDF1F6),
                                    modifier = Modifier.size(19.dp)
                                        .background(Color.Gray, shape = RoundedCornerShape(100.dp))
                                )

                                Spacer(modifier = Modifier.width(5.dp))

                                Text(
                                    text = if (event.cost != null && event.cost > 0) "Rp ${
                                        "%,.0f".format(
                                            event.cost
                                        ).replace(',', '.')
                                    }" else "Free",
                                    fontFamily = sansationFontFamily,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.End,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = "Description",
                                fontWeight = FontWeight.Bold,
                                fontFamily = sansationFontFamily,
                                fontSize = 16.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = event.description ?: "No description available.",
                                fontSize = 14.sp,
                                fontFamily = sansationFontFamily,
                                textAlign = TextAlign.Justify,
                                lineHeight = 20.sp
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
    if (showPaymentDialog && selectedEvent != null) {
        val orderViewModel: OrderViewModel = viewModel()
        PaymentMethodDialog(
            onDismiss = { showPaymentDialog = false },
            onPay = {
                orderViewModel.createOrderForEvent(token, selectedEvent!!)
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
fun SuccessPopup(
    onFinish: () -> Unit
) {
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
                        "Your booking is confirmed!",
                        fontFamily = sansationFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = BluePrimary
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        "We’ve sent your booking details via email. Bring your excitement and we’ll handle the rest.",
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
                    onClick = {onFinish()},
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



@Composable
fun PaymentMethodDialog(
    onDismiss: () -> Unit,
    onPay: () -> Unit
) {
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
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
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
                            painter = painterResource(R.drawable.img_event_payment),
                            contentDescription = "order summary",
                            modifier = Modifier
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        PaymentMethods()
                    }

                    Spacer(modifier = Modifier.height(15.dp))
                    Button(
                        onClick = { onPay() },
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
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailsDialog(
    onDismiss: () -> Unit,
    onNext: () -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf(0) }
    var date by remember { mutableStateOf("") }
    var promoCode by remember { mutableStateOf("") }

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
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onDismiss() },
                        tint = BluePrimary
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Details Order",
                            fontSize = 20.sp,
                            color = BluePrimary,
                            fontWeight = FontWeight.Bold,
                            fontFamily = sansationFontFamily
                        )
                    }
                }

                Spacer(modifier = Modifier.height(7.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Fill this form for the details order",
                        fontSize = 12.sp,
                        color = OrangePrimary,
                        fontFamily = sansationFontFamily,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                Text("E-mail", fontFamily = sansationFontFamily, fontSize= 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 7.dp))
                BasicTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(1.dp, Color(0xFFD1D0D0), RoundedCornerShape(10.dp))
                        .padding(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black)
                )
                Spacer(modifier = Modifier.height(7.dp))

                Text("Name", fontFamily = sansationFontFamily, fontSize= 14.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(vertical = 7.dp))
                BasicTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(1.dp, Color(0xFFD1D0D0), RoundedCornerShape(10.dp))
                        .padding(12.dp),
                    textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black)
                )
                Spacer(modifier = Modifier.height(7.dp))
                Text("Phone Number", fontFamily = sansationFontFamily, fontSize= 14.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(vertical = 7.dp))
                BasicTextField(
                    value = phone,
                    onValueChange = { phone = it.filter { char -> char.isDigit() } },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(1.dp, Color(0xFFD1D0D0), RoundedCornerShape(10.dp))
                        .padding(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black)
                )
                Spacer(modifier = Modifier.height(7.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Type", fontFamily = sansationFontFamily, fontSize= 14.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(vertical = 7.dp))
                        BasicTextField(
                            value = type,
                            onValueChange = { type = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(45.dp)
                                .border(1.dp, Color(0xFFD1D0D0), RoundedCornerShape(10.dp))
                                .padding(12.dp),
                            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Duration", fontFamily = sansationFontFamily, fontSize= 14.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(vertical = 7.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(45.dp)
                                .border(1.dp, Color(0xFFD1D0D0), RoundedCornerShape(10.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(
                                Icons.Rounded.Remove,
                                contentDescription = "Decrease",
                                modifier = Modifier.clickable {
                                    if (duration > 0) duration--
                                }
                            )
                            Text("$duration")
                            Icon(
                                Icons.Rounded.Add,
                                contentDescription = "Increase",
                                modifier = Modifier.clickable {
                                    duration++
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(7.dp))
                Text("Date", fontFamily = sansationFontFamily, fontSize= 14.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(vertical = 7.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(1.dp, Color(0xFFD1D0D0), RoundedCornerShape(10.dp))
                        .clickable {
                            DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    date = "$dayOfMonth/${month + 1}/$year"
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }
                        .padding(12.dp)
                ) {
                    Text(
                        text = if (date.isNotEmpty()) date else "Select Date",
                        color = if (date.isNotEmpty()) Color.Black else Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(7.dp))
                Text("Promo Code", fontFamily = sansationFontFamily, fontSize= 14.sp, fontWeight = FontWeight.Bold,modifier = Modifier.padding(vertical = 7.dp))
                BasicTextField(
                    value = promoCode,
                    onValueChange = { promoCode = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(1.dp, Color(0xFFD1D0D0), RoundedCornerShape(10.dp))
                        .padding(12.dp),
                    textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black)
                )

                Spacer(modifier = Modifier.height(15.dp))
                Button(
                    onClick = { onNext() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                    shape = RoundedCornerShape(10.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Text(
                        text = "Next",
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


@Preview(showBackground = true)
@Composable
fun InfoEventPreview() {
    InfoEventUI(
        eventId = "previewEventId",
        token = "previewToken",
        onBackClick = {}
    )
}

@Preview
@Composable
fun OrderDetailsDialogPreview() {
    OrderDetailsDialog(onDismiss = {}, onNext = {})
}

@Preview
@Composable
fun PaymentMethodDialogPreview() {
    PaymentMethodDialog(onDismiss = {}, onPay = {})
}
@Preview
@Composable
fun SuccessPopupPreview() {
    SuccessPopup( onFinish = {})
}