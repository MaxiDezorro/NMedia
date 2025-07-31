package ru.netology.nmedia2.dto

data class Post(
    val id: Int,
    val author: String,
    val published: String,
    val content: String,
    val countLikes: Int = 0,
    val likeByMe: Boolean = false,
    val countShare: Int = 0,
    val countViews: Int = 0,
    val videoURL: String? = null
)
