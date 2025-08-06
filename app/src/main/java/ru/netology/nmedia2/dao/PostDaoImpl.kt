//package ru.netology.nmedia2.dao
//
//import android.content.ContentValues
//import android.database.Cursor
//import android.database.sqlite.SQLiteDatabase
//import ru.netology.nmedia2.dto.Post
//
//class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {
//    companion object {  // создаем таблицу, устанавливаем значения, ограничения
//        val DDL = """
//            CREATE TABLE ${PostColumns.TABLE} (
//            ${PostColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
//            ${PostColumns.COLUMN_AUTHOR} TEXT NOT NULL,
//            ${PostColumns.COLUMN_CONTENT} TEXT NOT NULL,
//            ${PostColumns.COLUMN_PUBLISHED} TEXT NOT NULL,
//            ${PostColumns.COLUMN_LIKE_BY_ME} BOOLEAN NOT NULL DEFAULT 0,
//            ${PostColumns.COLUMN_LIKES} INTEGER NOT NULL DEFAULT 0,
//            ${PostColumns.COLUMN_SHARE} INTEGER NOT NULL DEFAULT 0,
//            ${PostColumns.COLUMN_VIEWS} INTEGER NOT NULL DEFAULT 0,
//            ${PostColumns.COLUMN_VIDEO_URL} TEXT
//        );
//        """.trimIndent() // .trimIndent() удаляет общие отступы для всех строк
//    }
//
//    /**  DDL """ многострочная строка """ DDL (Data Definition Language) - это язык определения данных в SQL
//    Это SQL-команда для создания структуры таблицы
//    Хранится как строковая константа в companion object
//     **/
//    object PostColumns { // константы для работы с таблицей
//        const val TABLE = "posts"
//        const val COLUMN_ID = "id"
//        const val COLUMN_AUTHOR = "author"
//        const val COLUMN_CONTENT = "content"
//        const val COLUMN_PUBLISHED = "published"
//        const val COLUMN_LIKE_BY_ME = "likeByMe"
//        const val COLUMN_LIKES = "countLikes"
//        const val COLUMN_SHARE = "countShare"
//        const val COLUMN_VIEWS = "countViews"
//        const val COLUMN_VIDEO_URL = "videoURL"
//        val ALL_COLUMNS = arrayOf(
//            COLUMN_ID,
//            COLUMN_AUTHOR,
//            COLUMN_CONTENT,
//            COLUMN_PUBLISHED,
//            COLUMN_LIKE_BY_ME,
//            COLUMN_LIKES,
//            COLUMN_SHARE,
//            COLUMN_VIEWS,
//            COLUMN_VIDEO_URL
//        )
//
//    }
//
//    /** Параметры query():
//
//    Таблица — PostColumns.TABLE ("posts")
//
//    Колонки — все колонки из PostColumns.ALL_COLUMNS
//
//    WHERE — null (значит, выбираем все записи)
//
//    WHERE-аргументы — null (нет параметров для подстановки)
//
//    GROUP BY — null (не группируем)
//
//    HAVING — null (нет условий для групп)
//
//    ORDER BY — "id DESC" (сортировка по ID в порядке убывания)**/
//
//    override fun getAll(): List<Post> {
//        val posts = mutableListOf<Post>()
//        db.query(
//            PostColumns.TABLE,  // Таблица: "posts"
//            PostColumns.ALL_COLUMNS, // Все колонки (массив из PostColumns)
//            null, // WHERE условие (null = все записи)
//            null, // Аргументы для WHERE (нет)
//            null, // GROUP BY (нет)
//            null, // HAVING (нет)
//            "${PostColumns.COLUMN_ID} DESC"  // Сортировка по ID в обратном порядке
//        ).use { cursor ->
//            while (cursor.moveToNext()) { // Перебираем все строки результата
//                posts.add(map(cursor))  // Преобразуем строку в Post и добавляем в список
//            }
//        }
//        return posts
//    }
//
//    override fun save(post: Post): Post {
//        val values  = ContentValues().apply {
//            // todo delete hardcoded value
//            put(PostColumns.COLUMN_AUTHOR, "me")
//            put(PostColumns.COLUMN_CONTENT, post.content)
//            put(PostColumns.COLUMN_PUBLISHED, "now")
//        }
//        val id = if (post.id != 0) {// если id  не 0, обновляем существующий
//            db.update(
//                PostColumns.TABLE,
//                values,                                        // Новые значения
//                "${PostColumns.COLUMN_ID} = ?",  // Условие (по ID)
//                arrayOf(post.id.toString()),        // Параметр для условия
//            )
//           post.id   // Возвращаем существующий ID
//        } else {  // Вставка нового поста
//            db.insert(PostColumns.TABLE, null, values)
//        }
//            db.query(
//                PostColumns.TABLE,
//                PostColumns.ALL_COLUMNS,
//                "${PostColumns.COLUMN_ID} = ?",
//                arrayOf(id.toString()),
//                null,
//                null,
//                null
//            ).use {
//                it.moveToNext()  // Переход к первой (и единственной) записи
//                return map(it) // Преобразование в объект Post
//
//        }
//    }
//
//    override fun likeById(id: Int) {
//        db.execSQL(
//            """
//                UPDATE posts SET
//                countLikes = countLikes + CASE WHEN likeByMe THEN -1 ELSE 1 END,
//                likeByMe = CASE WHEN likeByMe THEN 0 ELSE 1 END
//                WHERE id = ?;
//            """.trimIndent(),
//            arrayOf(id) // arrayOf(id) - подставляется вместо ? для защиты от инекций
//        )
//    }
//
//    override fun shareById(id: Int) {
//        db.execSQL(
//            """
//                UPDATE ${PostColumns.TABLE} SET
//                ${PostColumns.COLUMN_SHARE} = ${PostColumns.COLUMN_SHARE} + 1
//                WHERE id = ?;
//            """.trimIndent(), arrayOf(id)
//        )
//    }
//
//    override fun removeById(id: Int) {
//        db.delete(
//            PostColumns.TABLE,
//            "${PostColumns.COLUMN_ID} = ?",
//            arrayOf(id.toString())
//        )
//    }
//
//    override fun viewById(id: Int) {
//        db.execSQL(
//            """
//                UPDATE ${PostColumns.TABLE} SET
//                ${PostColumns.COLUMN_VIEWS} = ${PostColumns.COLUMN_VIEWS} + 1
//                WHERE id = ?;
//            """.trimIndent(), arrayOf(id)
//        )
//    }
//
//    private fun map(cursor: Cursor): Post {  // преобразует данные из курсора SQLite в объект Post
//        with(cursor) {
//            return Post(
//                id = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_ID)),
//                author = getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTHOR)),
//                content = getString(getColumnIndexOrThrow(PostColumns.COLUMN_CONTENT)),
//                published = getString(getColumnIndexOrThrow(PostColumns.COLUMN_PUBLISHED)),
//                countLikes = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKES)),
//                likeByMe = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKE_BY_ME)) != 0,
//                countShare = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_SHARE)),
//                countViews = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_VIEWS)),
//                videoURL = getString(getColumnIndexOrThrow(PostColumns.COLUMN_VIDEO_URL))
//            )
//        }
//    }
//}