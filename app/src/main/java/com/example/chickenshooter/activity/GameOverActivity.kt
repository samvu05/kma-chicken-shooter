package com.example.chickenshooter.activity

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.chickenshooter.R
import com.example.chickenshooter.databinding.ActivityGameOverBinding
/**
 * Created by Dinh Sam Vu on 12/20/2021.
 */
class GameOverActivity : BaseActivity() {
    private lateinit var binding: ActivityGameOverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@GameOverActivity, R.layout.activity_game_over)
        initView()
    }

    private fun initView() {
        binding.tvScore.text = intent.extras!!.getInt("score").toString()
        binding.btnExit.setOnClickListener {
            startActivity(Intent(this@GameOverActivity, MainActivity::class.java))
            finish()
        }
        binding.btnReplay.setOnClickListener {
            startActivity(Intent(this@GameOverActivity, GameActivity::class.java))
            finish()
        }
    }
}