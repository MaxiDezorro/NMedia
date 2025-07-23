package ru.netology.nmedia2.repository

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia2.dto.Post
import kotlin.math.max

// Application context
class PostRepositoryFileImpl(private val context: Context) : PostRepository {

    private var nextId: Int = 1
    private var posts = emptyList<Post>()
        set(value) {
            field = value // field - клчевое слово для доступа к текущему значению поля
            sync()
            data.value = posts //  в data записываем новое состояние posta через метод value
            // обновляем данные в MutableLiveData
        }
    private val data = MutableLiveData(posts) // изменяемая LiveData от posts

    init {
        val file = context.filesDir.resolve(FILE_NAME)

        if (file.exists()) {
            file.bufferedReader().use { reader ->
                posts = gson.fromJson(reader, type)

                nextId = (posts.maxOfOrNull { it.id } ?: 0) + 1
            }
        } else {
            sync()
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

    }

    override fun shareById(id: Int) {
        posts = posts.map { // проходимся по списку постов и записываем в новый список
            if (it.id != id) it
            else it.copy(countShare = it.countShare + 1)
        }

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

    }

    private fun sync() {
       context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).bufferedWriter().use { writer ->
           writer.write(gson.toJson(posts, type))

       }
    }

    companion object { // статичная область памяти для констант
        // const поля будут созданы при компиляции, константу нельзя создать внутри класса
        private const val FILE_NAME = "posts.json"
        private val gson = Gson()  // экземпляр класса
        private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type

    }
}