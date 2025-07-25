package ru.netology.nmedia2.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia2.databinding.FragmentNewPostBinding
import ru.netology.nmedia2.util.AndroidUtils

class NewPostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

//        enableEdgeToEdge()
//        setContentView(R.layout.ac_new_post)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        val binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )

        AndroidUtils.showKeyboard(binding.edit)

        binding.savePost.setOnClickListener { // обрабатываем нажатие на кнопку
            val intent = Intent()
            if (binding.edit.text.isNullOrBlank()) { // проверяем текст
//                Toast.makeText(this@NewPostFragment, R.string.error_empty_content, Toast.LENGTH_SHORT).show()
//                setResult(Activity.RESULT_CANCELED, intent) // результат Canceled , передаем пустой интент
            } else {
                val content = binding.edit.text.toString() // получаем контент у вью
                intent.putExtra(Intent.EXTRA_TEXT, content) // кладем контент в интент по ключу
//                setResult(Activity.RESULT_OK, intent)
            }
//            finish() // завершаем работу активити
            findNavController().navigateUp() // возвращаемся по графу на предидущий экран
        }
        return binding.root
    }
}