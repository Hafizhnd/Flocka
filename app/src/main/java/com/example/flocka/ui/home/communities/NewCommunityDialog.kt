package com.example.flocka.ui.home.communities

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
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.OrangePrimary
import com.example.flocka.ui.components.sansationFontFamily
import com.example.flocka.viewmodel.community.CommunityViewModel


@Composable
fun NewCommunityDialog(
    token: String,
    communityViewModel: CommunityViewModel,
    onDismiss: () -> Unit,
    onCreateCommunity: () -> Unit
){

    var communityName by remember { mutableStateOf("") }
    var communityDesc by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(406.dp)
            .padding(horizontal = 34.dp)
            .background(Color.White, RoundedCornerShape(10.dp))
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 26.dp, horizontal = 23.dp)
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
                    "New Community",
                    fontFamily = sansationFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = BluePrimary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Tell us about your community. You can change the information anytime you want!",
                fontFamily = sansationFontFamily,
                fontSize = 10.sp,
                textAlign = TextAlign.Justify,
                color = OrangePrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            NewCommunityTypeField(
                value = communityName,
                onValueChange = { communityName = it },
                label = "Community Name",
                placeholder = "Enter your community name"
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                "Community name must be 3-30 characters long and cannot include hashtag (#)",
                fontFamily = sansationFontFamily,
                fontSize = 10.sp,
                color = Color(0xFFD1D0D0),
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            NewCommunityTypeField(
                value = communityDesc,
                onValueChange = { communityDesc = it },
                label = "Community Description",
                placeholder = "Enter your community description",
                height = 78.dp,
                multiline = true
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                "Give description so others can know what your community is up to.",
                fontFamily = sansationFontFamily,
                fontSize = 10.sp,
                color = Color(0xFFD1D0D0),
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(39.dp))

            Button(
                onClick = {
                    if (communityName.isNotBlank()) {
                        communityViewModel.createCommunity(token, communityName, communityDesc, null)
                    } else {
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary, contentColor = Color.White),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("Create", fontSize = 13.sp, fontFamily = sansationFontFamily, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun NewCommunityTypeField(
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
                fontSize = 12.sp,
                fontFamily = sansationFontFamily,
                color = Color.Black
            ),
            cursorBrush = SolidColor(Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .background(Color.White, RoundedCornerShape(5.dp))
                .border(1.dp, Color(0xFFD1D0D0), RoundedCornerShape(5.dp))
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
                                color = Color(0xFFD1D0D0)
                            )
                        }
                        innerTextField()
                    }
                }
            }
        )

        Box(
            modifier = Modifier
                .offset(x = 20.dp, y = (-6).dp)
                .background(Color.White)
                .padding(horizontal = 6.dp)
        ) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontFamily = sansationFontFamily,
                color = Color.Black
            )
        }
    }
}

@Preview
@Composable
fun NewCommunityDialogPreview(){
    NewCommunityDialog(
        token = "preview_token",
        communityViewModel = viewModel(),
        onDismiss = {},
        onCreateCommunity = {}
    )
}