package ru.netology.nmedia2.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia2.dto.Post

@Entity(tableName = "Post_Entity")
data class PostEntity(
    @PrimaryKey(true) val id: Int,
    val author: String,
    val published: String,
    val content: String,
    val countLikes: Int = 0,
    val likeByMe: Boolean = false,
    val countShare: Int = 0,
    val countViews: Int = 0,
    val videoURL: String? = null
) {

    fun toDto() = Post(
        id = id,
        author = author,
        published = published,
        content = content,
        countLikes = countLikes,
        likeByMe = likeByMe,
        countShare = countShare,
        countViews = countViews,
        videoURL = videoURL
    )

    companion object {
        fun fromDto(post: Post) = post.run {

            PostEntity(
                id = id,
                author = author,
                published = published,
                content = content,
                countLikes = countLikes,
                likeByMe = likeByMe,
                countShare = countShare,
                countViews = countViews,
                videoURL = videoURL
            )
        }
    }
}

fun Post.toEntity() = PostEntity(
    id = id,
    author = author,
    published = published,
    content = content,
    countLikes = countLikes,
    likeByMe = likeByMe,
    countShare = countShare,
    countViews = countViews,
    videoURL = videoURL
)