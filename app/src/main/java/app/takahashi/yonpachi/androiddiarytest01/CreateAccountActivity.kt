package app.takahashi.yonpachi.androiddiarytest01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import app.takahashi.yonpachi.androiddiarytest01.databinding.ActivityCreateAccountBinding
import coil.api.load
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAccountBinding
    private lateinit var userName: String
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        val userId = intent.getStringExtra("users")

        val docRef = db.collection("users").document(userId.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("tag", "DocumentSnapshot data: ${document.data}")
                    binding.createAccountUserImageView.load(document.data?.get("photoId").toString())
                    binding.createAccountUserTextEdit.setText(document.data?.get("name").toString())
                } else {
                    Log.d("tag", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("tag", "get failed with ", exception)
            }

        binding.createAccountButton.setOnClickListener {
            userName = binding.createAccountUserTextEdit.text.toString()
            if (userName != "") {
                docRef
                    .update("name", userName)
                    .addOnSuccessListener {
                        Log.d("tag😀", "😀")
                        val toLoginSecondActivityIntent = Intent(this, LoginSecondActivity::class.java)
                        toLoginSecondActivityIntent.putExtra("users", userId)
                        startActivity(toLoginSecondActivityIntent)
                    }
                    .addOnFailureListener { e->
                        Log.w("tag", "😭", e)
                    }
            } else {
                val dialog = NoneTextFragment()
                dialog.show(supportFragmentManager, "noneText")
                return@setOnClickListener
            }
            finish()
        }
    }
}