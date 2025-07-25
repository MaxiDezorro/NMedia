package ru.netology.nmedia2.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia2.activity.FeedFragment.Companion.textArgs
import ru.netology.nmedia2.databinding.FragmentNewPostBinding
import ru.netology.nmedia2.util.AndroidUtils
import ru.netology.nmedia2.viewmodel.PostViewModel
import kotlin.getValue

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
        val viewModel: PostViewModel by viewModels(::requireParentFragment)

        arguments?.textArgs.let {
            binding.edit.setText(it)
        }


        binding.edit.requestFocus()
        AndroidUtils.showKeyboard(binding.edit)
        binding.savePost.setOnClickListener { // обрабатываем нажатие на кнопку
            if (binding.edit.text.isNotBlank()) { // проверяем текст
//                Toast.makeText(this@NewPostFragment, R.string.error_empty_content, Toast.LENGTH_SHORT).show()
//
                val content = binding.edit.text.toString()
                viewModel.changeContent(content)
                viewModel.save()
            }
//            finish() // завершаем работу активити
            findNavController().navigateUp() // возвращаемся по графу на предидущий экран
        }
        return binding.root
    }
}