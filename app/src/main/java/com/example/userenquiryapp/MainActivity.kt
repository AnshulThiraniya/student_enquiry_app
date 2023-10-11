package com.example.userenquiryapp

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.example.userenquiryapp.databinding.ActivityMainBinding
import com.google.gson.Gson
import java.util.Calendar

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding:ActivityMainBinding
    lateinit var myDbAdapter: DataBaseClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.submitBtn.setOnClickListener(this)

        binding.etDob.setEndIconOnClickListener {
            showDatePicker()
        }




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
        binding.etDob.editText?.addTextChangedListener {
            binding.etDob.error=null
        }

        var preferance=getSharedPreferences("SignUp_data", MODE_PRIVATE)
        var myAdmin=preferance.getString("key_signupName","Guest User")

        binding.adminText.text="Welcome:- ${myAdmin} "

        myDbAdapter=DataBaseClass(this)


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu_main,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
         R.id.op_home-> {
             Toast.makeText(this, "home", Toast.LENGTH_LONG).show()
         }
         R.id.op_history->{
            val intent=Intent(this@MainActivity,HistoryActivity::class.java)

             startActivity(intent)
         }

        }
        return super.onOptionsItemSelected(item)
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
        if (!isValidPhoneNumber(binding.mNumber.editText?.text.toString())) {
            binding.mNumber.error = "*started either 7,8 or 9"
        }
        if(binding.emailId.editText?.text?.isEmpty()==true){
            binding.emailId.error="*Required field"
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(binding.emailId.editText?.text.toString()).matches()) {
            binding.emailId.error = "Invalid email address"

        }
        if(!isValidEmail(binding.emailId.editText?.text.toString())) {
            binding.emailId.error = "Invalid email address"
        }
        if(binding.etDob.editText!!.text.toString().isEmpty()){

            val toast:Toast=Toast.makeText(this@MainActivity,"please select Date",Toast.LENGTH_LONG) as Toast
            toast.setGravity(Gravity.CENTER,0,0)
            toast.show()
        }

        else if(binding.firstName.editText?.text.toString().isNotEmpty() && binding.lastName.editText?.text.toString().isNotEmpty()
            && binding.mNumber.editText?.text.toString().isNotEmpty() && isValidEmail(binding.emailId.editText?.text.toString())
            && isValidPhoneNumber(binding.mNumber.editText?.text.toString()) && binding.emailId.editText?.text.toString().isNotEmpty() &&
            binding.etDob.editText?.text.toString().isNotEmpty() )
        {

            val selectedGender = when {
                binding.rbMale.isChecked -> "Male"
                binding.rbFemale.isChecked -> "Female"
                binding.rbOther.isChecked -> "Other"
                else -> {
                    Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show()
                    return
                }
            }


            val selectedSkill = mutableListOf<String>()
            if (binding.cbAndroid.isChecked) {
                selectedSkill.add("Android")
            }
            if (binding.cbPython.isChecked) {
                selectedSkill.add("Python")
            }
            if (binding.cbJavascript.isChecked){
                selectedSkill.add("JavaScript")
            }
            if (binding.cbReact.isChecked) {
                selectedSkill.add("React")
            }
            if (binding.cbFlutter.isChecked) {
                selectedSkill.add("Flutter")
            }

            if (selectedSkill.isEmpty()) {
                Toast.makeText(this, "Please select at least one technology", Toast.LENGTH_SHORT).show()
                return
            }


//            val myUserData=UserData(binding.firstName.editText?.text.toString(),binding.middleName.editText?.text.toString(),
//                binding.lastName.editText?.text.toString(),binding.mNumber.editText?.text.toString(),
//                binding.emailId.editText?.text.toString(),binding.etDob.editText?.text.toString(),
//                selectedGender, selectedLanguages.joinToString(", "))

            myDbAdapter.createData(binding.firstName.editText?.text.toString(),binding.middleName.editText?.text.toString(),
                binding.lastName.editText?.text.toString(),binding.mNumber.editText?.text.toString(),
                binding.emailId.editText?.text.toString(),binding.etDob.editText?.text.toString(),
                selectedGender,selectedSkill.joinToString(", "))


            val myIntent=Intent(this@MainActivity,HistoryActivity::class.java)
            startActivity(myIntent)
//            myIntent.putExtra(LocalKeys.USER_DATA,Gson().toJson(myUserData))


        }

    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val phoneNumberPattern = "^[789]\\d{9}$"
        return phoneNumber.matches(phoneNumberPattern.toRegex())
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]{2,3}"
        return email.matches(emailPattern.toRegex())
    }



    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit")
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes") { _, _ ->
                finishAffinity()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay-${selectedMonth + 1}-$selectedYear"
               binding.etDob.editText?.setText(selectedDate) }, year, month, day)

        datePickerDialog.show()
    }



}


