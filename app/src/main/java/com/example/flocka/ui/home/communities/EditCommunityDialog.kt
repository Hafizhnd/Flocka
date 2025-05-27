package com.example.flocka.ui.home.communities

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BorderColor
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.sharp.Groups
import androidx.compose.material.icons.sharp.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flocka.R
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.OrangePrimary
import com.example.flocka.ui.components.alexandriaFontFamily
import com.example.flocka.ui.components.sansationFontFamily

@Composable
fun EditCommunityDialog(
    onDismiss: () -> Unit
){

    var communityName by remember { mutableStateOf("") }
    var communityDesc by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth() // Still fill width within its given space
            .height(406.dp)
            // REMOVED: .padding(horizontal = 34.dp)
            .background(Color.White, RoundedCornerShape(10.dp))
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 26.dp, horizontal = 23.dp) // Internal padding remains
        ) {
            Row(
                modifier = Modifier
                    .height(25.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Rounded.Close,
                    contentDescription = "close",
                    tint = BluePrimary,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onDismiss() }
                )

                Spacer(modifier = Modifier.width(26.dp))

                Text(
                    "Edit Community",
                    fontFamily = sansationFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = BluePrimary
                )

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .height(25.dp)
                        .clip(RoundedCornerShape(20.dp)),
                ) {
                    Button(
                        onClick = { /* Handle save logic */ },
                        modifier = Modifier
                            .width(50.dp)
                            .height(25.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                        shape = RoundedCornerShape(10.dp),
                        elevation = ButtonDefaults.buttonElevation(0.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Save",
                            color = Color.White,
                            fontFamily = sansationFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Customize how your community look and make it great!",
                fontFamily = sansationFontFamily,
                fontSize = 10.sp,
                textAlign = TextAlign.Justify,
                color = OrangePrimary
            )

            Spacer(modifier = Modifier.height(15.dp))

            EditCommunityTypeField(
                value = communityName,
                onValueChange = { communityName = it },
                label = "Community Name",
                placeholder = "READER’S HAVEN"
            )

            Spacer(modifier = Modifier.height(15.dp))

            EditCommunityTypeField(
                value = communityDesc,
                onValueChange = { communityDesc = it },
                label = "Community Description",
                placeholder = "Tempat untuk semua pecinta buku! Kita berbagi review, rekomendasi, dan diskusi tentang buku-buku favorit.\nBaca, berbagi, bertumbuh.",
                height = 78.dp,
                multiline = true
            )

            Spacer(modifier = Modifier.height(3.dp))

            Spacer(modifier = Modifier.height(15.dp))

            Row{
                Column {
                    Text(
                        "Community Icon",
                        fontFamily = sansationFontFamily,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Box{
                        Icon(
                            Icons.Sharp.Groups,
                            contentDescription = "Community Icon",
                            tint = Color(0xFFD3DAE4),
                            modifier = Modifier
                                .size(70.dp)
                                .background(Color(0xFF1E3B75))
                        )

                        Image(
                            painter = painterResource(R.drawable.ic_edit),
                            contentDescription = "edit",
                            Modifier
                                .size(16.dp)
                                .offset(x = 5.dp, y = -5.dp)
                                .background(BluePrimary, shape = RoundedCornerShape(100.dp))
                                .border(0.5.dp, color = Color.White, RoundedCornerShape(100.dp))
                                .padding(3.dp)
                                .align(Alignment.TopEnd)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                Column {
                    Text(
                        "Community Header",
                        fontFamily = sansationFontFamily,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Box{
                        Icon(
                            Icons.Sharp.Image,
                            contentDescription = "Community Header",
                            tint = BluePrimary,
                            modifier = Modifier
                                .width(160.dp)
                                .height(70.dp)
                                .background(BluePrimary)
                        )

                        Image(
                            painter = painterResource(R.drawable.ic_edit),
                            contentDescription = "edit",
                            Modifier
                                .size(16.dp)
                                .offset(x = 5.dp, y = -5.dp)
                                .background(BluePrimary, shape = RoundedCornerShape(100.dp))
                                .border(0.5.dp, color = Color.White, RoundedCornerShape(100.dp))
                                .padding(3.dp)
                                .align(Alignment.TopEnd)
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        "*Untuk mendapatkan hasil terbaik, pilih gambar dengan rasio aspek 5:1 dan lebar minimum 1000 piksel.",
                        fontFamily = alexandriaFontFamily,
                        fontSize = 5.sp,
                        fontWeight = FontWeight.ExtraLight,
                        modifier = Modifier
                            .width(150.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun EditCommunityTypeField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    height: Dp = 40.dp,
    multiline: Boolean = false
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = !multiline,
            textStyle = TextStyle(
                fontSize = 15.sp,
                fontFamily = sansationFontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            ),
            cursorBrush = SolidColor(Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .background(Color.White, RoundedCornerShape(5.dp))
                .border(1.dp, Color.Black, RoundedCornerShape(5.dp))
                .padding(horizontal = 16.dp, vertical = 14.dp),
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = if (multiline) Alignment.Top else Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                fontSize = 11.sp,
                                fontFamily = sansationFontFamily,
                                color = Color.Black
                            )
                        }
                        innerTextField()
                    }
                }
            }
        )

        Box(
            modifier = Modifier
                .offset(y = (-16).dp)
                .background(Color.White)
                .padding(horizontal = 6.dp)
        ) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontFamily = sansationFontFamily,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
    }
}



@Preview
@Composable
fun EditCommunityPreview(){
    EditCommunityDialog(
        onDismiss = {}
    )
}