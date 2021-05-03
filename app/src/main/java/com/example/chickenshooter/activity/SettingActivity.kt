package com.example.chickenshooter.activity

import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.chickenshooter.R
import com.example.chickenshooter.databinding.ActivitySettingBinding
import com.google.android.material.textfield.TextInputEditText
/**
 * Created by Dinh Sam Vu on 12/20/2021.
 */
class SettingActivity : BaseActivity() {
    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this@SettingActivity, R.layout.activity_setting)
        initView()
    }

    private fun initView() {
        binding.tvName.text = preferenceHelper.getName()

        binding.btnEditName.setOnClickListener {
            dialogEditName()
        }
        binding.btnSettingExit.setOnClickListener {
            finish()
        }
    }

    private fun dialogEditName() {
        val ownerInput = TextInputEditText(this)
        ownerInput.inputType = InputType.TYPE_CLASS_TEXT
        val myDialog =
            AlertDialog.Builder(this).setTitle("New name")
                .setView(ownerInput)

        myDialog.setPositiveButton("OK") { _, _ ->
            val result = ownerInput.text.toString().trim()
            if (result != "") {
                preferenceHelper.setName(result)
                binding.tvName.text = result
                notifyUser(this, "Change name successfully !")
            } else {
                notifyUser(this, "Name cannot be empty !")
            }
        }
        myDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        myDialog.show()
    }
}