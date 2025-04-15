package com.example.flocka.ui.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.flocka.ui.components.sansationFontFamily

@Composable
fun MessageInputBar(
    onSend: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .shadow(
                    elevation = 4.dp,
                    spotColor = Color(0x40000000),
                    ambientColor = Color(0x40000000)
                )
                .fillMaxWidth(0.9f)
                .height(40.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(100.dp)
                )
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BasicTextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = sansationFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                ),
                modifier = Modifier.weight(1f),
                decorationBox = { innerTextField ->
                    if (text.isEmpty()) {
                        Text(
                            text = "Write your message",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = sansationFontFamily,
                                fontWeight = FontWeight.Normal,
                                color = Color(0xFFD1D0D0),
                            )
                        )
                    }
                    innerTextField()
                }
            )


            IconButton(
                onClick = { /* attach file */ },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.AttachFile,
                    contentDescription = "Attach File",
                    tint = Color(0xFF9E9E9E),
                    modifier = Modifier.size(20.dp)
                )
            }

            // Icon 2 - Camera
            IconButton(
                onClick = {
                    if (text.isNotBlank()) {
                        onSend(text)
                        text = ""
                    }
                },
                modifier = Modifier
                    .size(24.dp)
                    .padding(start = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.PhotoCamera,
                    contentDescription = "Camera",
                    tint = Color(0xFF9E9E9E),
                    modifier = Modifier.size(20.dp)
                )
            }

    }
    }
}

@Preview(showBackground = true)
@Composable
fun MessageInputBarPreview() {
    MessageInputBar(onSend = { msg -> println("Send: $msg") })
}
