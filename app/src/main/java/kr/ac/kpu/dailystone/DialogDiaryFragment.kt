package kr.ac.kpu.dailystone

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.Resources.getSystem
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import android.content.res.Resources
import android.view.ViewGroup
import android.widget.FrameLayout
import com.firebase.ui.auth.AuthUI.getApplicationContext
import com.firebase.ui.auth.data.model.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.dialog_diary.*
import java.lang.NumberFormatException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap

@RequiresApi(Build.VERSION_CODES.O)
class DialogDiaryFragment(context: Context,date:String) : Dialog(context) {
    private var mAuth: FirebaseAuth? = null
    val database : FirebaseDatabase = FirebaseDatabase.getInstance()
    private lateinit var db: DatabaseReference
    private lateinit var level : Any
    private lateinit var diary : Any
    var cnt:Any = 0

    private val current: LocalDate = LocalDate.now()
    private var formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    private var formatted: String = current.format(formatter)
    var date = date
    var year = formatted.substring(2,4)
    var monthformatted = formatted.substring(4,6)
    var dayformatted: String = formatted.substring(6,8)

    var user: FirebaseUser?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_diary)
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().currentUser

        db= Firebase.database.reference

        //date 받아오기
        year = date.substring(0,2)
        monthformatted = date.substring(2,4)
        dayformatted = date.substring(4,6)
        Toast.makeText(context, "date $date", Toast.LENGTH_SHORT).show()

        //touch
        drawIv()
        /*
        ddEdHl.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                try {
                    var i: String? = ddEdHl.text.toString()
                    if (i == "") {
                        i = "0"
                    }
                    var str: Int? = i?.let { Integer.parseInt(it) }

                    if ((i!!.toInt() > 100) || (i!!.toInt() < 0)) {
                        Toast.makeText(context, "0부터 100까지의 숫자만 입력해주세요", Toast.LENGTH_SHORT).show()
                        ddEdHl.setText("0")
                    } else {
                        //ddSbar.progress = str!!
                    }
                }catch(e: NumberFormatException){
                    Toast.makeText(context, "숫자만 입력가능합니다", Toast.LENGTH_SHORT).show()
                    return
                }
                ddEdHl.setSelection(ddEdHl.length())

            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
        })

         */


        ddBtnYes.setOnClickListener {
            onWriteDBPost()
            dismiss()
        }
        ddBtnNo.setOnClickListener {
            dismiss()
        }
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n", "RestrictedApi")
    private fun drawIv(){
        var levelX:Int=0
        var levelY:Int=0
        var tempx = ddIvicon.x


        LinearDraw.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
            val width = LinearDraw.width
            val height = LinearDraw.height
            val iconx = ddIvicon.x
            val icony = ddIvicon.y
            //ddIvicon.translationX = width/2.toFloat() - ddIvicon.width/2
            //ddIvicon.translationY = height/2.toFloat() - ddIvicon.height/2

            var act = motionEvent.action
            Toast.makeText(context, "$tempx", Toast.LENGTH_SHORT).show()
            when (act){
                MotionEvent.ACTION_DOWN -> { //처음 눌렀을 때
                    ddIvicon.x = motionEvent.getX() - ddIvicon.width/2
                    ddIvicon.y = motionEvent.getY() - ddIvicon.height/2
                }
                MotionEvent.ACTION_MOVE -> { //누르고 움직였을 때
                    ddIvicon.x = motionEvent.getX() - ddIvicon.width/2
                    ddIvicon.y = motionEvent.getY() - ddIvicon.height/2
                }
                MotionEvent.ACTION_UP -> { //누른걸 땠을 때
                    ddIvicon.x = motionEvent.getX() - ddIvicon.width/2
                    ddIvicon.y = motionEvent.getY() - ddIvicon.height/2

                }

            }
            return@OnTouchListener true
        })



    }


    private fun onWriteDBPost() {
        db = Firebase.database.reference
        var user = FirebaseAuth.getInstance().currentUser

        level = ddEdHl.text.toString()
        diary = ddEdDiary.text.toString()


        val postValues: HashMap<String, Any> = HashMap()
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child("count").child(date).child("count").value == null) {
                    cnt = 0
                } else {
                    cnt = snapshot.child("count").child(date).child("count").value!!
                }

                val myRefDiary =
                    db.child(user!!.uid).child("diary").child(year).child(monthformatted)
                        .child(dayformatted)
                        .child((cnt.toString().toInt() + 1).toString())
                postValues["level"] = level
                postValues["diary"] = diary
                myRefDiary.setValue(postValues)

                val myRefCount = db.child(user!!.uid).child("count").child(date).child("count")
                myRefCount.setValue(cnt.toString().toInt() + 1)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        db.child(user!!.uid).addListenerForSingleValueEvent(postListener)
    }
}