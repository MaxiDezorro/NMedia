package ru.netology.nmedia2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia2.dto.Post
import ru.netology.nmedia2.repository.PostRepository
import ru.netology.nmedia2.repository.PostRepositoryFileImpl
import ru.netology.nmedia2.repository.PostRepositoryInMemoryImpl
import ru.netology.nmedia2.repository.PostRepositorySharedPreferencesImpl

private val empty = Post(
    id = 0,
    author = "",
    content = "",
    published = "",
    likeByMe = false
)

//     class PostViewModel : ViewModel() { // заменяем ViewModel на AndroidViewModel  чтоб получить доступ к context
class PostViewModel(application: Application): AndroidViewModel(application) {
    private val repository: PostRepository =
        PostRepositoryFileImpl(application) // предоставляем достуа к репозиторию через
    // интерфейс PostRepository. можем обратиться к методам только этого интерфейса

    val data: LiveData<List<Post>> = repository.getAll() // на это поле подписана activity

    val edited = MutableLiveData(empty) // хранит текущий пост который добавляем или редактируем

    fun likeById(id: Int) = repository.likeById(id) // вызывается в слушателе и изменяет наш post
    fun shareById(id: Int) = repository.shareById(id) // поделиться
    fun removeById(id: Int) = repository.removeById(id) // удалить

    fun viewById(id: Int) = repository.viewById(id) // просмотры

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
        clearEdit()
    }

    fun edit(post: Post) {
       edited.value = post

    }

    fun clearEdit(){
        edited.value = empty // устанавливаем в edited значение пустого поста
    }

}