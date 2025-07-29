package ru.netology.nmedia2.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia2.R
import ru.netology.nmedia2.fragment.showHowManyIntToString
import ru.netology.nmedia2.databinding.CardPostBinding
import ru.netology.nmedia2.dto.Post

//typealias OnLikeListener = (post: Post) -> Unit // возвращает пост и ничего не принимает
//typealias OnShareListener = (post: Post) -> Unit
//typealias OnRemoveListener = (post: Post) -> Unit

interface OnInteractorListener {
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onRemove(post: Post)
    fun onEdit(post: Post)
    fun onPlayVideoIntent(post: Post)
    fun onOnePostOpen(post: Post)
}

class PostAdapter(

    private val onInteractorListener: OnInteractorListener
) :
    ListAdapter<Post, PostViewHolder>(PostDiffCallBack) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostViewHolder { // вызывается каждый раз когда создается новая карточка

        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PostViewHolder(binding, onInteractorListener) // создали новый PostViewHolder
    }

    override fun onBindViewHolder(
        holder: PostViewHolder,
        position: Int
    ) { // переиспользуем карточки для новых постов
        val post = getItem(position) // находим нужный пост по позиции
        holder.bind(post) // binding данных для карточки
    }

}

class PostViewHolder(
    private val binding: CardPostBinding, // передаем binding (разметку) нашей карточки из todo onCreateViewHolder
    private val onInteractorListener: OnInteractorListener


) : RecyclerView.ViewHolder(binding.root) { // в конструктор вьюхолдера передаем корневую вью карточки
    fun bind(post: Post) {
        binding.bind(post, onInteractorListener)
    }
//    fun bind(post: Post) =  // заполняем карточку данными поста
//        with(binding) { // обращаемся ко всем полям binding (вместо binding.author.text итд )
//            author.text = post.author
//            content.text = post.content
//            published.text = post.published
//
//            viewsAmount.text = showHowManyIntToString(post.countViews)
//
//            like.apply {
//                isChecked = post.likeByMe
//                text = showHowManyIntToString(post.countLikes)
//            }
//            // без apply так         \\  или с  with(like) {
////            like.isChecked =         \\  isChecked =
////            like.text =               \\  text =  }
//
//            share.text = showHowManyIntToString(post.countShare)
//
//            if (post.videoURL != null) {
//                videoLayout.visibility = View.VISIBLE
//            } else {
//                videoLayout.visibility = View.GONE
//            }
//
//            videoLayout.setOnClickListener {
//                onInteractorListener.onPlayVideoIntent(post)
//            }
//
//
//            like.setOnClickListener { // дергаем лямбду
//                onInteractorListener.onLike(post)
//            }
//            share.setOnClickListener {
//                onInteractorListener.onShare(post)
//            }
//            menu.setOnClickListener {
//                PopupMenu(
//                    it.context,
//                    it
//                ).apply { // передаем в popup(контекст вью, и место появления с нашей вью)
//
//                    inflate(R.menu.post_menu) // инфлейтим наше меню
//                    setOnMenuItemClickListener { item -> // item из post_menu.xml
//                        when (item.itemId) {
//                            R.id.remove -> {
//                                onInteractorListener.onRemove(post)
//                                true
//                            }
//
//                            R.id.edit -> {
//                                onInteractorListener.onEdit(post)
//                                true
//                            }
//
//                            else -> false
//                        }
//                    }
//                }.show()
//            }
//
//            content.setOnClickListener {
//                onInteractorListener.onOnePostOpen(post)
//            }
//
//        }


}

object PostDiffCallBack : DiffUtil.ItemCallback<Post>() {   //  отвечает за сравнение списков
    // по этому всегда создаем новый список, а не редактируем имеющийся
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id // сравниваем посты по id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem // сравниваем содержимое постов, не изменились ли данные
    }
}

fun CardPostBinding.bind(post: Post, onInteractorListener: OnInteractorListener) {
    author.text = post.author
    content.text = post.content
    published.text = post.published

    viewsAmount.text = showHowManyIntToString(post.countViews)

    like.apply {
        isChecked = post.likeByMe
        text = showHowManyIntToString(post.countLikes)
    }

    share.text = showHowManyIntToString(post.countShare)

    if (post.videoURL != null) {
        videoLayout.visibility = View.VISIBLE
    } else {
        videoLayout.visibility = View.GONE
    }

    videoLayout.setOnClickListener {
        onInteractorListener.onPlayVideoIntent(post)
    }

    like.setOnClickListener {
        onInteractorListener.onLike(post)
    }

    share.setOnClickListener {
        onInteractorListener.onShare(post)
    }

    menu.setOnClickListener {
        PopupMenu(it.context, it).apply {
            inflate(R.menu.post_menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.remove -> {
                        onInteractorListener.onRemove(post)
                        true
                    }

                    R.id.edit -> {
                        onInteractorListener.onEdit(post)
                        true
                    }

                    else -> false
                }
            }
        }.show()
    }

    content.setOnClickListener {
        onInteractorListener.onOnePostOpen(post)
    }
}