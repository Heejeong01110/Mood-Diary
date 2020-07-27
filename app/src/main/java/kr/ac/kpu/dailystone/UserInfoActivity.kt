package kr.ac.kpu.dailystone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import kotlinx.android.synthetic.main.activity_user_info.*

class UserInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var editFlagEmail : Boolean = false      // 에디트 버튼을 누를 때 확인 플래그
        var editFlagPassword : Boolean = false

        setContentView(R.layout.activity_user_info)
        btnBack.setOnClickListener {
            finish()
        }

        btnEditEmail.setOnClickListener{
            if(editFlagEmail == false){
                editFlagEmail = true
                tvChEmail.visibility = View.INVISIBLE
                etChEmail.visibility = View.VISIBLE

            }else{
            }
        }

        btnEditPassword.setOnClickListener {

        }


    }
}