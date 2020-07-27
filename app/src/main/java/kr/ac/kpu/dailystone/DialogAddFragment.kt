package kr.ac.kpu.dailystone

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.dialog_happy.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DialogAddFragment(context: Context) : Dialog(context) {
    private var mAuth: FirebaseAuth? = null
    val database : FirebaseDatabase = FirebaseDatabase.getInstance()
    private lateinit var db: DatabaseReference
    var user: FirebaseUser?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_happy)

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().currentUser

        db=Firebase.database.reference
        var uid = user?.uid.toString()
        readID(uid)

        dhBtnYes.setOnClickListener {
            Toast.makeText(context,"확인 테스트",Toast.LENGTH_SHORT)
        }
        dhBtnNo.setOnClickListener {
            Toast.makeText(context,"취소 테스트",Toast.LENGTH_SHORT)
            dismiss();
        }
    }

    fun readID(uid:String): String {
        db = Firebase.database.reference
        var Did: String="1"
        db.child(uid).child("20200727").runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                if((mutableData.child("Did").value as? Long)!=null){
                    Did= mutableData.child("Did").value as String
                }else{
                    Did="1"
                }

                return Transaction.success(mutableData);
            }

            override fun onComplete(
                databaseError: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {
                //Toast.makeText(applicationContext,"Transaction:onComplete:$databaseError",Toast.LENGTH_LONG).show()
            }
        })
        return Did
    }


}