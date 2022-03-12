package app.takahashi.yonpachi.androiddiarytest01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import app.takahashi.yonpachi.androiddiarytest01.databinding.ActivityDetailBinding
import coil.api.load
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        val chatId = intent.getStringExtra("chats")

        db.collection("chats").document(chatId.toString())
            .get()
            .addOnSuccessListener { document ->
                Log.d("tag1🐣", document.data?.get("date").toString())
                Log.d("tag2🐣", "あ".javaClass.toString())
                getUser(document.data?.get("userId").toString())
                binding.detailDateTextView.text = document.toObject(Chat::class.java)?.date?.let { SimpleDateFormat("MM月dd日(E)", Locale.JAPANESE).format(it) }
                binding.detailDiaryTextView.text = document.data?.get("content").toString()
            }
            .addOnFailureListener { e ->
                Log.d("tag😶‍🌫️", "chatDocumentFailed", e)
            }

        binding.editButton.setOnClickListener {
        }

    }

    private fun getUser(userId: String) {
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                Log.d("tag🐣", document.id)
                binding.detailUserImageView.load(document.data?.get("photoId").toString())
                binding.detailUserTextView.text = document.data?.get("name").toString()
            }
            .addOnFailureListener { e ->
                Log.d("tag😶‍🌫️", "userDocumentFailed", e)
            }
    }
}