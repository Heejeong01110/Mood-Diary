package kr.ac.kpu.dailystone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_main.viewPager
import kotlinx.android.synthetic.main.fragment_main.*

class MainActivity : AppCompatActivity() {

    val adapter = MainAdapter(supportFragmentManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager.adapter = adapter
        dots_indicator.setViewPager(viewPager)
        /*adapter.addFragment(MainFragment())
        adapter.addFragment(MonthFragment())
        adapter.addFragment(UserFragment())*/




    }
}