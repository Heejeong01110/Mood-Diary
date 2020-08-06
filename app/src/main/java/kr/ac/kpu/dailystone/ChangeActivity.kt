package kr.ac.kpu.dailystone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_change.*

class ChangeActivity : AppCompatActivity() {

    val user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change)
        var ChangeEmail : String
        var ChangePassword : String


        btnChangeAccept.setOnClickListener {
            val intent = Intent(this,UserInfoActivity::class.java)
            ChangeEmail = etEmailChange.text.toString()
            ChangePassword = etPasswordChange.text.toString()
            emailUpdate(ChangeEmail)
            passwordUpdate(ChangePassword)
            intent.putExtra("email", ChangeEmail )
            intent.putExtra("password", ChangePassword)

            startActivity(intent)
        }

        btnChangeCancel.setOnClickListener {
            finish()
            Toast.makeText(this, "이메일 비밀번호 변경 취소", Toast.LENGTH_SHORT).show()
        }




    }

    fun emailUpdate(newEmail: String) {
        user?.updateEmail(newEmail)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "이메일 비밀번호 변경 완료", Toast.LENGTH_SHORT).show()
                    Log.d("Min", "User email address updated.")

                }
            }
    }

    fun passwordUpdate(newPassword: String) {
        user?.updatePassword(newPassword)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Min", "User password updated.")
                }
            }
    }
}