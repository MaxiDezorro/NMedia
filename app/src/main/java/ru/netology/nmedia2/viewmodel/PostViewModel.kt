package ru.netology.nmedia2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia2.dto.Post
import ru.netology.nmedia2.repository.PostRepository
import ru.netology.nmedia2.repository.PostRepositoryInMemoryImpl

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()

    val data: LiveData<Post> = repository.get()

    fun like() = repository.like()

}