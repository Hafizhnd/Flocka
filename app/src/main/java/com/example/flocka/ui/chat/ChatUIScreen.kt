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
import com.yourpackage.ui.components.TopBar
import com.yourpackage.ui.components.BottomNavBar

@Composable
fun ChatUIScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Bagian atas: chat list
        ChatRow(
            modifier = Modifier
                .weight(1f), // Supaya isi chat mengisi ruang di atas
            header = {
                TopBar(
                    username = "Benny Jeans",
                    subtitle = "UI / UX Enthusiast"
                )
            },
            searchBar = {
                ChatSearchBar()
            },
            chats = {
                items(chatModalList) { chat ->
                    ChattingRow(data = chat)
                }
            }
        )

        // Bottom NavBar di paling bawah
        BottomNavBar(navController = navController)
    }
}

@Preview(showBackground = true)
@Composable
fun ChatUIScreenPreview() {
    val navController = rememberNavController()
    ChatUIScreen(navController = navController)
}
