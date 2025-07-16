package ru.netology.nmedia2.activity

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

/*
ActivityResultContract специальный класс который работает с интентами
 */
class NewPostResultContract :
    ActivityResultContract<Unit, String?>() {  //Unit - данные для отправки,String - данные для получения
    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(context, NewPostActivity::class.java)
        // создаем новый интент(передаем в конструктор context и ссылку на класс активити(которое запускаем этим интентом))
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        return intent?.getStringExtra(Intent.EXTRA_TEXT) // обрабатываем результат, олучаем наш текст по ключу
    }

}