package ru.netology.nmedia2.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia2.R
import ru.netology.nmedia2.databinding.FragmentNewEditPostBinding
import ru.netology.nmedia2.fragment.FeedFragment.Companion.textArgs
import ru.netology.nmedia2.util.AndroidUtils
import ru.netology.nmedia2.viewmodel.PostViewModel

class NewEditPostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewEditPostBinding.inflate(
            inflater,
            container,
            false
        )
        val viewModel: PostViewModel by viewModels(::requireParentFragment)

//        arguments?.textArgs.let(binding.edit::setText) // вызываем метод setText на аргументе через ссылку

        arguments?.textArgs.let {
         binding.edit.setText(it)
        }

        binding.edit.requestFocus()
        AndroidUtils.showKeyboard(binding.edit)

        binding.savePost.setOnClickListener { // обрабатываем нажатие на кнопку
            if (binding.edit.text.isNotBlank()) { // проверяем текст

                val content = binding.edit.text.toString()
                viewModel.changeContent(content)
                viewModel.save()
            } else {
                Toast.makeText(context, R.string.error_empty_content, Toast.LENGTH_LONG).show()
            }


            findNavController().navigateUp() // возвращаемся по графу на предыдущий экран
        }
        return binding.root

    }
}