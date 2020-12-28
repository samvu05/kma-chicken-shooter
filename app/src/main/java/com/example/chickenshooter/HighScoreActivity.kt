package com.example.chickenshooter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.chickenshooter.databinding.ActivityHighscoreBinding

class HighScoreActivity : BaseActivity() {
    private lateinit var binding: ActivityHighscoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this@HighScoreActivity, R.layout.activity_highscore)
    }
}