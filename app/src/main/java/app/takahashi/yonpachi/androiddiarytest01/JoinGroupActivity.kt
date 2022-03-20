package app.takahashi.yonpachi.androiddiarytest01

import android.content.Intent
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

        binding.joinGroupButton.setOnClickListener {
            val groupId = binding.joinGroupIdEditText.text.toString()
            if(groupId != "") {
                if (checkGroup(groupId, userId.toString())) {
                    Log.d("tag" , "グループが見つかりました😇")
                    val toMainActivityIntent = Intent(this, MainActivity::class.java)
                    toMainActivityIntent.putExtra("users", userId)
                    startActivity(toMainActivityIntent)
                    finish()
                } else {
                    Log.d("tag", "グループが見つかりませんでした🤯")
                    return@setOnClickListener
                }
            } else {
                val dialog = NoneTextFragment()
                dialog.show(supportFragmentManager, "noneText")
                return@setOnClickListener
            }

        }
    }

    private fun checkGroup(groupId: String, userId: String): Boolean {
        var check: Boolean = false
        val docGroup = db.collection("groups")
            .whereEqualTo("groupId", groupId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("tag", "🏚")
                    joinGroup(groupId, userId)
                    check = true
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
            }
            .addOnFailureListener { e ->
                Log.w("tag", "😤", e)
            }
    }


}