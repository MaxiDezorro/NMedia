package ru.netology.nmedia2.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.netology.nmedia2.dao.PostDao
import ru.netology.nmedia2.entity.PostEntity


@Database(entities = [PostEntity::class], version = 1)
abstract class AppDb : RoomDatabase() {

    /** класс для хранения бд в одном экземпляре **/

    abstract fun postDao(): PostDao

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }
        private fun buildDatabase(context: Context) = Room
            .databaseBuilder(context, AppDb::class.java, name = "app.db")  // (context, класс библиотеки, имя файла)
            .allowMainThreadQueries() /** Метод для работы с бд в основном потоке **/
            .build()

    }
}
//        /** DbHelper - сборка базы **/
//        class DbHelper(
//            context: Context,
//            dbVersion: Int,
//            dbName: String,
//            private val DDLs: Array<String>
//        ) :
//            SQLiteOpenHelper(context, dbName, null, dbVersion) {
//            override fun onCreate(db: SQLiteDatabase) {
//                DDLs.forEach {
//                    db.execSQL(it)
//                }
//            }
//
//            override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
//                TODO("Not implemented")
//            }
//
//            override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
//                TODO("Not implemented")
//            }
//        }

