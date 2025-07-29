package ru.netology.nmedia2.repository

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia2.dto.Post

/** SharedPreferences - спец API позволяющее хранить данные примитивных типов(Boolen, строки ) в виде пар ключ:значение
 *
 *
 * **/

// Application context
class PostRepositorySharedPreferencesImpl(context: Context) : PostRepository {

    private val pref = context.getSharedPreferences(
        SHARED_PREF_NAME, // имя файла на устройстве
        Context.MODE_PRIVATE  // тип приватности(доступа)
    )

    private var nextId: Int = 1
    private var posts = emptyList<Post>()
        set(value) {
            field = value // field - клчевое слово для доступа к текущему значению поля
            sync()
        }
    private val data = MutableLiveData(posts) // изменяемая LiveData от posts

    init { // логика для заполнения полей posts, data
        pref.getString(POSTS_KEY, null)?.let { json ->  // извлекаем данные по ключу, если данных нет получаем null(или значение по умолчанию)
            posts = gson.fromJson(json, type) // преобразуем стринг в список постов

            data.value = posts // записываем полученные посты в livedata
            nextId = (posts.maxOfOrNull { it.id } ?: 0) + 1
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

    override fun viewById(id: Int) {
        posts = posts.map {
            if (it.id != id) it
            else it.copy(countViews = it.countViews + 1)
        }
    }

    private fun sync() {  // сохраняем данные
        pref.edit() {
            putString(
                POSTS_KEY, // по ключу
                gson.toJson(posts, type) // сериализуем список постов в json строку с учетом type
            )
        }
    }

    companion object { // статичная область памяти для констант
        // const поля будут созданы при компиляции, константу нельзя создать внутри класса
        private const val SHARED_PREF_NAME = "repo" // имя файла на устройстве
        private const val POSTS_KEY = "posts" // ключ для списка постов
        private val gson = Gson()  // экземпляр класса

        private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
            // type указываем какого типа данные будут храниться(передаем ссылки) и получаем тип
    /** Объясняем JSON что мы отим получить List из Post  **/


    }
}