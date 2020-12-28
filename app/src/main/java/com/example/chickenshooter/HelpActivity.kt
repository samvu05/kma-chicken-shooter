package com.example.chickenshooter

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.chickenshooter.databinding.ActivityHelpBinding


class HelpActivity : BaseActivity() {
    private lateinit var binding: ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@HelpActivity, R.layout.activity_help)
    }
}