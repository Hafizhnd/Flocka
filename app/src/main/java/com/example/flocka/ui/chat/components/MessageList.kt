package com.example.flocka.ui.chat

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.example.flocka.ui.chat.components.MessageBubble
import com.example.flocka.ui.chat.components.MessageBubbleGroup
import com.example.flocka.ui.chat.domain.Message

@Composable
fun MessageList(
    messages: List<Message>,
    isGroupChat: Boolean // <- tambahkan flag ini
) {
    LazyColumn(
        reverseLayout = false,
        content = {
            items(messages) { message ->
                if (isGroupChat) {
                    MessageBubbleGroup(message = message)
                } else {
                    MessageBubble(message = message)
                }
            }
        }
    )
}
