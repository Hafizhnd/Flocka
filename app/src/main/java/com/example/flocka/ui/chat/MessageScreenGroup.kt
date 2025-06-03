package com.example.flocka.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flocka.ui.chat.components.*
import com.example.flocka.ui.chat.domain.Message
import com.example.flocka.ui.chat.domain.groupChat

@Composable
fun MessageScreenGroup (
    messages: List<Message>,
    title: String,
    subtitle: String,
    onBackClick: () -> Unit
) {
    var chatMessages by remember { mutableStateOf(messages) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDF1F6))
    ) {

        MessageHeader(
            username = title,
            subtitle = subtitle,
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            MessageList(
                messages = chatMessages,
                isGroupChat = true
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        MessageInputBar(
            onSend = { newMessage ->
                chatMessages = chatMessages + Message(
                    id = chatMessages.size + 1,
                    sender = "Me",
                    content = newMessage,
                    time = "14:54",
                    isMine = true
                )
            }
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview
@Composable
fun PreviewMessageScreenGroup() {
    MessageScreenGroup(
        messages = groupChat.messages,
        title = groupChat.title,
        subtitle = "UI Designer",
        onBackClick = {}
    )
}
