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

            if (rName != "" && rId != "") {
                val room = Room(
                    roomName = rName,
                    roomId = rId,
                    chats = null
                )
                checkRooms(room, userId.toString())
            } else {
                val dialog = NoneTextFragment()
                dialog.show(supportFragmentManager, "noneText")
                return@setOnClickListener
            }
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

    private fun checkRooms(room: Room, userId: String) {
        db.collection("rooms")
            .whereEqualTo("roomId",room.roomId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("tag", "🏚")
                    val dialog = RoomFragment()
                    dialog.show(supportFragmentManager, "room")
                    return@addOnSuccessListener
                }
                addRooms(room, userId)
            }
            .addOnFailureListener { e ->
                Log.w("tag", "😭", )
            }
    }

    private fun addRooms(room: Room, userId: String) {
        db.collection("rooms")
            .add(room)
            .addOnSuccessListener { documentReference ->
                Log.d("tag", "DocumentSnapshot added with ID: ${documentReference.id}")
                addRoomId(documentReference.id, userId)
                val toMainActivityIntent = Intent(this, MainActivity::class.java)
                toMainActivityIntent.putExtra("users", userId)
                startActivity(toMainActivityIntent)
            }
    }
}