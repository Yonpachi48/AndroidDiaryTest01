package app.takahashi.yonpachi.androiddiarytest01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import app.takahashi.yonpachi.androiddiarytest01.databinding.ActivityUpdateDiaryBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UpdateDiaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateDiaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateDiaryBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        // chatの取得
        val chatId = intent.getStringExtra("chats")
        val chats = Firebase.firestore.collection("chats").document(chatId.toString())
        chats.get()
            .addOnSuccessListener { document ->
                // editTextの初期値入力
                binding.updateDiaryTextEdit.setText(document.data?.get("content").toString())
            }
            .addOnFailureListener { e ->
                Log.d("tag","🤯", e)
            }

        // ボタンアクション
        binding.updateDairyButton.setOnClickListener {
            var updateText: String = ""
            updateText = binding.updateDiaryTextEdit.text.toString()

            if (updateText != "") {
                chats
                    .update("content", updateText)
                    .addOnSuccessListener { document ->
                        Log.d("tag", "😲updateDocument: $document")
                    }
                    .addOnFailureListener { e ->
                        Log.d("tag", "🥳", e)
                    }
                finish()
            } else {
                val dialog = NoneTextFragment()
                dialog.show(supportFragmentManager, "noneText")
                return@setOnClickListener
            }
        }
    }
}