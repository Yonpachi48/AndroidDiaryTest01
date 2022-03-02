package app.takahashi.yonpachi.androiddiarytest01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import app.takahashi.yonpachi.androiddiarytest01.databinding.ActivityLoginScreenBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginScreen : AppCompatActivity() {

    private lateinit var binding: ActivityLoginScreenBinding
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater).apply {
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
                    Log.d("account", auth.currentUser?.photoUrl.toString())
                    var toMainActivityIntent = Intent(this, MainActivity::class.java)
                    startActivity(toMainActivityIntent)
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

                }
            }
    }

    companion object {
        const val RC_GOOGLE_SIGN_IN_CODE = 9001
    }

}