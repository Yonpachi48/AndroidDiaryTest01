package app.takahashi.yonpachi.androiddiarytest01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        val userId = intent.getStringExtra("users")

        val docUser = db.collection("users").document(userId.toString())
        docUser.get()
            .addOnSuccessListener { documentReference ->
                Log.d("tag", documentReference.id)
                getRoom(documentReference.data?.get("roomId").toString())
            }
    }

    private fun getRoom(roomId: String) {
        db.collection("rooms").document(roomId)
            .get()
            .addOnSuccessListener { document ->
                Log.d("tag", document.id)
                binding.roomNameTextView.text = document.data?.get("roomName").toString()
                binding.roomIdTextView.text = document.data?.get("roomId").toString()
            }
            .addOnFailureListener { e ->
                Log.w("tag", "😭", e)
            }
    }
}