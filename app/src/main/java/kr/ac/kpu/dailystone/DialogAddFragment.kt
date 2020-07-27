package kr.ac.kpu.dailystone

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.dialog_happy.*
import kotlinx.android.synthetic.main.dialog_happy.view.*

class DialogAddFragment(context: Context) : Dialog(context) {
    private var mAuth: FirebaseAuth? = null
    val database : FirebaseDatabase = FirebaseDatabase.getInstance()
    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_happy)

        mAuth = FirebaseAuth.getInstance();

        db=Firebase.database.reference



        dhBtnYes.setOnClickListener {
            Toast.makeText(context,"확인 테스트",Toast.LENGTH_SHORT)
            // 데이터베이스에 저장
            onWriteDBPost()
        }
        dhBtnNo.setOnClickListener {
            Toast.makeText(context,"취소 테스트",Toast.LENGTH_SHORT)
            dismiss();
        }
    }

    fun onWriteDBPost() {
        db = Firebase.database.reference
        var user = FirebaseAuth.getInstance().currentUser

        //val myRef = database.getReference("posts")
        val myRef = database.getReference(user?.uid.toString())

        val postValues: HashMap<String, Any> = HashMap()
        postValues["date"] = "20200727"
        postValues["level"] = "5"

        myRef.setValue(postValues)

    }

}