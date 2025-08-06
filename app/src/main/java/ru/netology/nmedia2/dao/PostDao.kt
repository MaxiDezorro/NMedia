package ru.netology.nmedia2.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.nmedia2.entity.PostEntity

@Dao
interface PostDao {
    @Query("SELECT * FROM POST_ENTITY ORDER BY id DESC") // sql запрос
    fun getAll(): LiveData<List<PostEntity>>

    /** возвращаемый тип PostEntity, бд будет работать с PostEntity **/

    fun save(post: PostEntity) {
        if (post.id == 0) {
            insert(post)  // создаем новую запись
        } else {
            updateById(post.id, post.content)    // обновляем запись
        }
    }

    @Insert
    fun insert(post: PostEntity)

    @Query("UPDATE Post_Entity SET content=:content WHERE id=:id")
    fun updateById(id: Int, content: String)

    @Query(""" UPDATE Post_Entity SET
                countLikes = countLikes + CASE WHEN likeByMe THEN -1 ELSE 1 END,
                likeByMe = CASE WHEN likeByMe THEN 0 ELSE 1 END
                WHERE id =:id;""")
    fun likeById(id: Int)

    @Query("""UPDATE Post_Entity SET
                countShare = countShare + 1
                WHERE id =:id;""")
    fun shareById(id: Int)

    @Query("DELETE FROM Post_Entity WHERE id=:id")
    fun removeById(id: Int)

    @Query("""UPDATE Post_Entity SET
                countViews = countViews + 1
                WHERE id =:id;""")
    fun viewById(id: Int)
}