package com.example.chickenshooter

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.ImageFormat
import android.graphics.Paint
import android.graphics.Rect
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Build
import android.view.SurfaceView

class GameView : SurfaceView, Runnable {
    companion object {
        var screenRatioX = 0f
        var screenRatioY = 0f
    }

    private lateinit var thread: Thread
    private var isPlaying = false
    private var isGameOver = false
    private var isMute = false
    private var screenX: Float
    private var screenY: Float
    private var score = 0
    private var paint: Paint
    private var prefs: SharedPreferences
    private var soundPool: SoundPool
    private var sound = 0
    private var activity: GameActivity
    private var background1: Background
    private var background2: Background

    constructor(activity: GameActivity, screenX: Float, screenY: Float) : super(activity) {
        this.activity = activity
        prefs = activity.getSharedPreferences("GamePlanes", Context.MODE_PRIVATE)

        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build()
            SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .build()
        } else SoundPool(1, AudioManager.STREAM_MUSIC, 0)

//        sound = soundPool.load(activity, R.raw.shoot, 1)
        this.screenX = screenX
        this.screenY = screenY

        screenRatioX = 1080f / screenX
        screenRatioY = 1920f / screenY

        background1 = Background(screenX, screenY, resources)
        background2 = Background(screenX, screenY, resources)
        background1.y = screenY
        background2.y = 0f
        paint = Paint()
        paint.textSize = 128f
        paint.color = Color.WHITE
    }


    override fun run() {
        while (isPlaying) {
            update()
            draw()
            sleep()
        }
    }

    private fun update() {
        background1.y += (10 * screenRatioY).toInt()
        background2.y += (10 * screenRatioY).toInt()

        if (background1.y - background1.background.height > 0) {
            background1.y = -screenY
        }
        if (background2.y - background2.background.height > 0) {
            background2.y = -screenY
        }

//        when (flight.goingTo) {
//            0 -> {
//                flight.y -= (30 * screenRatioY).toInt()
//            }
//            1 -> {
//                flight.y += (30 * screenRatioY).toInt()
//            }
//            2 -> {
//                flight.x += (30 * screenRatioX).toInt()
//            }
//            3 -> {
//                flight.x -= (30 * screenRatioX).toInt()
//            }
//        }
//
//        if (flight.y < 0) {
//            flight.y = 0F
//            flight.goingTo = 1
//            soundPool.play(sound, 1f, 1f, 0, 0, 1f)
//        }
//        if (flight.y >= screenY - flight.height) {
//            flight.y = screenY - flight.height
//            flight.goingTo = 0
//            soundPool.play(sound, 1f, 1f, 0, 0, 1f)
//        }
//
//        if (flight.x > screenX / 2) {
//            flight.x = screenX / 2
//            flight.goingTo = 3
//            soundPool.play(sound, 1f, 1f, 0, 0, 1f)
//        }
//
//        if (flight.x < 0) {
//            flight.x = 0f
//            flight.isGoingRight = true
//            flight.goingTo = 2
//            soundPool.play(sound, 1f, 1f, 0, 0, 1f)
//        }
//
//        val trash: MutableList<Bullet> = arrayListOf()
//
//        for (bullet in bullets) {
//            if (bullet.x > screenX) trash.add(bullet)
//            bullet.x += (50 * screenRatioX).toInt()
//            for (bird in birds) {
//                if (Rect.intersects(
//                        bird.getCollisionShape(),
//                        bullet.getCollisionShape()
//                    )
//                ) {
//                    score++
//                    bird.x = -500
//                    bullet.x = screenX + 500
//                    bird.wasShot = true
//                }
//            }
//        }
//        for (bullet in trash) bullets.remove(bullet)

//        for (bird in birds) {
//            bird.x -= bird.speed
//            if (bird.x + bird.width < 0) {
////                if (!bird.wasShot) {
////                    isGameOver = true
////                    return
////                }
//                val bound = (30 * screenRatioX).toInt()
//                bird.speed = random.nextInt(bound)
//                if (bird.speed < 10 * screenRatioX) bird.speed =
//                    (10 * screenRatioX).toInt()
//                bird.x = screenX.toInt()
//                bird.y = random.nextInt((screenY - bird.height).toInt())
//                bird.wasShot = false
//            }
//
//            if (Rect.intersects(bird.getCollisionShape(), flight.getCollisionShape())) {
//                bird.wasShot = true
//                birds.remove(bird)
//                soundPool.play(sound, 1f, 1f, 0, 0, 1f)
//                isGameOver = true
//                break
//            }
//
//        }
    }

    private fun draw() {
        if (holder.surface.isValid) {
            val canvas = holder.lockCanvas()
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint)
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint)

//            if (isGameOver) {
//                isPlaying = false
//                val gameOver = "GAME OVER"
//                val widthString = paint.measureText(gameOver)
//                paint.color = Color.BLACK
//                canvas.drawText(gameOver, screenX / 2f - widthString/2, screenY / 2, paint)
//                holder.unlockCanvasAndPost(canvas)
//                saveIfHighScore()
//                waitBeforeExiting()
//                return
//            }
//            canvas.drawBitmap(flight.getFlight(), flight.x, flight.y, paint)
//            for (bird in birds) canvas.drawBitmap(
//                bird.getBird(),
//                bird.x.toFloat(),
//                bird.y.toFloat(),
//                paint
//            )
//            canvas.drawText(score.toString() + "", screenX / 2f, 164f, paint)
//
//
//            for (bullet in bullets) canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint)
            holder.unlockCanvasAndPost(canvas)
        }
    }

    private fun sleep() {
        try {
            Thread.sleep(15)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun resume() {
        isPlaying = true
        thread = Thread(this)
        thread.start()
    }

    fun pause() {
        try {
            isPlaying = false
            thread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun destroy() {
        isPlaying = false
    }

}