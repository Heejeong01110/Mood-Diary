package kr.ac.kpu.dailystone

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_userinfo.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class UserFragment : Fragment() {
    private lateinit var mAuth : FirebaseAuth
    private val current: LocalDate = LocalDate.now()
       private var formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
       private var formatted: String = current.format(formatter)
       var date = formatted.substring(2,8)
    companion object { // 상수 역할

        fun newInstance(): UserFragment {
            return UserFragment()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        userBtninfo.setOnClickListener {
            val intent = Intent(context, UserInfoActivity::class.java)
            startActivity(intent)
        }

        userBtnReset.setOnClickListener {

        }

        userBtnLogout.setOnClickListener {      //클릭 시 로그아웃
            signOut()
        }

        btnLockSet.setOnClickListener {
            val intent = Intent(context, LockActivity::class.java)
            startActivity(intent)
        }
        btnGoalSet.setOnClickListener {
            var dialog = fDialogGoal(it.context,date)
            dialog.show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_userinfo, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun signOut(){
        val intent = Intent(context, LoginActivity::class.java)
        FirebaseAuth.getInstance().signOut()
        startActivity(intent)
        activity?.finish()
    }

}