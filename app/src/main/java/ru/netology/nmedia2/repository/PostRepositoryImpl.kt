package ru.netology.nmedia2.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.nmedia2.dao.PostDao
import ru.netology.nmedia2.dto.Post
import ru.netology.nmedia2.entity.PostEntity
import ru.netology.nmedia2.entity.toEntity

class PostRepositoryImpl(private val dao: PostDao) : PostRepository {

    override fun getAll(): LiveData<List<Post>> {
        return dao.getAll().map { list ->
            list.map {
                it.toDto()  // преобразуем PostEntity в Post
            }
        }
    }

    override fun likeById(id: Int) {
        dao.likeById(id)
    }

    override fun shareById(id: Int) {
        dao.shareById(id)
    }

    override fun removeById(id: Int) {
        dao.removeById(id)
    }

    override fun save(post: Post) {
//        dao.save(PostEntity.fromDto(post))
        dao.save(post.toEntity())
    }

    override fun viewById(id: Int) {
        dao.viewById(id)
    }
}