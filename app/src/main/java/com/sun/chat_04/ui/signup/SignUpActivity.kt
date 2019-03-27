package com.sun.chat_04.ui.signup

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.OnClickListener
import android.widget.DatePicker
import android.widget.RadioButton
import com.jpardogo.android.googleprogressbar.library.ChromeFloatingCirclesDrawable
import com.sun.chat_04.R.color
import com.sun.chat_04.R.id
import com.sun.chat_04.R.layout
import kotlinx.android.synthetic.main.sign_up_screen.button_signUp
import kotlinx.android.synthetic.main.sign_up_screen.edit_confirm_password
import kotlinx.android.synthetic.main.sign_up_screen.edit_email
import kotlinx.android.synthetic.main.sign_up_screen.edit_fullname
import kotlinx.android.synthetic.main.sign_up_screen.edit_password
import kotlinx.android.synthetic.main.sign_up_screen.progress
import kotlinx.android.synthetic.main.sign_up_screen.radio_groups
import kotlinx.android.synthetic.main.sign_up_screen.text_birthday
import kotlinx.android.synthetic.main.sign_up_screen.toolbar
import java.text.SimpleDateFormat
import java.util.Calendar

class SignUpActivity : AppCompatActivity(), OnClickListener {
    private var mBirth: String = ""
    private var mName: String = ""
    private var mEmail: String = ""
    private var mPass: String = ""
    private var mGender: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.sign_up_screen)
        requirePermissions()
    }

    override fun onResume() {
        super.onResume()
        displayProgressBar()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSION_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    initComponents()
                } else {
                    requirePermissions()
                }
                return
            }
        }
    }

    @SuppressLint("NewApi")
    override fun onClick(v: View?) {
        when (v?.id) {
            id.text_birthday -> showDatePicker()
            id.button_signUp -> handleSignUp()
        }
    }

    private fun requirePermissions() {
        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSION_CODE
            )
        } else {
            initComponents()
        }
    }

    private fun initComponents() {
        text_birthday.setOnClickListener(this)
        button_signUp.setOnClickListener(this)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            this.finish()
        }
    }

    private fun handleSignUp() {
        progress.visibility = View.VISIBLE
        mName = edit_fullname.text.toString()
        mBirth = text_birthday.text.toString()
        val viewId = radio_groups.findViewById<RadioButton>(radio_groups.checkedRadioButtonId)
        mGender = radio_groups.indexOfChild(viewId).toString()
        mEmail = edit_email.text.toString()
        mPass = edit_password.text.toString()
        val confirmPass = edit_confirm_password.text.toString()
    }

    private fun displayProgressBar() {
        val bounds = progress.indeterminateDrawable.bounds
        val progressDrawable = ChromeFloatingCirclesDrawable.Builder(this)
            .colors(
                intArrayOf(
                    ContextCompat.getColor(this, color.red),
                    ContextCompat.getColor(this, color.blue),
                    ContextCompat.getColor(this, color.yellow),
                    ContextCompat.getColor(this, color.green)
                )
            )
            .build()
        progress.indeterminateDrawable = progressDrawable
        progress.indeterminateDrawable.bounds = bounds
    }

    @SuppressLint("SimpleDateFormat")
    private fun showDatePicker() {
        val cal = Calendar.getInstance()
        val callback =
            DatePickerDialog.OnDateSetListener { datePicker: DatePicker, year: Int, month: Int, day: Int ->
                cal.set(year, month, day)
                val dateFormat = SimpleDateFormat(DATE_FORMAT)
                text_birthday.setText(dateFormat.format(cal.time).toString())
            }
        val picker = DatePickerDialog(
            this, callback, cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
        )
        picker.show()
    }

    companion object {
        val REQUEST_PERMISSION_CODE = 100
        val DATE_FORMAT = "dd/MM/yyyy"
    }
}
