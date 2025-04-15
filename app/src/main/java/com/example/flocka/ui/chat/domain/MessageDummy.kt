package com.example.flocka.ui.chat.domain

import com.example.flocka.R

data class Message(
    val id: Int,
    val sender: String,
    val content: String,
    val time: String,
    val isMine: Boolean,
    val avatarResId: Int? = null
)

data class ChatRoom(
    val title: String,
    val members: List<String>,
    val messages: List<Message>
)

val privateChat = ChatRoom(
    title = "Billy Belly",
    members = listOf("Me", "Billy Belly"),
    messages = listOf(
        Message(1, "Billy", "jam 4 otw yaa! ketemu di tmpt.", "14.50", false),
        Message(2, "Me", "siapp, bro, aman.", "14.52", true),
        Message(3, "Me", "kalau bisa jgn telat yaa!", "14.52", true),
        Message(4, "Billy", "aman bro, pasti on time. ntr gw langsung tunggu di meja yaa.", "14.50", false),
        Message(5, "Me", "siap.", "14.52", true),
        Message(6, "Me", "gw otw nih ya", "14.52", true)
    )
)

val groupChat = ChatRoom(
    title = "Team of dreams",
    members = listOf("Me", "Aline Shah", "Fore", "Hasan", "Raechel"),
    messages = listOf(
        Message(1, "Me", "meeting jam 11, jgn lupa ya all!", "21.09", true),
        Message(2, "Hasan", "noted, ben. ntr linknya di share aja.", "21.12", false, R.drawable.p_hasan),
        Message(3, "Aline Shah", "sipp. ntr izin nelat dikit ya gess.", "21.13", false, R.drawable.p_aline),
        Message(4, "Me", "materi bahasan aman?", "21.13", true),
        Message(5, "Raechel", "aman line, udh aku siapin kok.", "21.15", false, R.drawable.p_rachel),
        Message(6, "Me", "linknya ntr aku share ya, kita pakai meet aja.", "21.20", true),
        Message(7, "Fore", "oke", "21.21", false, R.drawable.p_fore)
    )
)

