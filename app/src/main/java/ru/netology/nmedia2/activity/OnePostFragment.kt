package ru.netology.nmedia2.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.netology.nmedia2.databinding.FragmentSinglePostBinding
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


        val postId = arguments?.getInt("postId") ?: 0

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == postId } ?: return@observe
            with(binding.postOne) {
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

            }
        }
    }

}
