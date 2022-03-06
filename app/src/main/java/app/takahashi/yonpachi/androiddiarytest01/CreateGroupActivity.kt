package app.takahashi.yonpachi.androiddiarytest01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import app.takahashi.yonpachi.androiddiarytest01.databinding.ActivityCreateGroupBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CreateGroupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateGroupBinding
    private lateinit var rName: String
    private lateinit var rId: String
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateGroupBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        val userId = intent.getStringExtra("users")

        binding.createGroupButton.setOnClickListener {
            rName = binding.groupNameEditText.text.toString()
            rId = binding.groupIdEditText.text.toString()

            if (rName != null && rId != null) {
                val room = Room(
                    roomName = rName,
                    roomId = rId,
                    chats = null
                )
                db.collection("rooms")
                    .add(room)
                    .addOnSuccessListener { documentReference ->
                        Log.d("tag", "DocumentSnapshot added with ID: ${documentReference.id}")
                        addRoomId(documentReference.id, userId.toString())
                        val toMainActivityIntent = Intent(this, MainActivity::class.java)
                        toMainActivityIntent.putExtra("users", userId)
                        startActivity(toMainActivityIntent)
                    }
            } else return@setOnClickListener
        }
    }

    private fun addRoomId(roomId: String, userId: String){
        val docUser = db.collection("users").document(userId)
        docUser.update("roomId", roomId)
            .addOnSuccessListener {
                Log.d("tag", "😀")
            }
            .addOnFailureListener { e->
                Log.w("tag", "😭", e)
            }
    }
}