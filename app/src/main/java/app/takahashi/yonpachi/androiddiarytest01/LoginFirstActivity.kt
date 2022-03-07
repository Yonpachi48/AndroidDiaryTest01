package app.takahashi.yonpachi.androiddiarytest01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import app.takahashi.yonpachi.androiddiarytest01.databinding.ActivityLoginFirstScreenBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginFirstActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginFirstScreenBinding
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginFirstScreenBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        // Configure Google Sign In
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = Firebase.auth

        binding.Signin.setOnClickListener {
            googleSignIn()
        }

    }

    private fun googleSignIn(){
        var googleSignInIntent  = mGoogleSignInClient?.signInIntent
        //Googleサインイン画面に遷移
        startActivityForResult(googleSignInIntent, RC_GOOGLE_SIGN_IN_CODE)
    }

    //Googleサインイン画面から戻ってきたときに実行される。
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_GOOGLE_SIGN_IN_CODE){
            var task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                var account = task.result
                //Googleアカウントの情報が取得できた際の処理
                if (account != null) {
                    firebaseAuthWithGoogle(account)

                    val user = User(
                        uid = auth.currentUser?.uid.toString(),
                        name = auth.currentUser?.displayName.toString(),
                        photoId = auth.currentUser?.photoUrl.toString(),
                        roomId = null
                    )
                    Log.d(ADD_TAG, "😀")
                    checkUser(user)
                }
            }catch (e : ApiException){
                Log.d("ApiException", e.toString())
            }
        }
    }

    //Googleアカウント情報を元に、firebaseで認証する。
    private fun firebaseAuthWithGoogle(acct : GoogleSignInAccount){

        var credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener{
                //認証成功時の処理
                if (it.isSuccessful){
                    Log.d("currentUser", auth?.currentUser.toString())
                }//認証失敗時の処理
                else{
                    // If sign in fails, display a message to the user.
                    Log.w("currentUser", "signInWithCredential:failure")
                }
            }
    }

    private fun checkUser(user: User) {
        db.collection("users").whereEqualTo("uid", user.uid)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("tag", "${document.id} => ${document.data} 🧐")
                    var toCreateGroupActivityIntent = Intent(this, CreateAccountActivity::class.java)
                    toCreateGroupActivityIntent.putExtra("users", document.id)
                    startActivity(toCreateGroupActivityIntent)
                    return@addOnSuccessListener
                }
                createUser(user)
            }
            .addOnFailureListener {
                Log.d("AddTag", "😎")
            }
    }

    private fun createUser(user: User) {
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(ADD_TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                var toCreateGroupActivityIntent = Intent(this, CreateGroupActivity::class.java)
                toCreateGroupActivityIntent.putExtra("users", documentReference.id)
                startActivity(toCreateGroupActivityIntent)
            }
            .addOnFailureListener { e ->
                Log.d(ADD_TAG, "Error adding document", e)
            }
    }

    companion object {
        const val RC_GOOGLE_SIGN_IN_CODE = 9001
        const val ADD_TAG = "taskTag"
    }

}