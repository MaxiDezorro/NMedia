package ru.netology.nmedia2.dto

data class Post(
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    val countLikes: Int = 999,
    val likeByMe: Boolean = false,
    var countShare: Int = 999,
    var countViews: Int = 6399456,
    val videoURL: String? = null
)
