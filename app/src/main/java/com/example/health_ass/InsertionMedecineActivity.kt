package com.example.health_ass

import android.Manifest
import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.util.*


class InsertionMedecineActivity : AppCompatActivity() {

    private val CAMERA_PERMISSION_REQUEST_CODE = 1001 // You can use any integer value here
    private lateinit var etMedecineName: EditText
    private lateinit var etMedecineDose: EditText
    private lateinit var etMedecinestart: EditText
    private lateinit var etMedecinePhoto: Button
    private lateinit var radioMedecineOrdre: RadioGroup // Changed to RadioGroup
    private lateinit var btnSaveData: Button
    private lateinit var ivMedecineImage: ImageView
    private lateinit var dbRef: DatabaseReference
    private lateinit var storageRef: StorageReference


    private var medecineImageUri: Uri? = null // Uri pour stocker l'URI de l'image capturée

    private lateinit var etMedecineTime1: EditText
    private lateinit var etMedecineTime2: EditText
    private lateinit var etMedecineTime3: EditText
    private lateinit var btnPickTime1: Button
    private lateinit var btnPickTime2: Button
    private lateinit var btnPickTime3: Button
    private lateinit var calendar: Calendar

    private lateinit var mDurationValueSpinner: Spinner
    private lateinit var mDurationUnitSpinner: Spinner


    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }




    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion_medecine)

        // Request CAMERA permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        }


        //&supportActionBar

        etMedecineName = findViewById(R.id.etMedecineName)
        etMedecineDose = findViewById(R.id.etMedecineDose)
        etMedecineTime1 = findViewById(R.id.etMedecineTime1)
        etMedecineTime2 = findViewById(R.id.etMedecineTime2)
        etMedecineTime3 = findViewById(R.id.etMedecineTime3)
        btnPickTime1 = findViewById(R.id.btnPickTime1)
        btnPickTime2 = findViewById(R.id.btnPickTime2)
        btnPickTime3 = findViewById(R.id.btnPickTime3)

        mDurationValueSpinner = findViewById(R.id.spinnerDurationValue);
        mDurationUnitSpinner = findViewById(R.id.spinnerDurationUnit);


        // Set click listeners for the time pickers
        btnPickTime1.setOnClickListener { showTimePickerDialog(etMedecineTime1) }
        btnPickTime2.setOnClickListener { showTimePickerDialog(etMedecineTime2) }
        btnPickTime3.setOnClickListener { showTimePickerDialog(etMedecineTime3) }

        calendar = Calendar.getInstance()


        etMedecinestart = findViewById(R.id.etMedecinestart)

        radioMedecineOrdre = findViewById(R.id.radioGroupMedecineOrdre) // Changed to RadioGroup


        etMedecinePhoto = findViewById(R.id.etMedecinePhoto)

        etMedecinePhoto.setOnClickListener {
            dispatchTakePictureIntent()
        }
        ivMedecineImage = findViewById(R.id.ivMedecineImage)




        dbRef = FirebaseDatabase.getInstance().getReference("Medecines")
        storageRef = FirebaseStorage.getInstance().reference

        btnSaveData = findViewById(R.id.btnSave)
        btnSaveData.setOnClickListener {
            saveMedecineData()
        }
    }

    private fun saveMedecineData() {




        //getting values
        val MedecineName = etMedecineName.text.toString()
        val MedecineDose = etMedecineDose.text.toString()
        val MedecineTime1 = etMedecineTime1.text.toString()
        val MedecineTime2 = etMedecineTime2.text.toString()
        val MedecineTime3 = etMedecineTime3.text.toString()

        val Medecinestart = etMedecinestart.text.toString()
        val selectedRadioButtonId = radioMedecineOrdre.checkedRadioButtonId
        val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
        val MedecineOrdre = selectedRadioButton.text.toString() // Obtaining selected value from RadioButton

        // Get the selected duration value from the spinner
        val MedecineDurationValue = mDurationValueSpinner.selectedItem.toString()
       // Get the selected duration unit from the spinner
        val MedecineDurationUnit = mDurationUnitSpinner.selectedItem.toString()



        if (MedecineName.isEmpty()) {
            etMedecineName.error = "Please enter name"
        }
        if (MedecineDose.isEmpty()) {
            etMedecineDose.error = "Please enter dose"
        }
       /* if (MedecineTime.isEmpty()) {
            etMedecineTime.error = "Please enter time"
        }*/

        val medId = dbRef.push().key!!
        if (medecineImageUri != null) { // Vérifier
            // Obtenir la référence de stockage pour l'image
            val imageRef = storageRef.child("medecine_images/$medId.jpg")

            // Compresser l'image en format JPEG
            val outputStream = ByteArrayOutputStream()
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, medecineImageUri)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            val data = outputStream.toByteArray()

            // Télécharger l'image compressée dans Firebase Storage
            val uploadTask = imageRef.putBytes(data)
            uploadTask.addOnSuccessListener {
                // Obtenir l'URL de téléchargement de l'image
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val medecine = MedecineModel(
                        medId,
                        MedecineName,
                        MedecineDose,
                        MedecineTime1,
                        MedecineTime2,
                        MedecineTime3,
                        Medecinestart,
                        MedecineDurationValue,
                        MedecineDurationUnit,
                        MedecineOrdre,
                        uri.toString() // Enregistrer l'URL de l'image dans l'objet Medecine
                    )
                    dbRef.child(medId).setValue(medecine).addOnSuccessListener {
                        Toast.makeText(this, "Medecine data saved successfully", Toast.LENGTH_SHORT).show()
                        finish()
                        Log.d("TAG", "votre message")
                    }.addOnFailureListener {
                        Toast.makeText(this, "Failed to save medecine data", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to get medecine image URL", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to upload medecine image", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Si aucune image capturée, enregistrer les autres données sans l'URL de l'image
            val medecine = MedecineModel(
                medId,
                MedecineName,
                MedecineDose,
                MedecineTime1,
                MedecineTime2,
                MedecineTime3,
                Medecinestart,
                MedecineDurationValue,
                MedecineDurationUnit,
                MedecineOrdre,
                ""
            )
            dbRef.child(medId).setValue(medecine).addOnSuccessListener {
                Toast.makeText(this, "Medecine data saved successfully", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to save medecine data", Toast.LENGTH_SHORT).show()
            }
        }
/*
        val medecine = MedecineModel(medId, MedecineName, MedecineDose, MedecineTime, Medecinestart, "", MedecineOrdre) // Updated MedecineModel constructor

        dbRef.child(medId).setValue(medecine)
            .addOnCompleteListener {
                Toast.makeText(applicationContext, "Data inserted successfully", Toast.LENGTH_LONG).show()

                etMedecineName.text.clear()
                etMedecineDose.text.clear()
                //etMedecineTime.text.clear()
                etMedecinestart.text.clear()
                radioMedecineOrdre.clearCheck()



            }.addOnFailureListener { err ->
                Toast.makeText(applicationContext, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }*/

    }

    private fun showTimePickerDialog(editText: EditText) {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this,
            { _, hourOfDay, minuteOfDay -> editText.setText("$hourOfDay:$minuteOfDay") }, hour, minute, true)

        timePickerDialog.show()
    }

/*    private fun showTimePickerDialog() {
            val cal = Calendar.getInstance()
            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val minute = cal.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    etMedecineTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute))
                },
                hour,
                minute,
                true
            )
            timePickerDialog.show()
        }*/


    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            medecineImageUri = getImageUri(imageBitmap)

            // Load the image into the ImageView using Picasso
            Picasso.get().load(medecineImageUri).into(ivMedecineImage)
        }
    }


    private fun getImageUri(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

















