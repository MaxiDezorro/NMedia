package ru.netology.nmedia2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia2.dto.Post
import ru.netology.nmedia2.repository.PostRepository
import ru.netology.nmedia2.repository.PostRepositoryInMemoryImpl

private val empty = Post(
    id = 0,
    author = "",
    content = "",
    published = "",
    likeByMe = false
)

class PostViewModel : ViewModel() {
    private val repository: PostRepository =
        PostRepositoryInMemoryImpl() // предоставляем достуа к репозиторию через
    // интерфейс PostRepository. можем обратиться к методам только этого интерфейса

    val data: LiveData<List<Post>> = repository.getAll() // на это поле подписана activity

    val edited = MutableLiveData(empty) // хранит текущий пост который добавляем или редактируем

    fun likeById(id: Int) = repository.likeById(id) // вызывается в слушателе и изменяет наш post
    fun shareById(id: Int) = repository.shareById(id) // поделиться
    fun removeById(id: Int) = repository.removeById(id) // удалить

    fun changeContent(content: String) {
        val text = content.trim() // убераем пробелы в начале и конце
        edited.value?.let {  // берем значение из edited
            if (text != it.content) {
            edited.value = it.copy(content = text) // заменяем у текушего поста контент на новый текст
            }

        }

    }

    fun save(){
        edited.value?.let {
            repository.save(it) // сохраняем новый пост
        }
        edited.value = empty // устанавливаем в edited значение пустого поста
    }

    fun edit(post: Post) {
       edited.value = post

    }

}