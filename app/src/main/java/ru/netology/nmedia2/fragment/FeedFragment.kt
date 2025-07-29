package ru.netology.nmedia2.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia2.R
import ru.netology.nmedia2.adapter.OnInteractorListener
import ru.netology.nmedia2.adapter.PostAdapter
import ru.netology.nmedia2.databinding.FragmentFeedBinding
import ru.netology.nmedia2.dto.Post
import ru.netology.nmedia2.util.SringArg
import ru.netology.nmedia2.viewmodel.PostViewModel

class FeedFragment : Fragment() {


    private val viewModel: PostViewModel by viewModels(::requireParentFragment) //todo функция viewModels позволяет предоставить ссылку на ту же viewModel после смены конфигураций(поворот экрана)
    // после смены конфигураций, когда создается новый экземпляр MainActivity в viewModel будет ссылка на старую вьюмодель
    // viewModel будет создана только при первом обрщении к ней (Lazy - ленивая инициализация)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding =
            FragmentFeedBinding.inflate(
                inflater,
                container,
                false
            ) // генерируется класс по имени нашего lauout .xml


        val adapter = PostAdapter(object : OnInteractorListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                // не явный интент
                val intent = Intent().apply { // создаем интент и настраиваем через apply
                    action = Intent.ACTION_SEND // константа означающая что отправляем данные
                    putExtra(Intent.EXTRA_TEXT, post.content)// кладем в интент
                    // (ключ для передаваемых данных(EXTRA_TEXT) и данные(контент поста))
                    type = "text/plain" //означает что передаем стандартный тип текстового поля
                }
                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_post))
                // создаем интент на показ Choser'a(меню выбора), куда передаем (интент и стринг(сообщение в меню(название))

                startActivity(shareIntent) // метод активити куда передаем наш интент

                viewModel.shareById(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                val text = post.content
                findNavController().navigate(
                    R.id.action_feedFragment_to_newPostFragment,
                    Bundle().apply {
                        textArgs = text
                    })

            }

            override fun onPlayVideoIntent(post: Post) {
                val intent = Intent()  // Создаем новый не явный Intent
                intent.action = Intent.ACTION_VIEW // Указываем действие "просмотр"
                intent.data = post.videoURL?.toUri() // Преобразуем URL видео в Uri
                startActivity(intent)
            }

            override fun onOnePostOpen(post: Post) {
                viewModel.viewById(post.id)
                findNavController().navigate(
                    R.id.action_feedFragment_to_onePostFragment,
                    Bundle().apply {
                        putInt("postId", post.id) // Теперь Bundle передается с навигацией
                    }
                )
            }
        })


        binding.list.adapter = adapter // передаем адаптер в нашу вью(лист)

        viewModel.data.observe(viewLifecycleOwner) { posts -> // передает новое состояние post, когда данные изменились //todo Подписка на изменение данных
//            поле data из репозитория, observe получает activity
            val newPost =
                posts.size > adapter.itemCount // проверяем, изменилось ли количество элементов в списке posts
            // по сравнению с текущим количеством элементов в адаптере adapter.itemCount.

            adapter.submitList(posts) {// обновляем данные
                if (newPost) {
                    binding.list.smoothScrollToPosition(0) // плавный скрол по позиции
                }
            }
        }



        binding.newPost.setOnClickListener {
            viewModel.clearEdit()
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)  // запускаем вызов нового экрана

        }


        viewModel.edited.observe(viewLifecycleOwner) { post ->
            if (post.id == 0) {
                return@observe
            }

        }
        return binding.root
    }

    companion object {
        var Bundle.textArgs by SringArg
    }
}


fun showHowManyIntToString(number: Int): String {
    val thousand = "K"
    val million = "M"

    fun formatSmall(value: Double, suffix: String): String {
        val str = value.toString()
        return when {
            str.getOrNull(2)?.let { it != '0' } == true -> "${str[0]}.${str[2]}"
            else -> str[0].toString()
        } + suffix
    }

    fun formatMedium(value: Double, suffix: String): String {
        val str = value.toString()
        return "${str[0]}${str[1]}$suffix"
    }

    fun formatLarge(value: Double, suffix: String): String {
        val str = value.toString()
        return "${str[0]}${str[1]}${str[2]}$suffix"
    }

    return when {
        number <= 999 -> number.toString()

        number < 10_000 -> {
            val value = number.toDouble() / 1000
            if (value in 1.0..10.0) formatSmall(value, thousand) else number.toString()
        }

        number < 100_000 -> {
            val value = number.toDouble() / 1000
            if (value in 1.0..100.0) formatMedium(value, thousand) else number.toString()
        }

        number < 1_000_000 -> {
            val value = number.toDouble() / 1000
            if (value in 1.0..1000.0) formatLarge(value, thousand) else number.toString()
        }

        number < 10_000_000 -> {
            val value = number.toDouble() / 1_000_000
            if (value in 1.0..10.0) formatSmall(value, million) else number.toString()
        }

        number < 100_000_000 -> {
            val value = number.toDouble() / 1_000_000
            if (value in 1.0..100.0) formatMedium(value, million) else number.toString()
        }

        else -> {
            val value = number.toDouble() / 1_000_000
            if (value in 1.0..1000.0) formatLarge(value, million) else number.toString()
        }
    }
}
