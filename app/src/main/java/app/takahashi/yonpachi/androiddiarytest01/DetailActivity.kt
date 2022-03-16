package app.takahashi.yonpachi.androiddiarytest01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import app.takahashi.yonpachi.androiddiarytest01.databinding.ActivityDetailBinding
import coil.api.load
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        val chatId = intent.getStringExtra("chats")

        // 日記をリアルタイム表示
        updateDiary(chatId.toString())

        // 編集ボタンアクション
        binding.editButton.setOnClickListener {
            val toUpdateDiaryActivityIntent = Intent(this, UpdateDiaryActivity::class.java)
            toUpdateDiaryActivityIntent.putExtra("chats", chatId)
            startActivity(toUpdateDiaryActivityIntent)
        }

    }

    // ユーザー情報取得
    private fun getUser(userId: String) {
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                val auth = Firebase.auth
                Log.d("tag🐣", document.id)
                binding.detailUserImageView.load(document.data?.get("photoId").toString())
                binding.detailUserTextView.text = document.data?.get("name").toString()
                // ユーザーの確認
                if (auth.currentUser?.uid.toString() != document.data?.get("uid").toString()) {
                    binding.editButton.visibility = View.INVISIBLE
                } else {
                    Log.d("tag", "あなたのアカウントです🤩")
                }
            }
            .addOnFailureListener { e ->
                Log.d("tag😶‍🌫️", "userDocumentFailed", e)
            }
    }

    // 日記情報をリアルタイム取得
    private fun updateDiary(chatId: String) {
        db.collection("chats").document(chatId)
            .addSnapshotListener { document, e ->
                if(e != null) {
                    Log.w("tag", "😇", e)
                    return@addSnapshotListener
                }

                if (document != null) {
                    getUser(document?.data?.get("userId").toString())
                    binding.detailDateTextView.text = document?.toObject(Chat::class.java)?.date?.let { SimpleDateFormat("MM月dd日(E)", Locale.JAPANESE).format(it) }
                    binding.detailDiaryTextView.text = document?.data?.get("content").toString()
                } else {
                    Log.d("tag", "Current data: nul🥑")
                }
            }

    }
}