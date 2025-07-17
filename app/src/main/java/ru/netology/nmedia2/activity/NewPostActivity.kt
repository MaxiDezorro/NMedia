package ru.netology.nmedia2.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia2.R
import ru.netology.nmedia2.databinding.AcNewPostBinding

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.ac_new_post)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        val binding = AcNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.savePost.show()

        binding.savePost.setOnClickListener { // обрабатываем нажатие на кнопку
            val intent = Intent()
            if (binding.edit.text.isNullOrBlank()) { // проверяем текст
                setResult(Activity.RESULT_CANCELED, intent) // результат Canceled , передаем пустой интент
            } else {
                val content = binding.edit.text.toString() // получаем контент у вью
                intent.putExtra(Intent.EXTRA_TEXT, content) // кладем контент в интент по ключу
                setResult(Activity.RESULT_OK, intent)
            }
            finish() // завершаем работу активити
        }
    }
}