package ru.netology.nmedia2.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia2.dto.Post

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun likeById(id: Int)
    fun shareById(id: Int)
    fun removeById(id: Int)
    fun save(post: Post)
}