
package com.example.health_ass

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FetchingMedecineActivity : AppCompatActivity() {

    private lateinit var btnInsertData: Button
    private lateinit var medRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var medList: ArrayList<MedecineModel>
    private lateinit var dbRef: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fetching_medecine_activity)
        //desplaying days_bar

        //val recyclerView = findViewById<RecyclerView>(R.id.days_recyclerview)

        val days = generateDaysForCurrentMonth()
        val currentDayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        days.find { it.dayOfMonth == currentDayOfMonth }?.isCurrentDay = true

        //recyclerView.adapter = DayAdapter(days)
        //recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        val jan = findViewById<TextView>(R.id.jan)
        val feb = findViewById<TextView>(R.id.feb)
        val mar = findViewById<TextView>(R.id.mar)
        val apr = findViewById<TextView>(R.id.apr)
        val may = findViewById<TextView>(R.id.may)
        val jun = findViewById<TextView>(R.id.jun)
        val jul = findViewById<TextView>(R.id.jul)
        val aug = findViewById<TextView>(R.id.aug)
        val sep = findViewById<TextView>(R.id.sep)
        val oct = findViewById<TextView>(R.id.oct)
        val nev = findViewById<TextView>(R.id.nev)
        val dec = findViewById<TextView>(R.id.dec)

        val currentDate = Calendar.getInstance().time
        val monthFormatter = SimpleDateFormat("MMMM", Locale.getDefault())
        val currentMonth = monthFormatter.format(currentDate)

        for (textView in arrayOf(jan, feb, mar, apr, may, jun, jul, aug, sep, oct, nev, dec)) {
            if (textView.text == currentMonth) {
                textView.setTextColor(Color.BLUE)
            } else {
                textView.setTextColor(Color.GRAY)
            }
        }



        btnInsertData = findViewById(R.id.btnInsertData)
        btnInsertData.setOnClickListener {
            val intent = Intent(this, InsertionMedecineActivity::class.java)
            startActivity(intent)
        }
        medRecyclerView = findViewById(R.id.rvMed)
        medRecyclerView.layoutManager = LinearLayoutManager(this)
        medRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        medList = arrayListOf<MedecineModel>()

        getMedecinesData()

    }

    private fun getMedecinesData() {

        medRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Medecines")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                medList.clear()
                if (snapshot.exists()){
                    for (medSnap in snapshot.children){
                        val medData = medSnap.getValue(MedecineModel::class.java)
                        medList.add(medData!!)
                    }
                    val mAdapter = MedecineAdapter(medList)
                    medRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : MedecineAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@FetchingMedecineActivity, MedecineDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("medId", medList[position].medecineId)
                            intent.putExtra("medName", medList[position].medecineName)
                            intent.putExtra("medDose", medList[position].medecineDose)
                            intent.putExtra("medTime1", medList[position].medecineTime1)
                            intent.putExtra("medTime2", medList[position].medecineTime2)
                            intent.putExtra("medTime3", medList[position].medecineTime3)
                            intent.putExtra("medStart", medList[position].medecineStart)
                            intent.putExtra("medDureeValue", medList[position].medecineDurationValue)
                            intent.putExtra("medDureeUnit", medList[position].medecineDurationUnit)
                            val medDuration = "${medList[position].medecineDurationValue} ${medList[position].medecineDurationUnit}"
                            intent.putExtra("medDuration", medDuration)
                            Log.d("medOrdre", medList[position].medecineOrdre.toString())

                            intent.putExtra("medOrdre", medList[position].medecineOrdre)
                            intent.putExtra("medPhoto", medList[position].downloadUrl)


                            startActivity(intent)
                        }

                    })

                    medRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun generateDaysForCurrentMonth(): List<Day> {
        val calendar = Calendar.getInstance()
        val maxDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val days = mutableListOf<Day>()
        for (i in 1..maxDayOfMonth) {
            days.add(Day(i))
        }
        return days
    }
}


