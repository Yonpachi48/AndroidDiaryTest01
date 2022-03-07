package app.takahashi.yonpachi.androiddiarytest01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import app.takahashi.yonpachi.androiddiarytest01.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        // RecyclerViewの設定
        val diaryAdapter = DiaryAdapter()
        binding.recyclerView.adapter = diaryAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // cellの表示
        getChats(diaryAdapter)
        updateChats(diaryAdapter)

        // cellのクリックイベント
        diaryAdapter.setOnClickListener(
            object: DiaryAdapter.OnItemClickListener {
                override fun onClick(view: View, chat: Chat) {
                    val context = view.context

                }
            }
        )

        // ユーザー情報の取得
        val userId = intent.getStringExtra("users")
        val docUser = db.collection("users").document(userId.toString())
        docUser.get()
            .addOnSuccessListener { documentReference ->
                Log.d("tag1", "${documentReference.id}, ${documentReference.data?.get("groupId").toString()}")
                getGroup(documentReference.data?.get("groupId").toString())
            }

        binding.addButton.setOnClickListener {
            Log.d("tag", "📱")
            val toAddDiaryActivityIntent = Intent(this, AddDiaryActivity::class.java)
            toAddDiaryActivityIntent.putExtra("users", userId)
            startActivity(toAddDiaryActivityIntent)
        }

    }

    private fun getGroup(groupId: String) {
        db.collection("groups").document(groupId)
            .get()
            .addOnSuccessListener { document ->
                Log.d("tag", document.id)

            }
            .addOnFailureListener { e ->
                Log.w("tag", "😭", e)
            }
    }

    private fun getChats(diaryAdapter: DiaryAdapter) {
        db.collection("chats")
            .get()
            .addOnSuccessListener { documents ->
                val diaryList = ArrayList<Chat>()
                documents.forEach { diaryList.add(it.toObject(Chat::class.java)) }
                diaryAdapter.submitList(diaryList)
            }
            .addOnFailureListener { e ->
                Log.d("tag", "🐣", e)
            }
    }

    private fun updateChats(diaryAdapter: DiaryAdapter) {
        db.collection("chats")
            .addSnapshotListener { documents, e ->
                if(e != null) {
                    Log.w("tag", "😇", e)
                    return@addSnapshotListener
                }

                if(documents != null) {
                    val diaryList = ArrayList<Chat>()
                    documents.forEach { diaryList.add(it.toObject(Chat::class.java)) }
                    diaryAdapter.submitList(diaryList)
                } else {
                    Log.d("tag", "Current data: nul🥑")
                }
            }

    }
}