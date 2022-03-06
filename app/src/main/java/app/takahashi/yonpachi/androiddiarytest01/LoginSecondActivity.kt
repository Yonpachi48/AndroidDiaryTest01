package app.takahashi.yonpachi.androiddiarytest01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import app.takahashi.yonpachi.androiddiarytest01.databinding.ActivityLoginSecondActivitiyBinding
import coil.api.load
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginSecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginSecondActivitiyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginSecondActivitiyBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        val userId = intent.getStringExtra("users")

        val db = Firebase.firestore
        val docRef = db.collection("users").document(userId.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("tag", "DocumentSnapshot data: ${document.data}")
                    binding.loginUserImageView.load(document.data?.get("photoId").toString())
                    binding.loginUserTextEdit.setText(document.data?.get("name").toString())
                } else {
                    Log.d("tag", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("tag", "get failed with ", exception)
            }


        binding.createGpButton.setOnClickListener {

            val nameText = binding.loginUserTextEdit.text

            if(binding.loginUserTextEdit.text != null) {
                docRef
                    .update("name", nameText)
                    .addOnSuccessListener {
                        Log.d("tag😀", "😀")
                    }
                    .addOnFailureListener { e->
                        Log.w("tag", "😭", e)
                    }
            } else return@setOnClickListener


            Log.d("task🧐", "🧐${userId}")
            val toCreateGroupActivityIntent = Intent(this, CreateGroupActivity::class.java)
            toCreateGroupActivityIntent.putExtra("users", userId)
            startActivity(toCreateGroupActivityIntent)
        }
    }
}