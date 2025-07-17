package ru.netology.nmedia2.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia2.R
import ru.netology.nmedia2.databinding.AcEditBinding
import ru.netology.nmedia2.util.AndroidUtils

class EditPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.ac_edit)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        val binding = AcEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AndroidUtils.showKeyboard(binding.edit)
        val content = intent.getStringExtra(Intent.EXTRA_TEXT)
        binding.edit.setText(content)


        binding.savePost.setOnClickListener {
            val intent = Intent()
            if (binding.edit.text.isNullOrBlank()) {
                setResult(RESULT_CANCELED, intent)
            } else {
                val content = binding.edit.text.toString()
                intent.putExtra(Intent.EXTRA_TEXT, content)
                setResult(RESULT_OK, intent)
            }

            finish()
        }

    }

}