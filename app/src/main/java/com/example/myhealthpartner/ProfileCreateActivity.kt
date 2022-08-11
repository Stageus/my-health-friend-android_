package com.example.myhealthpartner

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.lang.Exception
import java.util.*
import java.util.jar.Manifest


class ProfileCreateActivity : AppCompatActivity() {

    lateinit var imageResult : ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_profile_page)
        val checkboxLinear = findViewById<LinearLayout>(R.id.checkboxLinear)
        checkboxLinear.visibility = View.GONE

        var imageFile : File

        imageResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            result ->
            if(result.resultCode == RESULT_OK){
                val imageUri = result.data?.data
                imageUri.let{
                    imageFile = File(getRealPathFromURI(it!!))
                }
            }
        }

        initEvent()
    }

    companion object{
        const val REVIEW_MIN_LENGTH = 10
        const val REQ_GALLERY = 1
        const val PARAM_KEY_IMAGE = "image"
        const val PARAM_KEY_PRODUCT_ID = "product_id"
        const val PARAM_KEY_REVIEW = "review_content"
        const val PARAM_KEY_RATING = "rating"
    }

    fun getRealPathFromURI(uri : Uri) : String{
        val buildName = Build.MANUFACTURER
        if(buildName.equals("Xiaomi")){
            return uri.path!!
        }
        var columnIndex = 0
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, proj, null, null, null)
        if(cursor!!.moveToFirst()){
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        val result = cursor.getString(columnIndex)
        cursor.close()
        return result
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun selectGallery(){
        val readPermission = ContextCompat.checkSelfPermission(this,
        android.Manifest.permission.READ_EXTERNAL_STORAGE)

        if(readPermission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), REQ_GALLERY
            )
        }else{
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            "image/*")
            imageResult.launch(intent)
        }


    }

    fun getGeocodeFromAddress(address: String): Address {
        val coder = Geocoder(this)
        val geocodedAddress: List<Address> = coder.getFromLocationName(address, 50)
        Log.d("GmapViewFragment", "Geocode from Address ${geocodedAddress}${geocodedAddress.get(0).longitude}")
        return geocodedAddress[0]
    }

//    fun getDistanceFromCurrentLocation(current: LatLng, dest: LatLng): ArrayList<Int> {
//        val currentLocation = Location("현재위치")
//        currentLocation.latitude = current.latitude
//        currentLocation.longitude = current.longitude
//        val destLocation = Location("목적지")
//        destLocation.latitude = dest.latitude
//        destLocation.longitude = dest.longitude
//        val distance: Float = currentLocation.distanceTo(destLocation)
//        Log.d("GmapViewFragment","Current Lat: ${currentLocation.latitude}, Lng: ${currentLocation.longitude}")
//        return
//    }

    fun navigateGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 2000)

    }



    fun initEvent(){
        val checkboxLinear = findViewById<LinearLayout>(R.id.checkboxLinear)
        val fitnessListBtn = findViewById<ImageButton>(R.id.fitnessListBtn)
        val setprofileBtn = findViewById<Button>(R.id.setProfileBtn)

        setprofileBtn.setOnClickListener {
            selectGallery()
        }


        fitnessListBtn.setOnClickListener {
            if(checkboxLinear.visibility == View.GONE){
                checkboxLinear.visibility = View.VISIBLE
            }
            else{
                checkboxLinear.visibility = View.GONE
            }
        }
        val createProfile = findViewById<Button>(R.id.createProfileBtn)

        createProfile.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        val findAdressBtn = findViewById<Button>(R.id.findAdressBtn)
        findAdressBtn.setOnClickListener{
            val dialogTemp1 = AlertDialog.Builder(this)
            val dialog1 = dialogTemp1.create()
            val dialogView = layoutInflater.inflate(R.layout.address_dialog1,null)
            val findBtn = dialogView.findViewById<Button>(R.id.findBtn)
            val editText = dialogView.findViewById<EditText>(R.id.questionTextView)
            val searchResultTextView = dialogView.findViewById<TextView>(R.id.addressResultTextView)
            val confirmBtn = dialogView.findViewById<Button>(R.id.confirmButton)
            dialog1.setView(dialogView)
            dialog1.show()
            findBtn.setOnClickListener{
                try{
                    val tempAddress = getGeocodeFromAddress(editText.toString())
                    searchResultTextView.text = tempAddress.getAddressLine(0)
                }
                catch(e: Exception) {
                    val dialogTemp2 = AlertDialog.Builder(this)
                    val dialog2 = dialogTemp2.create()
                    val dialogViewTemp = layoutInflater.inflate(R.layout.common_alert_dialog,null)
                    val alertMessage = dialogViewTemp.findViewById<TextView>(R.id.alertMessage)
                    alertMessage.text = "유효하지 않은 주소\n(혹은 입력)입니다"
                    dialog2.setView(dialogViewTemp)
                    dialog2.show()
                    dialogViewTemp.findViewById<Button>(R.id.confirmButton).setOnClickListener{
                        dialog2.dismiss()
                    }
                }
            }
            confirmBtn.setOnClickListener{
                if (dialogView.findViewById<TextView>(R.id.addressResultTextView).text == "")
                {
                    val dialogTemp2 = AlertDialog.Builder(this)
                    val dialog2 = dialogTemp2.create()
                    val dialogViewTemp = layoutInflater.inflate(R.layout.common_alert_dialog,null)
                    val alertMessage = dialogViewTemp.findViewById<TextView>(R.id.alertMessage)
                    alertMessage.text = "유효하지 않은 주소\n(혹은 입력)입니다"
                    dialog2.setView(dialogViewTemp)
                    dialog2.show()
                    dialogViewTemp.findViewById<Button>(R.id.confirmButton).setOnClickListener{
                        dialog2.dismiss()
                    }
                }
                else {
                    findViewById<TextView>(R.id.addressTextView).text = dialogView.findViewById<TextView>(R.id.addressResultTextView).text
                    dialog1.dismiss()
                }
            }
        }
    }
}