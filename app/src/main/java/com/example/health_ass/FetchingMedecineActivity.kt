
package com.example.health_ass

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
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
    private lateinit var currentDayTextView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fetching_medecine_activity)

        //desplaying days_bar

        //val recyclerView = findViewById<RecyclerView>(R.id.days_recyclerview)

        val days = generateDaysForCurrentMonth()
        val currentDayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        days.find { it.dayOfMonth == currentDayOfMonth }?.isCurrentDay = true

        currentDayTextView = findViewById(R.id.dayofmonth)


        // Get the current day
        val calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        // Set the current day in the TextView
        currentDayTextView.text = currentDay.toString()
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
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        dbRef = FirebaseDatabase.getInstance().getReference("Medecines").child(userId.orEmpty())

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                medList.clear()
                if (snapshot.exists()){
                    for (medSnap in snapshot.children){
                        val medData = medSnap.getValue(MedecineModel::class.java)
                        medList.add(medData!!)
                        if (isTimeToNotify(medData.medecineTime1)) {
                            // Create and display the notification for each medication individually
                            showNotification(medData.medecineName)
                        }
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

    private fun isTimeToNotify(medecineTime1: String?): Boolean {
        // Get the current time
        val currentTime = Calendar.getInstance().time

        // Parse the medication time from the string to a Date object
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        val medicationTime = format.parse(medecineTime1)
        // Compare the current time with the medication time
        return currentTime.after(medicationTime)
    }

    private fun showNotification(medicationName: String?) {
        val channelId = "medication_channel"
        val channelName = "Medication Channel"
        val notificationId = 1
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        // Create a notification channel (required for Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = "Channel for medication reminders"
                enableLights(true)
                lightColor = Color.GREEN
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        // Build the notification
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.assistance) // Replace with your own icon
            .setContentTitle("Medication Reminder")
            .setContentText("Time to take your medication: $medicationName")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(soundUri)

        // Show the notification
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}


