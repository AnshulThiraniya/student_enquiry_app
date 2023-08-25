package com.example.userenquiryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isEmpty
import androidx.core.widget.addTextChangedListener
import com.example.userenquiryapp.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submitBtn.setOnClickListener(this)
        binding.firstName.editText?.addTextChangedListener {
            binding.firstName.error=null
        }
        binding.lastName.editText?.addTextChangedListener {
            binding.lastName.error=null
        }
        binding.mNumber.editText?.addTextChangedListener {
            binding.mNumber.error=null
        }
        binding.emailId.editText?.addTextChangedListener {
            binding.emailId.error=null
        }

    }

    override fun onClick(view: View?) {

        if(binding.firstName.editText?.text?.isEmpty()==true) {
            binding.firstName.error="*Required field"
        }
        if(binding.lastName.editText?.text?.isEmpty()==true){
            binding.lastName.error="*Required field"
        }
        if(binding.mNumber.editText?.text?.isEmpty()==true){
            binding.mNumber.error="*Required field"
        }
        if(binding.emailId.editText?.text?.isEmpty()==true){
            binding.emailId.error="*Required field"
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(binding.emailId.editText?.text.toString()).matches()) {
            binding.emailId.error = "Invalid email address"
        }

        if((binding.firstName.editText?.text?.isNotEmpty()==true)&&  (binding.lastName.editText?.text?.isNotEmpty()==true) &&
            (binding.mNumber.editText?.text?.isNotEmpty()==true) && (binding.emailId.editText?.text?.isNotEmpty()==true)){

            val myIntent=Intent(this,ResultActivity::class.java)

            val selectedGender = when {
                binding.rbMale.isChecked -> "Male"
                binding.rbFemale.isChecked -> "Female"
                binding.rbOther.isChecked -> "Other"
                else -> {
                    Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show()
                    return
                }
            }

            val selectedLanguages = mutableListOf<String>()
            if (binding.cbAndroid.isChecked) {
                selectedLanguages.add("Android")
            }
            if (binding.cbPython.isChecked) {
                selectedLanguages.add("Python")
            }
            if (binding.cbJavascript.isChecked){
                selectedLanguages.add("JavaScript")
            }
            if (binding.cbReact.isChecked) {
                selectedLanguages.add("React")
            }
            if (binding.cbFlutter.isChecked) {
                selectedLanguages.add("Flutter")
            }

            if (selectedLanguages.isEmpty()) {
                Toast.makeText(this, "Please select at least one technology", Toast.LENGTH_SHORT).show()
                return
            }

            val myUserData=UserData(binding.firstName.editText?.text.toString(),binding.middleName.editText?.text.toString(),
                binding.lastName.editText?.text.toString(),binding.mNumber.editText?.text.toString(),
                binding.emailId.editText?.text.toString(),
                selectedGender,
                selectedLanguages.joinToString(", ") )


            myIntent.putExtra(LocalKeys.USER_DATA,Gson().toJson(myUserData))

            startActivity(myIntent)


        }

        }

    override fun onBackPressed() {
        showExitConfirmationDialog()
    }

    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { _, _ ->
                finish()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}


