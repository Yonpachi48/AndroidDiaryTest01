package app.takahashi.yonpachi.androiddiarytest01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import app.takahashi.yonpachi.androiddiarytest01.databinding.ActivityAddDiaryBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*


class AddDiaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddDiaryBinding
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDiaryBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        val date: Date = Date(System.currentTimeMillis())

        binding.addDateTextView.text = SimpleDateFormat("yyyy年MM月dd日E曜日", Locale.JAPANESE).format(date)

        val userId = intent.getStringExtra("users")

        binding.addDairyButton.setOnClickListener {
            val diaryText = binding.addDiaryTextEdit.text.toString()
            if (diaryText != "") {
                val chat = Chat(
                    userId = userId.toString(),
                    content = diaryText
                )
                db.collection("chats")
                    .add(chat)
                    .addOnSuccessListener { document ->
                        Log.d("tag", "🙋🏻DocumentID: ${document.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.d("tag", "Error adding document🥶", e)
                    }
            } else {
                val dialog = NoneTextFragment()
                dialog.show(supportFragmentManager, "noneText")
                return@setOnClickListener
            }

            finish()
        }
    }
}