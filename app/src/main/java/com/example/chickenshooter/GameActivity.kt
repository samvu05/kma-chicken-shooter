package com.example.chickenshooter


import android.graphics.Point
import android.media.MediaPlayer
import android.os.Bundle

class GameActivity : BaseActivity() {
    private lateinit var gameView: GameView
    private var isMute = false
    private lateinit var mpBackgroundSound: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val point = Point()
        windowManager.defaultDisplay.getSize(point)
        gameView = GameView(this, point.x.toFloat(), point.y.toFloat())
        setContentView(gameView)
        mpBackgroundSound = MediaPlayer.create(this, R.raw.boss_mp)
        isMute = prefs.getBoolean("isMute", false)
        if (!isMute) {
            mpBackgroundSound.start()
        }
    }

    override fun onPause() {
        super.onPause()
        gameView.pause()
        if (mpBackgroundSound != null && mpBackgroundSound.isPlaying) {
            mpBackgroundSound.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        gameView.resume()
        if (mpBackgroundSound != null && !mpBackgroundSound.isPlaying) {
            mpBackgroundSound.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mpBackgroundSound.release()
        gameView.destroy()
    }

}