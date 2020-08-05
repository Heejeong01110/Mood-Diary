package kr.ac.kpu.dailystone

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_main.*
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
    //private val formatterYear: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy")
    //private val yearformatted: String = current.format(formatterYear)
    //private val yearformatted: String = current.format(formatter)
    //private val formatterMonth : DateTimeFormatter = DateTimeFormatter.ofPattern("MM")
    //private val monthformatted: String = current.format(formatterMonth)
    //private val monthformatted: String = current.format(formatter)
    //private val formatterDay : DateTimeFormatter = DateTimeFormatter.ofPattern("dd")
    //private val dayformatted: String = current.format(formatterDay)




    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mAuth = FirebaseAuth.getInstance();
        db = Firebase.database.reference

        ProgressView()

        mainTvDate.text = date

        mainBtnHappy.setOnClickListener {

            var dialog = DialogAddFragment(it.context,date)
            dialog.show()

        }
        mainBtnSad.setOnClickListener {
            var dialog = DialogSadAddFragment(it.context)
            dialog.show()
        }
        mainBtnPre.setOnClickListener {
            preDate()
            Log.d("predatetest", "date : $date")
        }
        mainBtnNext.setOnClickListener {
            nextDate()
            Log.d("predatetest", "date : $date")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun ProgressView(){
        var user = FirebaseAuth.getInstance().currentUser
        var day:Int = 50
        var dayList = mutableListOf<Int>()
        var dcnt:Any = 0
        var value = 0
        val daymyRef = db.child(user!!.uid).child("diary").child(year).child(monthformatted).child(dayformatted)
        val dayListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.child("count").child(date).child("count").value==null){
                    dcnt=0
                }
                else{
                    dcnt = snapshot.child("count").child(date).child("count").value!!
                }
                var level:Any=0
                for( i in 1 until dcnt.toString().toInt()+1){
                    level = snapshot.child("diary").child(year).child(monthformatted)
                        .child(dayformatted).child(i.toString()).child("level").value!!
                    Log.d("daytest", "i : $i, level : $level")
                    dayList.add(i-1,level.toString().toInt())
                }
                day = dayList.average().toInt()
                Log.d("daytest","dcnt : $dcnt")
                Log.d("daytest","day : $day")
                value = day
                mainPbDay.progress = value
            }
            override fun onCancelled(error: DatabaseError) { }
        }
        db.child(user.uid).addValueEventListener(dayListener)
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
       ProgressView()
   }

    private fun nextDate(){//이전 날짜 조회
        var ld : LocalDate = LocalDate.of(year.toInt(), monthformatted.toInt(),dayformatted.toInt())
        var plusDayNow = ld.plusDays(1)
        var formatted2 = plusDayNow.format(formatter)
        date = formatted2.substring(2,8)
        year = formatted2.substring(2,4)
        monthformatted = formatted2.substring(4,6)
        dayformatted = formatted2.substring(6,8)
        mainTvDate.text = date
        ProgressView()
    }
}