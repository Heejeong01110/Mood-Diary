package kr.ac.kpu.dailystone

import android.app.ActionBar
import android.app.Activity
import android.content.res.Resources
import android.hardware.display.DisplayManager
import android.media.Image
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Display
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_month.*


class MonthFragment : Fragment() {
     companion object { // 상수 역할

        fun newInstance(): MonthFragment {
             return MonthFragment()
         }
     }

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
     }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_month,container,false)
        for(i in 1..30) {
            val gv: GridLayout = rootView.findViewById(R.id.monthGrid)
            var width = Resources.getSystem().displayMetrics.widthPixels
            var devicewidth = (width - 128) / 5
            val iv: ImageView = ImageView(context)
            iv.setImageResource(R.drawable.happy_level1)
            val params = LinearLayout.LayoutParams(devicewidth, devicewidth)
            iv.layoutParams = params
            iv.setOnClickListener {
                Toast.makeText(context, "clicked $i", Toast.LENGTH_SHORT).show()
            }
            gv.addView(iv)

        }

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }


}
