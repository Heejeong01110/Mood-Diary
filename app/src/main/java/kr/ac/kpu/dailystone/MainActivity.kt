package kr.ac.kpu.dailystone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Initialize Firebase Auth
        auth = Firebase.auth
        //if(etIdInput.)

    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun loginEmail(etEmail : EditText, etPass : EditText){
        auth.signInWithEmailAndPassword(etEmail.text.toString(), etPass.text.toString()).addOnCompleteListener(this) { task ->
            if(task.isSuccessful){

            } else {

            }
        }
    }

    private fun updateUI(user : FirebaseUser?){

    }

}