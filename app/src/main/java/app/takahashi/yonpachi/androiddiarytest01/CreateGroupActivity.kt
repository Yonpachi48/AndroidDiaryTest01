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
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateGroupBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        val userId = intent.getStringExtra("users")

        binding.createGroupButton.setOnClickListener {
            val rName = binding.groupNameEditText.text.toString()
            val rId = binding.groupIdEditText.text.toString()

            if (rName != "" && rId != "") {
                val group = Group(
                    groupName = rName,
                    groupId = rId,
                    chats = null
                )
                checkGroups(group, userId.toString())
            } else {
                val dialog = NoneTextFragment()
                dialog.show(supportFragmentManager, "noneText")
                return@setOnClickListener
            }
        }
    }

    private fun addGroupId(groupId: String, userId: String){
        val docUser = db.collection("users").document(userId)
        docUser.update("groupId", groupId)
            .addOnSuccessListener {
                Log.d("tag", "😀")
            }
            .addOnFailureListener { e->
                Log.w("tag", "😭", e)
            }
    }

    private fun checkGroups(group: Group, userId: String) {
        db.collection("groups")
            .whereEqualTo("groupId",group.groupId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("tag", "🏚")
                    val dialog = GroupFragment()
                    dialog.show(supportFragmentManager, "group")
                    return@addOnSuccessListener
                }
                addGroups(group, userId)
            }
            .addOnFailureListener { e ->
                Log.w("tag", "😭", )
            }
    }

    private fun addGroups(group: Group, userId: String) {
        db.collection("groups")
            .add(group)
            .addOnSuccessListener { documentReference ->
                Log.d("tag", "DocumentSnapshot added with ID: ${documentReference.id}")
                addGroupId(documentReference.id, userId)
                val toMainActivityIntent = Intent(this, MainActivity::class.java)
                toMainActivityIntent.putExtra("users", userId)
                startActivity(toMainActivityIntent)
            }
    }
}