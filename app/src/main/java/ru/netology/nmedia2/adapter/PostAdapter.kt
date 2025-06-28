package ru.netology.nmedia2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia2.R
import ru.netology.nmedia2.activity.showHowManyIntToString
import ru.netology.nmedia2.databinding.CardPostBinding
import ru.netology.nmedia2.dto.Post

typealias OnLikeListener = (post: Post) -> Unit // возвращает пост и ничего не принимает
typealias OnShareListener = (post: Post) -> Unit

class PostAdapter(
    private val onLikeListener: OnLikeListener,
    private val onShareListener: OnShareListener
) :
    ListAdapter<Post, PostViewHolder>(PostDiffCallBack) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder { // вызывается каждый раз когда создается новая карточка
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onLikeListener, onShareListener) // создали новый PostViewHolder
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) { // переиспользуем карточки для новых постов
        val post = getItem(position) // находим нужный пост по позиции
        holder.bind(post) // binding данных для карточки
    }

}

class PostViewHolder(
    private val binding: CardPostBinding, // передаем binding (разметку) нашей карточки из todo onCreateViewHolder
    private val onLikeListener: OnLikeListener,
    private val onShareListener: OnShareListener


) : RecyclerView.ViewHolder(binding.root) { // в конструктор вьюхолдера передаем корневую вью карточки
    fun bind(post: Post) =  // заполняем карточку данными поста
        with(binding) { // обращаемся ко всем полям binding (вместо binding.author.text итд )
            author.text = post.author
            content.text = post.content
            published.text = post.published
            likesAmount.text = showHowManyIntToString(post.countLikes)
            shareAmount.text = showHowManyIntToString(post.countShare)
            viewsAmount.text = showHowManyIntToString(post.countViews)

            like.setImageResource(
                if (post.likeByMe) {
                    R.drawable.ic_like_red_24
                } else {
                    R.drawable.ic_like_24
                }
            )
            like.setOnClickListener { // дергаем лямбду
                onLikeListener(post)
            }
            share.setOnClickListener {
                onShareListener(post)
            }
        }
}

object PostDiffCallBack : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id // сравниваем посты по id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem // сравниваем содержимое постов, не изменились ли данные
    }
}