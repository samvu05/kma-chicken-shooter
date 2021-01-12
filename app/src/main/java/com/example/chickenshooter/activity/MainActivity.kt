package com.example.chickenshooter.activity

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.chickenshooter.R
import com.example.chickenshooter.databinding.ActivityMainBinding


class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isMute: Boolean = false
    private lateinit var mp: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        initViews()
        setViewEvents()
    }

    private fun initViews() {
        mp = MediaPlayer.create(this, R.raw.menu_mp)
        isMute = preferenceHelper.isMute()
        if (isMute) {
            binding.btnSound.setImageResource(R.drawable.ic_sound_off)
            mp.pause()
        } else {
            binding.btnSound.setImageResource(R.drawable.ic_sound_on)
            mp.start()
        }

        binding.tvName.text = preferenceHelper.getName()
    }

    private fun setViewEvents() {
        binding.btnPlay.setOnClickListener {
            startActivity(
                Intent(this@MainActivity, GameActivity::class.java)
            )
        }
        binding.btnHelp.setOnClickListener {
            startActivity(Intent(this@MainActivity, HelpActivity::class.java))
        }
        binding.btnHighScore.setOnClickListener {
            startActivity(Intent(this@MainActivity, HighScoreActivity::class.java))
        }
        binding.btnSetting.setOnClickListener {
            startActivity(Intent(this@MainActivity, SettingActivity::class.java))
        }
        binding.btnExit.setOnClickListener {
            finish()
        }
        binding.btnSound.setOnClickListener {
            isMute = !isMute
            if (isMute) {
                binding.btnSound.setImageResource(R.drawable.ic_sound_off)
                mp.pause()

            } else {
                binding.btnSound.setImageResource(R.drawable.ic_sound_on)
                mp.start()

            }
            preferenceHelper.setIsMute(isMute)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.tvName.text = preferenceHelper.getName()
        if (isMute) {
            mp.pause()
        } else mp.start()
    }

    override fun onPause() {
        super.onPause()
        Log.d("logDB", "pause")
        mp.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("logDB", "destroy")
        mp.release()
    }
}