package com.example.flocka.ui.home.communities

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flocka.ui.components.BluePrimary
import com.example.flocka.ui.components.OrangePrimary
import com.example.flocka.ui.components.sansationFontFamily

@Composable
fun EditCommunityDialog(
    onDismiss: () -> Unit
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
                            onClick = { },
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
                    "Tell us about your community. You can change the information anytime you want!",
                    fontFamily = sansationFontFamily,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Justify,
                    color = OrangePrimary
                )

                NewCommunityTypeField(
                    value = communityName,
                    onValueChange = { communityName = it },
                    label = "Community Name",
                    placeholder = "Enter your community name"
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    "Community name must be 3-30 characters long and cannot include hashtag (#)",
                    fontFamily = sansationFontFamily,
                    fontSize = 10.sp,
                    color = Color(0xFF808183),
                    textAlign = TextAlign.Justify,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )

                NewCommunityTypeField(
                    value = communityDesc,
                    onValueChange = { communityDesc = it },
                    label = "Community Description",
                    placeholder = "Enter your community description",
                    height = 78.dp,
                    multiline = true
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    "Give description so others can know what your community is up to.",
                    fontFamily = sansationFontFamily,
                    fontSize = 10.sp,
                    color = Color(0xFF808183),
                    textAlign = TextAlign.Justify,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    "Community Visibility",
                    fontFamily = sansationFontFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    "Choose who can discover and join your community. All community can be seen by others in Flocka.",
                    fontFamily = sansationFontFamily,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Justify,
                    color = OrangePrimary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically){
                    Column {
                        Text(
                            "Public",
                            fontFamily = sansationFontFamily,
                            fontSize = 11.sp
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            "Anyone can join your community without an invitation.",
                            fontFamily = sansationFontFamily,
                            fontSize = 9.sp,
                            textAlign = TextAlign.Justify,
                            color = Color(0xFF808183)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        Icons.Rounded.CheckCircle,
                        contentDescription = "public",
                        tint = BluePrimary,
                        modifier = Modifier
                            .size(18.dp)
                    )
                }
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
