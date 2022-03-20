package app.takahashi.yonpachi.androiddiarytest01

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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

        binding.joinGroupButton.setOnClickListener {
            val groupId = binding.joinGroupIdEditText.text.toString()
            if(groupId != "") {
                checkGroup(groupId, userId.toString())
            } else {
                val dialog = NoneTextFragment()
                dialog.show(supportFragmentManager, "noneText")
            }
        }
    }

    private fun checkGroup(groupId: String, userId: String): Boolean {
        Log.d("tag", "$groupId😅")
        var check: Boolean = false
        val docGroup = db.collection("groups")
            .whereEqualTo("groupId", groupId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    check = true
                    Log.d("tag", "🏚, $check")
                    joinGroup(groupId, userId)
                    return@addOnSuccessListener
                }
                val dialog = NoneGroupFragment()
                dialog.show(supportFragmentManager, "group")
                check = false
            }
            .addOnFailureListener { e->
                Log.w("tag", "🤐", e)
                check = false
            }
        return check
    }

    private fun joinGroup(groupId: String, userId: String) {
        val docUser = db.collection("users").document(userId)
            .update("groupId", groupId)
            .addOnSuccessListener {
                Log.d("tag", "🍳${groupId}")
                val toMainActivityIntent = Intent(this, MainActivity::class.java)
                toMainActivityIntent.putExtra("users", userId)
                toMainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(toMainActivityIntent)
            }
            .addOnFailureListener { e ->
                Log.w("tag", "😤", e)
            }
    }


}