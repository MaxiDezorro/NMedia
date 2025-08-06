//package ru.netology.nmedia2.repository
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import ru.netology.nmedia2.dao.PostDao
//import ru.netology.nmedia2.dto.Post
//
//// Application context
//class PostRepositorySQLiteImpl(private val dao: PostDao) : PostRepository {
//
//
//    private var posts = emptyList<Post>() // val не имеет сеттера
//
//    private val data = MutableLiveData(posts) // изменяемая LiveData от posts
//
//    init {
//        posts = dao.getAll()
//        data.value = posts
//    }
//
//
//    override fun getAll(): LiveData<List<Post>> {
//        return data
//    }
//
//    override fun likeById(id: Int) {
//        dao.likeById(id)
//        posts = posts.map { post ->
//            if (post.id != id) post else post.copy(
//                likeByMe = !post.likeByMe,
//                countLikes = if (!post.likeByMe) post.countLikes + 1 else post.countLikes - 1
//            )
//        }
//        data.value = posts
//    }
//
//    override fun shareById(id: Int) {
//        dao.shareById(id)
//        posts = posts.map { // проходимся по списку постов и записываем в новый список
//            if (it.id != id) it
//            else it.copy(countShare = it.countShare + 1)
//        }
//        data.value = posts
//    }
//
//    override fun removeById(id: Int) {
//        dao.removeById(id)
//        posts = posts.filter { it.id != id }
//        data.value = posts
//    }
//
//    override fun save(post: Post) {
//        val id = post.id
//        val saved = dao.save(post)
//        posts = if (id == 0) {
//            listOf(saved) + posts
//        } else {
//            posts.map {
//                if (it.id != id) it else saved
//            }
//        }
//        data.value = posts
//    }
//
//    override fun viewById(id: Int) {
//        dao.viewById(id)
//        posts = posts.map {
//            if (it.id != id) it
//            else it.copy(countViews = it.countViews + 1)
//        }
//        data.value = posts
//    }
//
//
//}