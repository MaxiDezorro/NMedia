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
import ru.netology.nmedia2.adapter.bind
import ru.netology.nmedia2.databinding.FragmentSinglePostBinding
import ru.netology.nmedia2.dto.Post
import ru.netology.nmedia2.fragment.FeedFragment.Companion.textArgs
import ru.netology.nmedia2.viewmodel.PostViewModel

class OnePostFragment : Fragment() {
    val viewModel: PostViewModel by viewModels(::requireParentFragment)
    lateinit var binding: FragmentSinglePostBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSinglePostBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
val onInteractorListener = object: OnInteractorListener {
    override fun onLike(post: Post) {
        viewModel.likeById(post.id)
    }

    override fun onShare(post: Post) {
        // не явный интент
        val intent = Intent()
            intent.action = Intent.ACTION_SEND // константа означающая что отправляем данные
            intent.putExtra(Intent.EXTRA_TEXT, post.content)// кладем в интент
            // (ключ для передаваемых данных(EXTRA_TEXT) и данные(контент поста))
            intent.type = "text/plain" //означает что передаем стандартный тип текстового поля

        val shareIntent =
            Intent.createChooser(intent, getString(R.string.chooser_share_post))
        // создаем интент на показ Choser'a(меню выбора), куда передаем (интент и стринг(сообщение в меню(название))

        startActivity(shareIntent) // метод активити куда передаем наш интент

        viewModel.shareById(post.id)
    }

    override fun onRemove(post: Post) {
        viewModel.removeById(post.id)
        findNavController().navigateUp()
    }

    override fun onEdit(post: Post) {
        viewModel.edit(post)
        val text = post.content
        findNavController().navigate(
            R.id.action_onePostFragment_to_newPostFragment,
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

    override fun onOnePostOpen(post: Post) { }

}

        val postId = arguments?.getInt("postId") ?: 0

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == postId } ?: return@observe
            binding.postOne.bind(post, onInteractorListener)
//            with(binding.postOne) {
//                author.text = post.author
//                content.text = post.content
//                published.text = post.published
//
//                viewsAmount.text = showHowManyIntToString(post.countViews)
//
//                like.apply {
//                    isChecked = post.likeByMe
//                    text = showHowManyIntToString(post.countLikes)
//                }
//
//
//                share.text = showHowManyIntToString(post.countShare)
//
//                if (post.videoURL != null) {
//                    videoLayout.visibility = View.VISIBLE
//                } else {
//                    videoLayout.visibility = View.GONE
//                }
//            }
        }
    }

}