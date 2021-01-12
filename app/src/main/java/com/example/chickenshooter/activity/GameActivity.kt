package com.example.chickenshooter.activity


import android.graphics.Point
import android.media.MediaPlayer
import android.os.Bundle
import com.example.chickenshooter.gameview.GameView
import com.example.chickenshooter.R

@Suppress("DEPRECATION")
class GameActivity : BaseActivity() {
    private lateinit var gameView: GameView
    private var isMute = false
    private lateinit var mpBackgroundSound: MediaPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val point = Point()
        windowManager.defaultDisplay.getSize(point)
        gameView = GameView(this, point)
        setContentView(gameView)
        mpBackgroundSound = MediaPlayer.create(this, R.raw.boss_mp)
        isMute = preferenceHelper.isMute()
        if (!isMute) {
            mpBackgroundSound.start()
        }
    }

    override fun onPause() {
        super.onPause()
        gameView.pause()
        if (mpBackgroundSound.isPlaying) {
            mpBackgroundSound.pause()
        }
    }

    override fun onResume() {
        super.onResume()

        gameView.resume()
        if (!this.isMute) {
            mpBackgroundSound.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mpBackgroundSound.release()
        gameView.destroy()
    }

}