package com.example.flocka.ui.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.flocka.ui.chat.components.ChatRow
import com.example.flocka.ui.chat.components.ChatSearchBar
import com.example.flocka.ui.chat.components.ChattingRow
import com.example.flocka.ui.chat.domain.chatModalList

@Composable
fun ChatUIScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        ChatRow(
            modifier = Modifier
                .fillMaxSize(),
            header = {}, // Removed TopBar
            searchBar = {
                ChatSearchBar()
            },
            chats = {
                items(chatModalList) { chat ->
                    ChattingRow(data = chat)
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChatUIScreenPreview() {
    val navController = rememberNavController()
    ChatUIScreen(navController = navController)
}
