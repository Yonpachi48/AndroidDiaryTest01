package app.takahashi.yonpachi.androiddiarytest01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import app.takahashi.yonpachi.androiddiarytest01.databinding.ActivityMainBinding
import com.google.firebase.firestore.Query
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
        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager(this).getOrientation())
        binding.recyclerView.addItemDecoration(dividerItemDecoration)
        val diaryAdapter = DiaryAdapter()
        binding.recyclerView.adapter = diaryAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // cellの表示
        updateChats(diaryAdapter)

        // cellのクリックイベント
        diaryAdapter.setOnClickListener(
            object: DiaryAdapter.OnItemClickListener {
                override fun onClick(view: View, chat: Chat) {
                    val context = view.context

                    val toDetailActivityIntent = Intent(context, DetailActivity::class.java)
                    toDetailActivityIntent.putExtra("chats", chat.id)
                    startActivity(toDetailActivityIntent)

                }
            }
        )

        // ユーザー情報の取得
        val userId = intent.getStringExtra("users")
        val docUser = db.collection("users").document(userId.toString())
        docUser.get()
            .addOnSuccessListener { documentReference ->    // ユーザー情報の使用
                Log.d("tag1", "${documentReference.id}, ${documentReference.data?.get("groupId").toString()}")
                getGroupId(documentReference.data?.get("groupId").toString())
            }

        // 追加ボタンアクション
        binding.addButton.setOnClickListener {
            Log.d("tag", "📱")
            val toAddDiaryActivityIntent = Intent(this, AddDiaryActivity::class.java)
            toAddDiaryActivityIntent.putExtra("users", userId)
            startActivity(toAddDiaryActivityIntent)
        }
    }

    // グループ情報の使用
    private fun getGroup(groupId: String) {
        Log.d("tag", "groupId:$groupId, 🤢")
        db.collection("groups").document(groupId)
            .get()
            .addOnSuccessListener { document ->
                Log.d("tag", "🐒, $groupId")
                    Log.d("tag😘", document.id)
                    binding.topAppBar.title = document.data?.get("groupName").toString()
                    return@addOnSuccessListener
            }
            .addOnFailureListener { e ->
                Log.w("tag", "😭", e)
            }
    }

    private fun getGroupId(groupId: String) {
        db.collection("groups")
            .whereEqualTo("groupId", groupId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("tag", "documentId: ${document.id}")
                    getGroup(document.id)
                }
            }
            .addOnFailureListener { e ->
                Log.d("tag", "groupIdが取得できませんでした", e)
            }
    }

    private fun updateChats(diaryAdapter: DiaryAdapter) {
        db.collection("chats").orderBy("date", Query.Direction.ASCENDING)
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