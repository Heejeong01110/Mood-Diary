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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mAuth = FirebaseAuth.getInstance();
        db = Firebase.database.reference

        //ProgressView()
        readCount()
        goalCount()
        mainTvDate.text = date

        mainBtnHappy.setOnClickListener {
            //var dialog = DialogAddFragment(it.context,date)
            var dialog = DialogDiaryFragment(it.context,date)
            dialog.show()
        }
        mainBtnSad.setOnClickListener {
            var dialog = DialogSadAddFragment(it.context)
            dialog.show()
        }
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
        return inflater.inflate(R.layout.fragment_main,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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

    private fun goalCount(){
        var user = FirebaseAuth.getInstance().currentUser
        var gcnt:Any = 0

        val dayListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value==null){
                    gcnt=0
                    mainPbDay.progress = 0
                }
                else{
                    gcnt = snapshot.value!!
                    monthGoal(gcnt.toString().toInt())
                    dailyGoal()
                }
            }
            override fun onCancelled(error: DatabaseError) { }
        }
        db.child(user!!.uid).child("count").child(date).child("count")
            .addValueEventListener(dayListener)
    }

    private fun monthGoal(gcnt:Int) {
        var user = FirebaseAuth.getInstance().currentUser
        var Mgoal: Any = 0
        var goalSum : Int
        var goalList = mutableListOf<Int>()
        val goalListener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                var goalYear = mainTvDate.text.toString().substring(0,2)
                var goalMonth = mainTvDate.text.toString().substring(2,4)
                if (snapshot.child("count").child(date).value == null) {

                } else {
                    var cntDate : String= snapshot.child("count").child(date).value.toString()
                    var cntMonth = cntDate.substring(2,4)
                    var cntYear = cntDate.substring(0,2)
                    if((cntYear == snapshot.child("goal").child(goalYear).value) && (cntMonth == snapshot.child("goal").child(goalYear).child(goalMonth).value)){
                        for( i in 0 until gcnt){
                            Mgoal = snapshot.child("goal").child(date).child("count").value!!
                            goalList.add(i,Mgoal.toString().toInt())
                        }
                        goalSum = goalList.sum()
                        mainPbMgoal.progress = goalSum
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        db.child(user!!.uid).addListenerForSingleValueEvent(goalListener)
        /*override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child("goal").child(year).child(monthformatted).child("goal").value == null) {
                    goal = 0
                    mainPbMgoal.max = 0
                } else {
                    goal = snapshot.child("goal").child(year).child(monthformatted).child("goal").value!!
                    //일별 평균 level 출력
                    mainPbMgoal.max = goal as Int
                }
            }*/

    }
    private fun dailyGoal() {
        var user = FirebaseAuth.getInstance().currentUser
        var Dgoal: Any = 0
        val goalListener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                var goalYear = mainTvDate.text.toString().substring(0, 2)
                var goalMonth = mainTvDate.text.toString().substring(2, 4)
                var goalDay = mainTvDate.text.toString().substring(4,6)
                if (snapshot.child("count").child(date).value == null) {
                } else {
                    var cntDate: String = snapshot.child("count").child(date).value.toString()
                    var cntYear = cntDate.substring(0, 2)
                    var cntMonth = cntDate.substring(2, 4)
                    var cntDay = cntDate.substring(4,6)
                    if ((goalYear == snapshot.child("goal")
                            .child(cntYear).value) && (goalMonth == snapshot.child("goal")
                            .child(cntYear).child(goalMonth).value) && (goalDay == snapshot.child("goal").child(cntYear).child(cntMonth).child(cntDay).value)
                    ) {
                        Dgoal = snapshot.child("count").child(date).child("count").value.toString()
                        mainPbMgoal.progress = Dgoal.toString().toInt()

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        db.child(user!!.uid).addListenerForSingleValueEvent(goalListener)
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
        readCount()
    }
}