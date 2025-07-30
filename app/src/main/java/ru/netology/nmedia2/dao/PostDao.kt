package ru.netology.nmedia2.dao

import ru.netology.nmedia2.dto.Post

interface PostDao {
    fun getAll(): List<Post>
    fun save(post: Post): Post
    fun likeById(id: Int)
    fun shareById(id: Int)
    fun removeById(id: Int)
    fun viewById(id: Int)
}