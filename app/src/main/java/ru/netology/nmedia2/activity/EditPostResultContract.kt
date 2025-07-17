package ru.netology.nmedia2.activity

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class EditPostResultContract : ActivityResultContract<String, String?>() {
    override fun createIntent(context: Context, input: String): Intent =
        Intent(context, EditPostActivity::class.java).putExtra(Intent.EXTRA_TEXT, input)
    // тоже самое
//    {
//        val intent = Intent(context, EditPostActivity::class.java)
//        intent.putExtra(Intent.EXTRA_TEXT, input)
//        return intent
//    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        return intent?.getStringExtra(Intent.EXTRA_TEXT)
    }
}