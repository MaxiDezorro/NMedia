package ru.netology.nmedia2.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia2.dto.Post

// Application context
class PostRepositorySharedPreferencesImpl(context: Context) : PostRepository {

    private val pref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

    private var nextId: Int = 1
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts) // изменяемая LiveData от posts

    init {
        pref.getString(POSTS_KEY, null)?.let { json ->
            posts = gson.fromJson(json, type)

            data.value = posts
        }
    }

    override fun getAll(): LiveData<List<Post>> {
        return data
    }

    override fun likeById(id: Int) {
        posts = posts.map { post ->
            if (post.id != id) post else post.copy(
                likeByMe = !post.likeByMe,
                countLikes = if (!post.likeByMe) post.countLikes + 1 else post.countLikes - 1
            )
        }
        data.value = posts //  в data записываем новое состояние posta через метод value
    }

    override fun shareById(id: Int) {
        posts = posts.map { // проходимся по списку постов и записываем в новый список
            if (it.id != id) it
            else it.copy(countShare = it.countShare + 1)
        }
        data.value = posts
    }

    override fun removeById(id: Int) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        if (post.id == 0) {
            posts = listOf( // создаем новый список с 1 постом
                post.copy(
                    author = "Me",
                    id = nextId++,
                    published = "now"
                )
            ) + posts // соединяем списки
        } else {
            posts = posts.map {
                if (post.id != it.id) it else it.copy(content = post.content)
            }
        }
        data.value = posts // обновляем данные в MutableLiveData
    }

    private fun sync(){
        pref.edit().apply{
            putString(POSTS_KEY, gson.toJson(posts, type))
            apply()
        }
    }

    companion object { // статичная область памяти для констант
        // const поля будут созданы при компиляции, константу нельзя создать внутри класса
        private const val SHARED_PREF_NAME = "repo"
        private const val POSTS_KEY = "posts"
        private val gson = Gson()  // экземпляр класса

        private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type

    }
}