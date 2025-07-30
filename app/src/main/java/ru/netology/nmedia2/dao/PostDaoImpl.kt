package ru.netology.nmedia2.dao

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia2.dto.Post

class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {
    companion object {  // создаем таблицу, устанавливаем значения, ограничения
        val DDL = """
            CREATE TABLE ${PostColumns.TABLE} (
            ${PostColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${PostColumns.COLUMN_AUTHOR} TEXT NOT NULL,
            ${PostColumns.COLUMN_CONTENT} TEXT NOT NULL,
            ${PostColumns.COLUMN_PUBLISHED} TEXT NOT NULL,
            ${PostColumns.COLUMN_LIKE_BY_ME} BOOLEAN NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_LIKES} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_SHARE} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_VIEWS} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_VIDEO_URL} TEXT
        );
        """.trimIndent() // .trimIndent() удаляет общие отступы для всех строк
    }  /**  DDL """ многострочная строка """ DDL (Data Definition Language) - это язык определения данных в SQL
    Это SQL-команда для создания структуры таблицы
    Хранится как строковая константа в companion object
     **/
    object PostColumns { // константы для работы с таблицей
        const val TABLE = "posts"
        const val COLUMN_ID = "id"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_PUBLISHED = "published"
        const val COLUMN_LIKE_BY_ME = "likeByMe"
        const val COLUMN_LIKES = "countLikes"
        const val COLUMN_SHARE = "countShare"
        const val COLUMN_VIEWS = "countViews"
        const val COLUMN_VIDEO_URL = "videoURL"
        val ALL_COLUMNS = arrayOf(
            COLUMN_ID,
            COLUMN_AUTHOR,
            COLUMN_CONTENT,
            COLUMN_PUBLISHED,
            COLUMN_LIKE_BY_ME,
            COLUMN_LIKES,
            COLUMN_SHARE,
            COLUMN_VIEWS,
            COLUMN_VIDEO_URL
        )

    }





    override fun getAll(): List<Post> {
        TODO("Not yet implemented")
    }

    override fun save(post: Post): Post {
        TODO("Not yet implemented")
    }

    override fun likeById(id: Int) {
        TODO("Not yet implemented")
    }

    override fun shareById(id: Int) {
        TODO("Not yet implemented")
    }

    override fun removeById(id: Int) {
        TODO("Not yet implemented")
    }

    override fun viewById(id: Int) {
        TODO("Not yet implemented")
    }

    private fun map(cursor: Cursor): Post {  // преобразует данные из курсора SQLite в объект Post
        with(cursor) {
            return Post(
                id = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_ID)),
                author = getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTHOR)),
                content = getString(getColumnIndexOrThrow(PostColumns.COLUMN_CONTENT)),
                published = getString(getColumnIndexOrThrow(PostColumns.COLUMN_PUBLISHED)),
                countLikes = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKES)),
                likeByMe = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKE_BY_ME)) != 0,
                countShare = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_SHARE)),
                countViews = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_VIEWS)),
                videoURL = getString(getColumnIndexOrThrow(PostColumns.COLUMN_VIDEO_URL))
            )
        }
    }
}