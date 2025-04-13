package com.yourpackage.ui.progress

import android.R.id.background
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.flocka.R
import com.yourpackage.ui.components.AppBackground
import com.yourpackage.ui.components.BottomNavBar
import com.yourpackage.ui.components.sansationFontFamily
import com.yourpackage.ui.screens.BaseScreen

@Composable
fun ProgressMain() {
    BaseScreen {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxWidth()
            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .shadow(5.dp, RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))
                        .background(color = Color.White),
                    contentAlignment = Alignment.TopCenter
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(18.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(225.dp)
                        ){
                            Text(
                                "Streak",
                                fontFamily = sansationFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.Black
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            Text(
                                "Keep the good work!!",
                                fontFamily = sansationFontFamily,
                                fontSize = 11.sp,
                                color = Color(0xFF172D9D)
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Background bar with circles inside
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(10.dp)
                                        .clip(RoundedCornerShape(100.dp))
                                        .background(color = Color(0xFFEDF1F6))
                                        .padding(horizontal = 20.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        (1..7).forEach { day ->
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(10.dp)
                                                        .clip(RoundedCornerShape(50))
                                                        .background(Color.Gray)
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(2.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 23.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    (1..7).forEach { day ->
                                        Text(
                                            text = day.toString(),
                                            fontSize = 10.sp,
                                            fontFamily = sansationFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }

                        }

                        Box(
                            modifier = Modifier
                                .padding(bottom = 4.dp)
                                .fillMaxSize(),
                            contentAlignment = Alignment.BottomEnd
                        ){
                            Image(
                                painter = painterResource(id = R.drawable.ic_streak),
                                contentDescription = "Background Image",
                                modifier = Modifier
                                    .size(50.dp),
                            )
                            Text(
                                "X",
                                color = Color.White,
                                fontFamily = sansationFontFamily,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(end = 13.dp, bottom = 9.dp)
                            )
                        }
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ProgressMainPreview() {
    ProgressMain()
}

