package com.example.flocka.ui.chat.domain

import androidx.annotation.DrawableRes
import com.example.flocka.R

data class ChatModal(
    val id: Int,
    val name: String,
    @DrawableRes val image: Int,
    val isGroup: Boolean = false,
    val isTyping: Boolean = false,
    val messagePending: Boolean = false,
    val isDoubleTick: Boolean = false,
    val isOnline: Boolean = false,
    val message: String,
    val time: String,
    val receive: Boolean = true
)

val chatModalList = listOf(
    ChatModal(
        id = 1,
        name = "Billy Belly",
        image = R.drawable.p_billy,
        message = "jam 4 otw yaa! ketemu di tmpt.",
        time = "Kemarin",
        isOnline = false
    ),
    ChatModal(
        id = 2,
        name = "WE adventure",
        image = R.drawable.p_myadventure,
        message = "Adrian : logistik aman? ada yg kurang gk?",
        time = "21.20",
        isOnline = false,
        isGroup = true // ✅ mark as group
    ),
    ChatModal(
        id = 3,
        name = "Team of dreams",
        image = R.drawable.p_team,
        message = "You : meeting jam 11, jangan lupa ya all!",
        time = "21.09",
        isOnline = false,
        isGroup = true // ✅ mark as group
    ),
    ChatModal(
        id = 4,
        name = "Si Allie",
        image = R.drawable.p_allie,
        message = "al, dokumentasi jgn lupa ya besok!",
        time = "15.38",
        isOnline = true
    ),
    ChatModal(
        id = 5,
        name = "Alumni covid",
        image = R.drawable.p_alumnus,
        message = "infokan reuni!!",
        time = "15.38",
        isOnline = false,
        isGroup = true // ✅ mark as group
    ),
    ChatModal(
        id = 6,
        name = "Aditt yaa",
        image = R.drawable.p_gabriel,
        message = "ben, minjem charger bisa gk?",
        time = "Kemarin",
        isOnline = false
    ),
    ChatModal(
        id = 7,
        name = "Cici Cola",
        image = R.drawable.p_cici,
        message = "ci, mau ngeinfoin kalau bsk ada meeting.",
        time = "10.23",
        isOnline = false
    ),
    ChatModal(
        id = 8,
        name = "alchemist boy",
        image = R.drawable.p_alchemist,
        message = "Hasan : ayo daftar lomba, ges..",
        time = "10.23",
        isOnline = false,
        isGroup = true // ✅ mark as group
    )
)
