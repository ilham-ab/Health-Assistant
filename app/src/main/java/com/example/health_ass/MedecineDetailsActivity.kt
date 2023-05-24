

package com.example.health_ass

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class MedecineDetailsActivity : AppCompatActivity() {
    private lateinit var tvMedImage: ImageView
    //private lateinit var tvMedId: TextView
    private lateinit var tvMedName: TextView
    private lateinit var tvMedDose: TextView
    private lateinit var tvMedTime1: TextView
    private lateinit var tvMedTime2: TextView
    private lateinit var tvMedTime3: TextView
    private lateinit var tvMedStart: TextView
    private lateinit var tvMedDuree: TextView
    private lateinit var tvMedOrdre: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button
    private lateinit var calendar: Calendar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medecine_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("medId").toString(),
                intent.getStringExtra("medName").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("medId").toString()
            )
        }

    }

    private fun initView() {
        tvMedImage= findViewById(R.id.tvMedImage)
       // tvMedId = findViewById(R.id.tvMedId)
        tvMedName = findViewById(R.id.tvMedName)
        tvMedDose = findViewById(R.id.tvMedDose)
        tvMedTime1 = findViewById(R.id.tvMedTime1)
        tvMedTime2 = findViewById(R.id.tvMedTime2)
        tvMedTime3 = findViewById(R.id.tvMedTime3)
        tvMedOrdre = findViewById(R.id.tvMedOrdre)
        tvMedDuree= findViewById(R.id.tvMedDuree)
        tvMedStart = findViewById(R.id.tvMedStart)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
       // tvMedId.text = intent.getStringExtra("medId")
        tvMedName.text = intent.getStringExtra("medName")
        tvMedDose.text = intent.getStringExtra("medDose")
        tvMedTime1.text = intent.getStringExtra("medTime1")
        tvMedTime2.text = intent.getStringExtra("medTime2")
        tvMedTime3.text = intent.getStringExtra("medTime3")
        tvMedStart.text = intent.getStringExtra("medStart")
        tvMedDuree.text = intent.getStringExtra("medDuration")
        tvMedOrdre.text = intent.getStringExtra("medOrdre")
        val medPhotoUrl = intent.getStringExtra("medPhoto")
        Glide.with(this)
            .load(medPhotoUrl)
            .placeholder(R.drawable.placeholder_image) // optional, to show a placeholder image while loading
            .into(tvMedImage)
    }


    @SuppressLint("MissingInflatedId")
    private fun openUpdateDialog(
        medId: String,
        medName: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_medecine_dialog, null)
        val scrollView = mDialogView.findViewById<ScrollView>(R.id.scrollView)
        scrollView.isVerticalScrollBarEnabled = true
        scrollView.overScrollMode = View.OVER_SCROLL_ALWAYS

        mDialog.setView(mDialogView)




        val etMedName = mDialogView.findViewById<EditText>(R.id.etMedName)
        val etMedDose = mDialogView.findViewById<EditText>(R.id.etMedDose)
        val etMedTime1 = mDialogView.findViewById<EditText>(R.id.etMedTime1)
        val etMedTime2 = mDialogView.findViewById<EditText>(R.id.etMedTime2)
        val etMedTime3 = mDialogView.findViewById<EditText>(R.id.etMedTime3)
        val btnPickTime1=mDialogView.findViewById<Button>(R.id.btnPickTime1)
        val btnPickTime2=mDialogView.findViewById<Button>(R.id.btnPickTime2)
        val btnPickTime3=mDialogView.findViewById<Button>(R.id.btnPickTime3)
        val etMedStart = mDialogView.findViewById<EditText>(R.id.etMedStart)
        val durationUnitSpinner = mDialogView.findViewById<Spinner>(R.id.spinnerDurationUnit)
        val durationValueSpinner = mDialogView.findViewById<Spinner>(R.id.spinnerDurationValue)

        // Set click listeners for the time pickers
        btnPickTime1.setOnClickListener { showTimePickerDialog(etMedTime1) }
        btnPickTime2.setOnClickListener { showTimePickerDialog(etMedTime2) }
        btnPickTime3.setOnClickListener { showTimePickerDialog(etMedTime3) }

        calendar = Calendar.getInstance()

        val radioMedecineOrdre = mDialogView.findViewById<RadioGroup>(R.id.radioGroupMedecineOrdre) // Changed to RadioGroup

        val selectedRadioButtonId = radioMedecineOrdre.checkedRadioButtonId
        val selectedRadioButton = mDialogView.findViewById<RadioButton>(selectedRadioButtonId)

        val etMedOrdre = selectedRadioButton.text.toString() // Obtaining selected value from RadioButton




        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)


        etMedName.setText(intent.getStringExtra("medName").toString())
        etMedDose.setText(intent.getStringExtra("medDose").toString())
        etMedTime1.setText(intent.getStringExtra("medTime1").toString())
        etMedTime2.setText(intent.getStringExtra("medTime2").toString())
        etMedTime3.setText(intent.getStringExtra("medTime3").toString())
        etMedStart.setText(intent.getStringExtra("medStart").toString())

        val durationValue = durationValueSpinner.selectedItem.toString()
        val durationUnit = durationUnitSpinner.selectedItem.toString()

        //etMedOrdre.setText(intent.getStringExtra("medOrdre").toString())

        mDialog.setTitle("Updating $medName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateMedData(
                medId,
                etMedName.text.toString(),
                etMedDose.text.toString(),
                etMedTime1.text.toString(),
                etMedTime2.text.toString(),
                etMedTime3.text.toString(),
                etMedStart.text.toString(),
                durationValue ,
                durationUnit,
                etMedOrdre
            )

            Toast.makeText(applicationContext, "Medecine Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvMedName.text = etMedName.text.toString()
            tvMedDose.text = etMedDose.text.toString()
            tvMedTime1.text = etMedTime1.text.toString()
            tvMedTime2.text = etMedTime2.text.toString()
            tvMedTime3.text = etMedTime3.text.toString()
            tvMedStart.text = etMedStart.text.toString()
            tvMedDuree.text = durationValue + " " + durationUnit
            tvMedOrdre.text = etMedOrdre

            alertDialog.dismiss()
        }
    }

    private fun updateMedData(
        id: String,
        name: String,
        dose: String,
        time1: String,
        time2: String,
        time3: String,
        start:String,
        dureeValue: String,
        dureeUnit: String,
        ordre: String
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val dbRef = FirebaseDatabase.getInstance().getReference("Medecines").child(userId.orEmpty()).child(id)

        val medInfo = MedecineModel(id, name, dose, time1,time2, time3,start, dureeValue,dureeUnit, ordre)
        dbRef.setValue(medInfo)
    }




    private fun deleteRecord(
        id: String
    ){
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val dbRef = FirebaseDatabase.getInstance().getReference("Medecines").child(userId.orEmpty()).child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Medecine data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingMedecineActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }
    private fun showTimePickerDialog(editText: EditText) {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this,
            { _, hourOfDay, minuteOfDay -> editText.setText("$hourOfDay:$minuteOfDay") }, hour, minute, true)

        timePickerDialog.show()
    }

    fun getPosition(spinner: Spinner, value: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString() == value) {
                return i
            }
        }
        return 0 // or -1 if the value is not found
    }


}


