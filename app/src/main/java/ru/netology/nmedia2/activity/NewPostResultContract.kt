package ru.netology.nmedia2.activity

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class NewPostResultContract: ActivityResultContract<Unit, String?>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(context, NewPostResultContract::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
      return  intent?.getStringExtra(Intent.EXTRA_TEXT)
    }


}