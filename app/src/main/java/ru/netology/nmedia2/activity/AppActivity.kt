package ru.netology.nmedia2.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia2.R
import ru.netology.nmedia2.fragment.FeedFragment.Companion.textArgs
import ru.netology.nmedia2.databinding.ActivityAppBinding

class AppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        enableEdgeToEdge()

        val binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }



        // обработка входящего не явного интента
        intent?.let {
            if (it.action != Intent.ACTION_SEND) return@let // выхходим если action не ACTION_SEND

            val text =
                it.getStringExtra(Intent.EXTRA_TEXT) // записываем текст из интента по ключу EXTRA_TEXT
            if (text.isNullOrBlank()) { // если текст пустой или из пробелов или null создаем снекбар
                Snackbar.make(binding.root, R.string.error_empty_content, Snackbar.LENGTH_INDEFINITE)
                                // вьюшка на которой вызываем, текст сообщения, время показа

                    .setAction(android.R.string.ok) { // устанавливаем текст кнопки и  обрабатываем нажатие на кнопку
                        finish() // завершаем активити
                    }.show()
                return@let
            }

            findNavController(R.id.nav_host_fragment).navigate(
                R.id.action_feedFragment_to_newPostFragment,
                Bundle().apply {
                    textArgs = text
                }
            )
        }

    }
}