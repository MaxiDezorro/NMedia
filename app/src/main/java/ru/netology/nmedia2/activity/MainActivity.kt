package ru.netology.nmedia2.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia2.adapter.PostAdapter
import ru.netology.nmedia2.databinding.ActivityMainBinding
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

        val adapter = PostAdapter({ post ->
            viewModel.likeById(post.id)
        }, { post ->
            viewModel.shareById(post.id)
        })
        binding.list.adapter = adapter // передаем адаптер в нашу вью(лист)

        viewModel.data.observe(this) { posts -> // передает новое состояние post, когда данные изменились //todo Подписка на изменение данных
//            поле data из репозитория, observe получает activity
            adapter.submitList(posts) // обновляем данные

        }
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
