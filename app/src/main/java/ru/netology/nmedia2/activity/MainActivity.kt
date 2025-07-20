package ru.netology.nmedia2.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.isVisible
import ru.netology.nmedia2.R
import ru.netology.nmedia2.adapter.OnInteractorListener
import ru.netology.nmedia2.adapter.PostAdapter
import ru.netology.nmedia2.databinding.ActivityMainBinding
import ru.netology.nmedia2.dto.Post
import ru.netology.nmedia2.util.AndroidUtils
import ru.netology.nmedia2.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {


    private val viewModel: PostViewModel by viewModels() //todo функция viewModels позволяет предоставить ссылку на ту же viewModel после смены конфигураций(поворот экрана)
    // после смены конфигураций, когда создается новый экземпляр MainActivity в viewModel будет ссылка на старую вьюмодель
    // viewModel будет создана только при первом обрщении к ней (Lazy - ленивая инициализация)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            ActivityMainBinding.inflate(layoutInflater) // генерируется класс ActivityMainBinding по имени нашего lauout activity_main.xml
        // в метод inflate передаем (layoutInflater) - поле класса AppCompatActivity - создает из верстки вьюшку по разметке
        setContentView(binding.root) // предаем binding, и с корневой вью передаем идентификатор root

        val editPostLauncher = registerForActivityResult(EditPostResultContract()) { content ->
                if (content == null) {
                    viewModel.clearEdit()
                } else {
                    viewModel.changeContent(content)
                    viewModel.save()
                }
            }

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
                editPostLauncher.launch(post.content)
            }

            override fun onPlayVideoIntent(post: Post) {
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.data = post.videoURL?.toUri()

                startActivity(intent)
            }
        })


        binding.list.adapter = adapter // передаем адаптер в нашу вью(лист)

        viewModel.data.observe(this) { posts -> // передает новое состояние post, когда данные изменились //todo Подписка на изменение данных
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

        val newPostLauncher =
            registerForActivityResult(NewPostResultContract()) { content ->    // регистрируем контракт
                content ?: return@registerForActivityResult // проверяем что есть контент не null
                viewModel.changeContent(content)
                viewModel.save()
            }

        binding.newPost.setOnClickListener {
            newPostLauncher.launch()  // запускаем вызов нового экрана

        }

        viewModel.edited.observe(this) { post ->
            if (post.id == 0) {
                return@observe
            }
        }
//                with(binding) {
//                    editGroup.visibility = View.VISIBLE // показываем группу редактирования
//                    content.setText(post.content) // текст редактируемого поста устанавливаем в content
//                    AndroidUtils.showKeyboard(content) // показ клавиатуры
//                    editMessageContent.text =
//                        post.content // текст редактируемого поста устанавливаем в editMessageContent
//
//                    editClose.setOnClickListener {
//                        editGroup.visibility = View.GONE // скрываем группу
//                        content.clearFocus() // убираем фокус
//                        AndroidUtils.hideKeyboard(it) // убираем клавиатуру
//                        viewModel.changeContent(editMessageContent.text.toString())
//                        viewModel.save()
//                        content.setText("")
//
//                    }
//
//
//                }
//            }
//        }
//        with(binding) {
//            savePost.setOnClickListener {
//
////                editGroup.visibility = View.GONE // почему-то не скрывает группу редактирования только когда редактируем и сохраняем
//
//                if (content.text.isNullOrBlank()) { // проверяем есть ли текст в content
//                    Toast.makeText( // всплывающее сообщение
//                        this@MainActivity,// context наша MainActivity
//                        R.string.error_empty_content, // ресурс или текст отображаемый
//                        Toast.LENGTH_SHORT // константа  время отображения сообщения
//                    ).show()
//                    return@setOnClickListener // если текст был пустой - заранее выходим из обработчика
//                }
//
//                viewModel.changeContent(content.text.toString()) // вызываем методы именения
//                viewModel.save() // и сохранения текста
//
//                editGroup.isVisible = false
////                editGroup.visibility = View.GONE // работает корректно
//
//                content.setText("")  // устанавливаем пустое поле ввода, после добавления поста
//                content.clearFocus() // убираем фокус
//                AndroidUtils.hideKeyboard(it) // скрыть клавиатуру (передаем вью)
//            }
//        }
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
