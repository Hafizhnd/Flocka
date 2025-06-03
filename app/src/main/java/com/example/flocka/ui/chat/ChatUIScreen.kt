package com.example.flocka.ui.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flocka.R
import com.example.flocka.ui.chat.components.ChatSearchBar
import com.example.flocka.ui.chat.components.ChattingRow
import com.example.flocka.ui.chat.domain.ChatModal
import com.example.flocka.ui.chat.domain.chatModalList

@Composable
fun ChatUIScreen(
    onChatClick: (ChatModal) -> Unit,
    onAddContact: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 28.dp)
        ) {
            ChatSearchBar()

            LazyColumn {
                items(chatModalList) { chat ->
                    ChattingRow(
                        data = chat,
                        onClick = { onChatClick(chat) }
                    )
                }
            }
        }

        Image(
            painter = painterResource(id = R.drawable.ic_add_contact),
            contentDescription = "Add Contact",
            modifier = Modifier
                .size(50.dp)
                .offset(x = (-26).dp, y = (-26).dp)
                .align(Alignment.BottomEnd)
                .clickable(onClick = onAddContact)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChatUIScreenPreview() {
    ChatUIScreen(
        onChatClick = {},
        onAddContact = {}
    )
}
