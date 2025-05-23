package com.example.flocka.ui.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flocka.R
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.OrangePrimary
import com.example.flocka.ui.components.alexandriaFontFamily
import com.example.flocka.ui.components.sansationFontFamily
import com.yourpackage.ui.screens.BaseScreen
import okhttp3.internal.userAgent

@Composable
fun AddContactUI(
    onBackClick: () -> Unit,
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("Username") }

    BaseScreen {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(105.dp)
                        .background(BluePrimary)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 26.dp, start = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_arrow),
                            contentDescription = "Back",
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { onBackClick() }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            "New Contact",
                            fontFamily = alexandriaFontFamily,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 26.dp)
                ) {
                    Column {
                        Image(
                            painter = painterResource(id = R.drawable.ic_avatar),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(25.dp)
                                .offset(y = 12.dp)
                        )

                        Spacer(modifier = Modifier.height(147.dp))

                        Image(
                            painter = painterResource(
                                id = if (selectedOption == "Phone Number") R.drawable.ic_phone
                                else R.drawable.ic_hashtag
                            ),
                            contentDescription = "icon",
                            modifier = Modifier
                                .size(25.dp)
                                .offset(y = 12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(13.dp))

                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AddContactTypeField(
                            value = firstName,
                            onValueChange = { firstName = it },
                            placeholder = "First Name"
                        )

                        Spacer(modifier = Modifier.height(17.dp))

                        AddContactTypeField(
                            value = lastName,
                            onValueChange = { lastName = it },
                            placeholder = "Last Name"
                        )

                        Spacer(modifier = Modifier.height(17.dp))

                        // Radio Buttons
                        Row {
                            RadioOption(
                                label = "Username",
                                isSelected = selectedOption == "Username",
                                onClick = { selectedOption = "Username" }
                            )

                            Spacer(modifier = Modifier.width(45.dp))

                            RadioOption(
                                label = "Phone Number",
                                isSelected = selectedOption == "Phone Number",
                                onClick = { selectedOption = "Phone Number" }
                            )
                        }

                        Spacer(modifier = Modifier.height(17.dp))
                        if (selectedOption == "Phone Number") {
                            AddContactTypeField(
                                value = phoneNumber,
                                onValueChange = { phoneNumber = it },
                                placeholder = "Phone Number"
                            )
                        } else {
                            AddContactTypeField(
                                value = userName,
                                onValueChange = { userName = it },
                                placeholder = "Username"
                            )
                        }
                    }
                }
            }

            Button(
                onClick = {},
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(35.dp)
                    .padding(horizontal = 40.dp)
                    .offset(y = (-75).dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = OrangePrimary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    "Save",
                    fontSize = 14.sp,
                    fontFamily = sansationFontFamily,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun RadioOption(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .border(1.dp, Color.Black, shape = RoundedCornerShape(10.dp))
                .background(
                    if (isSelected) BluePrimary else Color.Transparent,
                    shape = RoundedCornerShape(10.dp)
                )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            fontSize = 13.sp,
            fontFamily = sansationFontFamily
        )
    }
}

@Composable
fun AddContactTypeField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Enter text"
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = TextStyle(
            fontSize = 13.sp,
            fontFamily = sansationFontFamily,
            fontWeight = FontWeight.Normal,
            color = Color.Black
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(10.dp)),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 15.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        fontSize = 13.sp,
                        fontFamily = sansationFontFamily,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF808183)
                    )
                }
                innerTextField()
            }
        }
    )
}

@Preview
@Composable
fun AddContactPreview(){
    AddContactUI(
        onBackClick = {}
    )
}