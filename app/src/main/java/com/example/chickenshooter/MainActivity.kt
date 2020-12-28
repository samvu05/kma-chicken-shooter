package com.example.chickenshooter

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.chickenshooter.databinding.ActivityMainBinding


class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isMute = false
    private lateinit var mp: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        initViews()
        setViewEvents()
    }

    fun initViews() {
        mp = MediaPlayer.create(this, R.raw.menu_mp)
        isMute = prefs.getBoolean("isMute", false)
        if (isMute) {
            binding.btnSound.setImageResource(R.drawable.ic_sound_off)
        } else {
            binding.btnSound.setImageResource(R.drawable.ic_sound_on)
            mp.start()
        }
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
                if (mp != null && mp.isPlaying) {
                    mp.pause()
                }

            } else {
                binding.btnSound.setImageResource(
                    R.drawable.ic_sound_on
                )
                if (mp != null && !mp.isPlaying) {
                    mp.start()
                }
            }
            val editor = prefs.edit()
            editor.putBoolean("isMute", isMute)
            editor.apply()
        }
    }

    override fun onResume() {
        super.onResume()
        if (mp != null && !mp.isPlaying) {
            mp.start()
        }
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