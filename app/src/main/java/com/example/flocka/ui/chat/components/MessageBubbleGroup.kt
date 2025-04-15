package com.example.flocka.ui.chat.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flocka.ui.chat.domain.Message
import com.example.flocka.ui.chat.domain.groupChat
import com.example.flocka.ui.chat.domain.privateChat

@Composable
fun MessageBubbleGroup(message: Message) {
    val bubbleColor = if (message.isMine) Color(0xFFECECEC) else Color.White
    val horizontalPadding =
        if (message.isMine) PaddingValues(start = 48.dp, end = 16.dp) else PaddingValues(start = 16.dp, end = 48.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 4.dp),
        horizontalArrangement = if (message.isMine) Arrangement.End else Arrangement.Start
    ) {
        if (!message.isMine) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                message.avatarResId?.let { avatarId ->
                    Image(
                        painter = painterResource(id = avatarId),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(50))
                    )
                } ?: Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color.Gray, shape = RoundedCornerShape(50))
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            modifier = Modifier
                .padding(horizontalPadding)
                .background(color = bubbleColor, shape = RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .widthIn(max = 280.dp)
        ) {
            if (!message.isMine) {
                Text(
                    text = message.sender,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF5871FF)
                )
                Spacer(modifier = Modifier.height(2.dp))
            }

            Text(
                text = message.content,
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = message.time,
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Preview
@Composable
fun MessageBubbleGroupPreview() {
    MessageBubbleGroup(message = privateChat.messages.first())
}
