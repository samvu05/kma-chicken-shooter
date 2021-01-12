package com.example.chickenshooter.activity

import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chickenshooter.utils.PreferenceHelper

@Suppress("DEPRECATION")
open class BaseActivity : AppCompatActivity() {
    lateinit var preferenceHelper: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceHelper = PreferenceHelper(this, "myPref")
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    protected fun notifyUser(context: Context, mess: String) {
        Toast.makeText(context, mess, Toast.LENGTH_SHORT).show()
    }
}