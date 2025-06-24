package ru.netology.nmedia2.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia2.dto.Post

interface PostRepository {
    fun get():LiveData<Post>
    fun like()
    fun share()
}