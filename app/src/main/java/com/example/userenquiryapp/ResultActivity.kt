package com.example.userenquiryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.gson.Gson

class ResultActivity : AppCompatActivity() {
    lateinit var tvResult:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        tvResult=findViewById(R.id.tv_result)

        val getUserData=intent.getStringExtra(LocalKeys.USER_DATA)
        val userData=Gson().fromJson(getUserData,UserData::class.java)

        val fullname="${userData.firstName} ${userData.middleName} ${userData.lastName}"
        tvResult.text="Name:-    ${fullname}\n" +
                       "Phone:-   +91${userData.mobileNumber}\n"+
                       "Email:-     ${userData.emailId}\n"+
                       "Gender:-  ${userData.gender}\n"+
                       "Dob:-        ${userData.dateOfBirth}\n"+
                      "Skills:-     ${userData.selectedLanguages}"

    }
}