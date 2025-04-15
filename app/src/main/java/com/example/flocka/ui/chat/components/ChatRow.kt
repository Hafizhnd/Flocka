package com.example.flocka.ui.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ChatRow(
    modifier: Modifier = Modifier,
    header: (@Composable () -> Unit)? = null,
    searchBar: (@Composable () -> Unit)? = null,
    chats: (LazyListScope.() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFEDF1F6)),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        header?.invoke()
        searchBar?.invoke()
        LazyColumn {
            chats?.invoke(this)
        }
    }
}
