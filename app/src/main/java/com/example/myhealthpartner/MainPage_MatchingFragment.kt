package com.example.myhealthpartner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

class MainPage_MatchingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_page_matching_fragment, container, false)
        val checkboxLinear = view.findViewById<LinearLayout>(R.id.checkboxLinear)
        val timeCheckbox = view.findViewById<LinearLayout>(R.id.timeCheckbox)
        timeCheckbox.visibility = View.GONE
        checkboxLinear.visibility = View.GONE
        initEvent(view)
        return view
    }

    fun timeChecked(myView: View) :String {
        val checkBoxes = arrayListOf<CheckBox>()
        var userChecked = String()
        checkBoxes.add(myView.findViewById<CheckBox>(R.id.timeCheckBox1))
        checkBoxes.add(myView.findViewById<CheckBox>(R.id.timeCheckBox2))
        checkBoxes.add(myView.findViewById<CheckBox>(R.id.timeCheckBox3))
        checkBoxes.add(myView.findViewById<CheckBox>(R.id.timeCheckBox4))

        for(index in 0 until 4){
            if (checkBoxes[index].isChecked){
                userChecked += "T"
            }
            else {
                userChecked += "F"
            }
        }
        return userChecked
    }

    fun exerciseChecked(myView: View) : String {
        val checkBoxes = arrayListOf<CheckBox>()
        var userChecked = String()
        checkBoxes.add(myView.findViewById<CheckBox>(R.id.exerCheck1))
        checkBoxes.add(myView.findViewById<CheckBox>(R.id.exerCheck2))
        checkBoxes.add(myView.findViewById<CheckBox>(R.id.exerCheck3))
        checkBoxes.add(myView.findViewById<CheckBox>(R.id.exerCheck4))


        for(index in 0 until 4){
            if (checkBoxes[index].isChecked){
                userChecked += "T"
            }
            else {
                userChecked += "F"
            }
        }
        return userChecked
    }


    fun initEvent(myView: View) {
        val matchingStartBtn = myView.findViewById<Button>(R.id.matchingstartBtn)
        val loginData = context?.getSharedPreferences("loginData", 0)
        val checkboxLinear = myView.findViewById<LinearLayout>(R.id.checkboxLinear)
        val timeCheckbox = myView.findViewById<LinearLayout>(R.id.timeCheckbox)
        val timeCheckBtn = myView.findViewById<ImageButton>(R.id.timeCheckBtn)
        val fitnessListBtn = myView.findViewById<ImageButton>(R.id.fitnessListBtn)
//        val baseAddressBtn = myView.findViewById<Button>(R.id.baseAddressBtn)
//
//        baseAddressBtn.setOnClickListener {
//            val addressTextView = myView.findViewById<TextView>(R.id.addressTextView)
//            addressTextView.text = loginData!!.getString("address", "")
//        }

        fitnessListBtn.setOnClickListener {
            if(checkboxLinear.visibility == View.GONE){
                checkboxLinear.visibility = View.VISIBLE
            }
            else{
                checkboxLinear.visibility = View.GONE
            }
        }
        timeCheckBtn.setOnClickListener {
            if(timeCheckbox.visibility == View.GONE) timeCheckbox.visibility = View.VISIBLE
            else timeCheckbox.visibility = View.GONE
        }

        matchingStartBtn.setOnClickListener {
            val userExerciseChecked = exerciseChecked(myView)
            val userTimeChecked = timeChecked(myView)
            val bundle = Bundle()
            bundle.putString("timeChecked", userExerciseChecked)
            bundle.putString("exerciseChecked", userTimeChecked)
            val matchingResult = MainPage_Matching_Result_fragment()
            matchingResult.arguments = bundle
            parentFragmentManager.beginTransaction().replace(R.id.fragmentBox, matchingResult).addToBackStack(null).commitAllowingStateLoss()

        }
    }



}


