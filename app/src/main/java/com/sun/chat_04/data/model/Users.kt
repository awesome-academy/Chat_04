package com.sun.chat_04.data.model

data class Users(
    val idUser: String = "",
    val userName: String = "",
    val Age: Int = 0,
    val gender: String = "",
    val bio: String? = "",
    val linkAvatar: String? = "",
    val linkBackground: String? = "",
    var isOnline: Int = 0,
    var lgn: Double = 0.0,
    var lat: Double = 0.0
)
