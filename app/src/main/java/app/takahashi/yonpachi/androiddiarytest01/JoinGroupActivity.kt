package app.takahashi.yonpachi.androiddiarytest01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import app.takahashi.yonpachi.androiddiarytest01.databinding.ActivityJoinGroupBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class JoinGroupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJoinGroupBinding
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinGroupBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        val userId = intent.getStringExtra("users")
        val docUser = db.collection("users").document(userId.toString())

        val groupId = binding.joinGroupIdEditText.text.toString()
        binding.joinGroupButton.setOnClickListener {
            if(groupId != "") {
                checkGroup(userId.toString())
            }
        }
    }

    private fun checkGroup(groupId: String) {
        val docUser = db.collection("users")
        docUser
            .whereEqualTo("roomId", groupId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("tag", "🏚")

                }
                val dialog = NoneTextFragment()
                dialog.show(supportFragmentManager, "group")
            }
            .addOnFailureListener {

            }
    }

    private fun joinGroup(groupId: String) {

    }


}