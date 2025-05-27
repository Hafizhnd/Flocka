package com.example.flocka.ui.home.communities

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.flocka.ui.components.sansationFontFamily

@Composable
fun CommunityCreatedDialog(
    onDoneClick: () -> Unit // This callback is still here
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(406.dp)
            .padding(horizontal = 34.dp)
            .background(Color.White, RoundedCornerShape(10.dp))
    ){
        Image(
            painterResource(R.drawable.new_com_dial),
            contentDescription = "Dialog",
            modifier = Modifier.fillMaxSize()
        )

        Button(
            onClick = onDoneClick, // This will now trigger the navigation through CommunitiesMain
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .padding(horizontal = 35.dp)
                .offset(y = 344.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary, contentColor = Color.White),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text("Done", fontSize = 14.sp, fontFamily = sansationFontFamily, fontWeight = FontWeight.Bold)
        }
    }
}

@Preview
@Composable
fun CommunityCreatedPreview(){
    CommunityCreatedDialog(
        onDoneClick = {}
    )
}