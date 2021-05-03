package com.example.chickenshooter.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.chickenshooter.R
import com.example.chickenshooter.databinding.ActivityHelpBinding

/**
 * Created by Dinh Sam Vu on 12/20/2021.
 */
class HelpActivity : BaseActivity() {
    private lateinit var binding: ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@HelpActivity, R.layout.activity_help)
        initView()
    }

    private fun initView() {
        binding.btnHelpExit.setOnClickListener {
            finish()
        }
    }
}