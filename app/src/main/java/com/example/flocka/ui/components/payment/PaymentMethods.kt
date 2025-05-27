package com.example.flocka.ui.components.payment

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flocka.R
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.alexandriaFontFamily

@Composable
fun PaymentMethods() {
    var selectedMethod by remember { mutableStateOf("PayPal") }

    Column {
        Text(
            "Payment Method",
            fontFamily = alexandriaFontFamily,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 11.dp)
        ) {
            PaymentMethodRow(
                label = "PayPal",
                iconRes = R.drawable.ic_paypall,
                selected = selectedMethod == "PayPal",
                onSelected = { selectedMethod = "PayPal" }
            )
            PaymentMethodRow(
                label = "MasterCard",
                iconRes = R.drawable.ic_mastercard,
                selected = selectedMethod == "MasterCard",
                onSelected = { selectedMethod = "MasterCard" }
            )
            PaymentMethodRow(
                label = "GoPay",
                iconRes = R.drawable.ic_gopay,
                selected = selectedMethod == "GoPay",
                onSelected = { selectedMethod = "GoPay" }
            )
            PaymentMethodRow(
                label = "OVO",
                iconRes = R.drawable.ic_ovo,
                selected = selectedMethod == "OVO",
                onSelected = { selectedMethod = "OVO" }
            )
            PaymentMethodRow(
                label = "Bank Transfer",
                iconRes = R.drawable.ic_banktf,
                selected = selectedMethod == "Bank Transfer",
                onSelected = { selectedMethod = "Bank Transfer" }
            )
        }
    }
}

@Composable
fun PaymentMethodRow(
    label: String,
    iconRes: Int,
    selected: Boolean,
    onSelected: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = label,
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.width(13.dp))
        Text(
            label,
            fontFamily = alexandriaFontFamily,
            fontSize = 13.sp,
        )
        Spacer(modifier = Modifier.weight(1f))
        RadioButton(
            selected = selected,
            onClick = onSelected,
            colors = RadioButtonDefaults.colors(
                selectedColor = BluePrimary, // your custom blue
                unselectedColor = Color.Gray
            )
        )
    }
}
