package kr.ac.kpu.dailystone

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.dialog_diary.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.android.synthetic.main.fragment_month.view.*
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt


@RequiresApi(Build.VERSION_CODES.O)
class MainFragment : Fragment() {
   companion object { // 상수 역할

       fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private var mAuth: FirebaseAuth? = null //auth
    private val database : FirebaseDatabase = FirebaseDatabase.getInstance()
    private lateinit var db: DatabaseReference

    //date
    private val current: LocalDate = LocalDate.now()
    private var formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    private var formatted: String = current.format(formatter)
    var date = formatted.substring(2,8)
    var year = formatted.substring(2,4)
    var monthformatted = formatted.substring(4,6)
    var dayformatted: String = formatted.substring(6,8)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mAuth = FirebaseAuth.getInstance();
        db = Firebase.database.reference
        readCount()

        mainTvDate.text = date

        mainBtnHappy.setOnClickListener {
            var dialog = DialogDiaryFragment(it.context,date)
            dialog.show()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainBtnPre.setOnClickListener {
            preDate()
        }
        mainBtnNext.setOnClickListener {
            nextDate()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)
        readDayDiary(rootView)

        return rootView
    }

    private fun readDayDiary(rootView:View){
        db = Firebase.database.reference
        var user = FirebaseAuth.getInstance().currentUser
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                rootView.mainGrid.removeAllViews()
                val childCount = snapshot.childrenCount.toInt()
                Log.d("test","childCount : $childCount")
                for (i in 1 until childCount + 1) {
                    if (snapshot.child("$i").hasChildren()) {
                        val gv: GridLayout = rootView.findViewById(R.id.mainGrid)
                        var width = Resources.getSystem().displayMetrics.widthPixels
                        var devicewidth = (width - 128) / 5
                        val iv = ImageView(context)

                        matchImageViewColor(iv, snapshot.child("$i").child("color").value.toString())
                        matchImageViewResource(iv, snapshot.child("$i").child("emoticon").value.toString())

                        val params = LinearLayout.LayoutParams(devicewidth, devicewidth)
                        iv.layoutParams = params
                        gv.addView(iv)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        db.child(user!!.uid).child("diary").child(year).child(monthformatted).child(dayformatted)
            .addValueEventListener(postListener)
    }
    private fun matchImageViewColor(view : ImageView, i : String){
        when(i){
            "1" -> {view.setColorFilter(Color.rgb(255,0,0))}
            "2" -> {view.setColorFilter(Color.rgb(255,192,0))}
            "3" -> {view.setColorFilter(Color.rgb(0, 176,80))}
            "4" -> {view.setColorFilter(Color.rgb(0,112,192))}
            "0" -> {view.setColorFilter(Color.rgb(0,112,192))}
        }
    }

    private fun matchImageViewResource(view : ImageView, i : String){
        when(i){
            "0" -> {view.setImageResource(R.drawable.basic_level) }
            "1" -> {view.setImageResource(R.drawable.happy_level1) }
            "2" -> {view.setImageResource(R.drawable.happy_level2) }
            "3" -> {view.setImageResource(R.drawable.happy_level3)}
            "4" -> {view.setImageResource(R.drawable.sad_level1) }
            "5" -> {view.setImageResource(R.drawable.sad_level2)}
            "6" -> {view.setImageResource(R.drawable.sad_level3)}
        }
    }




    private fun readCount(){
        var user = FirebaseAuth.getInstance().currentUser
        var dcnt:Any = 0

        val dayListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value==null){
                    dcnt=0
                    mainPbDay.progress = 0
                }
                else{
                    dcnt = snapshot.value!!
                    //일별 평균 level 출력
                    readLevel(dcnt.toString().toInt())
                }
            }
            override fun onCancelled(error: DatabaseError) { }
        }
        db.child(user!!.uid).child("count").child(date).child("count")
            .addValueEventListener(dayListener)
    }

    private fun readLevel(dcnt:Int){
        var user = FirebaseAuth.getInstance().currentUser
        var day:Int = 50
        var dayList = mutableListOf<Int>()
        var value = 0

        val dayListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var level:Any=0
                for( i in 0 until dcnt){
                    level = snapshot.child((i+1).toString()).child("level").value!!
                    dayList.add(i,level.toString().toInt())
                }
                day = dayList.average().toInt()
                value = day
                mainPbDay.progress = value
            }
            override fun onCancelled(error: DatabaseError) { }
        }
        db.child(user!!.uid).child("diary").child(year).child(monthformatted)
            .child(dayformatted).addListenerForSingleValueEvent(dayListener)

    }

   private fun preDate(){//이전 날짜 조회

       var ld : LocalDate = LocalDate.of(year.toInt(), monthformatted.toInt(),dayformatted.toInt())
       var minusDayNow = ld.plusDays(-1)
       var formatted2 = minusDayNow.format(formatter)
       date = formatted2.substring(2,8)
       year = formatted2.substring(2,4)
       monthformatted = formatted2.substring(4,6)
       dayformatted = formatted2.substring(6,8)
       mainTvDate.text = date
       var ft : FragmentTransaction? = fragmentManager?.beginTransaction()
       ft?.detach(this)?.attach(this)?.commit()
       readCount()
   }

    private fun nextDate(){//다음 날짜 조회
        var ld : LocalDate = LocalDate.of(year.toInt(), monthformatted.toInt(),dayformatted.toInt())
        var plusDayNow = ld.plusDays(1)
        var formatted2 = plusDayNow.format(formatter)
        date = formatted2.substring(2,8)
        year = formatted2.substring(2,4)
        monthformatted = formatted2.substring(4,6)
        dayformatted = formatted2.substring(6,8)
        mainTvDate.text = date
        var ft : FragmentTransaction? = fragmentManager?.beginTransaction()
        ft?.detach(this)?.attach(this)?.commit()
        readCount()
    }
}