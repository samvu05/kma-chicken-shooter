package com.example.chickenshooter.activity

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.chickenshooter.R
import com.example.chickenshooter.databinding.ActivityHighscoreBinding
/**
 * Created by Dinh Sam Vu on 12/20/2021.
 */
class HighScoreActivity : BaseActivity() {
    private lateinit var binding: ActivityHighscoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this@HighScoreActivity, R.layout.activity_highscore)

        initView()
    }

    private fun initView() {
        binding.btnHighScoreExit.setOnClickListener {
            finish()
        }
        binding.viewHighScore.visibility =
            if (preferenceHelper.getHighscore() == 0) View.GONE else View.VISIBLE
        binding.emptyHighScore.visibility =
            if (preferenceHelper.getHighscore() == 0) View.VISIBLE else View.GONE

        binding.tvHighScoreName.text = preferenceHelper.getTopScoreName()
        binding.tvHighSocre.text = preferenceHelper.getHighscore().toString()

    }
}