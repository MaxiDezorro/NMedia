package ru.netology.nmedia2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia2.dto.Post
import ru.netology.nmedia2.repository.PostRepository
import ru.netology.nmedia2.repository.PostRepositoryInMemoryImpl

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl() // предоставляем достуа к репозиторию через
    // интерфейс PostRepository. можем обратиться к методам только этого интерфейса

    val data: LiveData<List<Post>> = repository.getAll() // на это поле подписана activity

    fun likeById(id: Int) = repository.likeById(id) // вызывается в слушателе и изменяет наш post
    fun shareById(id: Int) = repository.shareById(id)

}