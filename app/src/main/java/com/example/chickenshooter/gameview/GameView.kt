package com.example.chickenshooter.gameview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.media.AudioManager
import android.media.SoundPool
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceView
import android.view.ViewConfiguration
import com.example.chickenshooter.R
import com.example.chickenshooter.activity.GameActivity
import com.example.chickenshooter.enum.BulletLine
import com.example.chickenshooter.enum.Ori
import com.example.chickenshooter.objects.*
import java.util.*
import kotlin.math.abs


@SuppressLint("ViewConstructor", "NewApi")
@Suppress("DEPRECATION", "UNREACHABLE_CODE")
class GameView(context: Context, point: Point) : SurfaceView(context), Runnable {

    companion object {
        var screenRatioX = 0f
        var screenRatioY = 0f
        var screenWidth = 0f
        var screenHeight = 0f
        private const val TEXT_SIZE = 40f
    }

    private var activity: GameActivity = context as GameActivity
    private var mContext: Context = context
    private var random: Random = Random()
    private var isPlaying = false
    private var isGameOver = false
    private lateinit var thread: Thread
    private var currentTime: Long = 0
    private var soundPool: SoundPool
    private var score = 0
    private var highScore = 0
    private var paint: Paint
    private var scorePaint = Paint()
    private var highScorePaint = Paint()
    private var heartIcon: Bitmap
    private var listAnimHeart = arrayListOf<AnimHeart>()
    private var level = 1
    private var bulletType = BulletLine.THREE_LINE

    private var lives = 4

    private var lastChickenBorn: Long = 0
    private var lastBombBorn: Long = 0
    private var lastBulletBorn: Long = 0
    private var lastBulletMove: Long = 0
    private var lastAnimHeartBorn: Long = 0

    //  Mark Figure & Control.
    private var figure: Figure
    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop
    private var figureWidth = 0f
    private var figureHeight = 0f
    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f
    private var currentX = 0f
    private var currentY = 0f

    private var fireShotSoundId: Int = 0

    // Mark Chicken
    private var listBullet: ArrayList<SingleBullet>
    private var listChicken: ArrayList<Chicken>
    private var listChickenLeft = arrayListOf<Chicken>()
    private var listChickenRight = arrayListOf<Chicken>()
    private var explosionList: ArrayList<Explosion>

    private var listBomb: ArrayList<FireBomb>


    private var background1: Background
    private var background2: Background
    private var lastBackgroundMove: Long = 0


    init {
        screenWidth = point.x.toFloat()
        screenHeight = point.y.toFloat()
        screenRatioX = 1080 / screenWidth
        screenRatioY = 2130 / screenHeight
        figure = Figure(screenWidth, screenHeight, mContext)
        background1 = Background(screenWidth, screenHeight, resources)
        background2 = Background(screenWidth, screenHeight, resources)
        background1.y = screenHeight
        background2.y = 0f
        heartIcon = BitmapFactory.decodeResource(mContext.resources, R.drawable.icon_heart_read)
        heartIcon =
            Bitmap.createScaledBitmap(
                heartIcon,
                50 * screenRatioX.toInt(),
                50 * screenRatioY.toInt(),
                false
            )

        highScore = activity.preferenceHelper.getHighscore()

        paint = Paint()
        paint.textSize = 128f
        paint.color = Color.WHITE

        scorePaint.color = Color.WHITE
        scorePaint.textSize = TEXT_SIZE
        scorePaint.textAlign = Paint.Align.LEFT
        scorePaint.typeface = mContext.resources.getFont(R.font.atari_pixel)

        highScorePaint.color = Color.RED
        highScorePaint.textSize = TEXT_SIZE
        highScorePaint.textAlign = Paint.Align.LEFT
        highScorePaint.typeface = mContext.resources.getFont(R.font.atari_pixel)


        soundPool = SoundPool(3, AudioManager.STREAM_MUSIC, 0)
        fireShotSoundId = soundPool.load(context, R.raw.fireshot, 1)



        listBullet = arrayListOf()
        figureWidth = figure.width
        figureHeight = figure.height

        listChicken = arrayListOf()
        explosionList = arrayListOf()
        listBomb = arrayListOf()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (figure.y > screenHeight) {
            figure.y = screenHeight - 2f
            return true
        }
        if (figure.y < 0) {
            figure.y = 2f
            return true
        }

        motionTouchEventX = event.x
        motionTouchEventY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
        }
        return true
    }

    private fun touchStart() {
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }


    private fun touchMove() {
        val dx = abs(motionTouchEventX - currentX)
        val dy = abs(motionTouchEventY - currentY)
        if (dx >= touchTolerance || dy >= touchTolerance) {
            if (figure.x + motionTouchEventX - currentX > 0 && figure.x + motionTouchEventX - currentX < screenWidth - figure.width) {
                figure.x += (motionTouchEventX - currentX).toInt()
            }
            figure.y += (motionTouchEventY - currentY).toInt()

            currentX = motionTouchEventX
            currentY = motionTouchEventY
        }
    }

    private fun drawAll() {
        if (holder.surface.isValid) {
            val canvas = holder.lockCanvas()
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint)
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint)
            canvas.drawBitmap(figure.fingure, figure.x, figure.y, paint)
            canvas.drawText("HighScore: $highScore", 20f, 60f, scorePaint)
            canvas.drawText("Score: $score", 20f, 120f, scorePaint)
            canvas.drawText("Live: ", 20f, 180f, scorePaint)

            for (i in 1..lives) {
                canvas.drawBitmap(heartIcon, 210f + i * heartIcon.width, 140f, null)
            }

            for (bullet in listBullet) {
                canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint)
            }

            drawExplodes(canvas)
            drawAllChicken(canvas)
            drawAllBomb(canvas)
            drawAnimHeart(canvas)
            holder.unlockCanvasAndPost(canvas)
        }
    }

    private fun fireBullets() {
        if (currentTime - lastBulletBorn < 20) {
            return
        }
        lastBulletBorn = currentTime
        when (bulletType) {
            BulletLine.ONE_LINE -> {
                val bullet = SingleBullet(mContext)
                bullet.x = figure.x + figureWidth / 2 - bullet.width / 2
                bullet.y = figure.y
                listBullet.add(bullet)
            }
            BulletLine.TWO_LINE -> {
                listBullet.addAll(
                    listOf(
                        SingleBullet(mContext).apply {
                            x = figure.x - bullet.width / 2
                            y = figure.y
                        },
                        SingleBullet(mContext).apply {
                            x = figure.x + figure.width - bullet.width / 2
                            y = figure.y
                        })
                )
            }
            BulletLine.THREE_LINE -> {
                listBullet.addAll(
                    listOf(
                        SingleBullet(mContext).apply {
                            x = figure.x - bullet.width / 2
                            y = figure.y
                        },
                        SingleBullet(mContext).apply {
                            x = figure.x + figure.width - bullet.width / 2
                            y = figure.y
                        },
                        SingleBullet(mContext).apply {
                            x = figure.x + figure.width / 2 - bullet.width / 2
                            y = figure.y
                        })
                )
            }

            else -> return
        }

        if (fireShotSoundId != 0) {
            soundPool.play(fireShotSoundId, 0.5f, 0.5f, 0, 0, 1f)
        }
    }

    private fun moveAllBullets() {
        val trashBullet: MutableList<SingleBullet> = arrayListOf()
        val trashChicken: MutableList<Chicken> = arrayListOf()

        for (bullet in listBullet) {
            if (bullet.y < 0) trashBullet.add(bullet)
            bullet.y -= 25 * screenRatioY
            for (chicken in listChicken) {
                if (Rect.intersects(
                        chicken.getCollisionShape(),
                        bullet.getCollisionShape()
                    )
                ) {
                    score++
                    if (score > highScore) {
                        highScore = score
                        activity.preferenceHelper.setHighscore(score)
                        activity.preferenceHelper.setTopScoreName(activity.preferenceHelper.getName())
                    }
                    explosionList.add(Explosion(mContext).apply {
                        x = chicken.x + chicken.width / 2 - this.width
                        y = chicken.y + chicken.height / 2 - this.height
                    })
                    trashChicken.add(chicken)
                    chicken.x = -300f
                    bullet.y = -100f
                    if (fireShotSoundId != 0) {
                        soundPool.play(fireShotSoundId, 1f, 1f, 0, 0, 1f)
                    }
                }
            }

        }
        for (bullet in trashBullet) listBullet.remove(bullet)
        for (chicken in trashChicken) listChicken.remove(chicken)
    }

    private fun drawExplodes(canvas: Canvas) {
        for (j in 0 until explosionList.size) {
            if (j < explosionList.size) {
                canvas.drawBitmap(
                    explosionList[j].getExplosion(explosionList[j].explosionFrame),
                    explosionList[j].x,
                    explosionList[j].y,
                    null
                )
                explosionList[j].explosionFrame++
                if (explosionList[j].explosionFrame > 6) {
                    explosionList.removeAt(j)
                }
            }
        }
    }

    private fun bornChicken() {
        if (currentTime - lastChickenBorn < 40) {
            return
        }
        lastChickenBorn = currentTime

        listChicken.add(Chicken(mContext).apply {
            x = random.nextInt((screenWidth - width).toInt()).toFloat()
            y = 0f
        })
    }

    private fun moveAllChicken() {
        val trash: MutableList<Chicken> = arrayListOf()
        for (chicken in listChicken) {
            chicken.y += 5
            if (chicken.y > screenHeight) {
                trash.add(chicken)
                lives--
            }

            if (Rect.intersects(chicken.getCollisionShape(), figure.getCollisionShape())) {
                trash.add(chicken)
                soundPool.play(fireShotSoundId, 1f, 1f, 0, 0, 1f)
                isGameOver = true
                Log.d("logDB", "game over")
            }
        }
        for (chicken in trash) listChicken.remove(chicken)
        if (trash.size > 10) trash.removeAll(trash)
    }

    private fun drawAllChicken(canvas: Canvas) {
        for (chicken in listChicken) {
            canvas.drawBitmap(
                chicken.getBitmap(),
                (chicken.x),
                (chicken.y),
                null
            )

            chicken.targetFrame++
            if (chicken.targetFrame > 9) chicken.targetFrame = 0
        }
    }

    private fun fireBomb() {
        if (currentTime - lastBombBorn < 100 || listChicken.size < 3) {
            return
        }
        lastBombBorn = currentTime

        val i = random.nextInt(listChicken.size)
        val chickenFired = listChicken[i]

        listBomb.add(FireBomb(mContext).apply {
            x = chickenFired.x + chickenFired.width - width
            y = chickenFired.y
        })
    }

    private fun moveAllBomb() {
        val trash: MutableList<FireBomb> = arrayListOf()
        for (bomb in listBomb) {
            bomb.y += 20
            if (bomb.y > screenHeight) trash.add(bomb)

            if (Rect.intersects(bomb.getCollisionShape(), figure.getCollisionShape())) {
                trash.add(bomb)
                soundPool.play(fireShotSoundId, 1f, 1f, 0, 0, 1f)
                isGameOver = true
                Log.d("logDB", "game over")
            }
        }
        for (bomb in trash) listBomb.remove(bomb)
        if (trash.size > 100) trash.removeAll(trash)
    }

    private fun drawAllBomb(canvas: Canvas) {
        for (bomb in listBomb) {
            canvas.drawBitmap(
                bomb.getBitmap(),
                (bomb.x),
                (bomb.y),
                null
            )

            bomb.targetFrame++
            if (bomb.targetFrame > 6) bomb.targetFrame = 0
        }
    }

    private fun addRandomAnimHeart() {
        if (currentTime - lastAnimHeartBorn < 400 || listAnimHeart.size >= 1 || lives >= 4) {
            return
        }
        lastAnimHeartBorn = currentTime
        listAnimHeart.add(AnimHeart(mContext).apply {
            x = 50f + random.nextInt(screenWidth.toInt() - 100)
            y = 200f + random.nextInt(screenHeight.toInt() - 300)
        })
    }

    private fun drawAnimHeart(canvas: Canvas) {
        val trash = arrayListOf<AnimHeart>()
        for (animHeart in listAnimHeart) {
            canvas.drawBitmap(
                animHeart.getBitmap(),
                (animHeart.x),
                (animHeart.y),
                null
            )
            animHeart.targetFrame++
            if (animHeart.targetFrame > 6) animHeart.targetFrame = 0

            if (Rect.intersects(animHeart.getCollisionShape(), figure.getCollisionShape())) {
                if (lives < 4) lives++
                trash.add(animHeart)
            }
        }
        for (animHeart in trash) listAnimHeart.remove(animHeart)
    }


    private fun updateBackground() {
        if (currentTime - lastBackgroundMove < 1) {
            return
        }
        lastBackgroundMove = currentTime

        background1.y += (5 * screenRatioY).toInt()
        background2.y += (5 * screenRatioY).toInt()

        if (background1.y - background1.background.height > 0) {
            background1.y = -screenHeight
        }
        if (background2.y - background2.background.height > 0) {
            background2.y = -screenHeight
        }
    }


    override fun run() {
        while (isPlaying) {
            updateBackground()
            fireBullets()
            moveAllBullets()

            bornChicken()
            moveAllChicken()

            fireBomb()
            moveAllBomb()

            addRandomAnimHeart()

            drawAll()

            currentTime += 1
            try {
                Thread.sleep(1)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
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

    private fun bornChicken2() {
        if (currentTime - lastChickenBorn < 50) {
            return
        }
        lastChickenBorn = currentTime
        if (listChicken.size > 20) return
        for (i in 0..3) {
            when (i) {
                0 -> {
                    listChickenLeft.add(Chicken(mContext).apply {
                        width = (screenWidth - 100) / 10
                        height = width
                        ori = Ori.LEFT
                        x = 0f
                        y = height.toFloat() + 50f
                    })
                }
                2 -> {
                    listChickenLeft.add(Chicken(mContext).apply {
                        width = ((screenWidth - 100) / 5)
                        height = width
                        ori = Ori.LEFT
                        x = 0f
                        y = 3 * height.toFloat() + 50f
                    })
                }
                1 -> {
                    listChickenRight.add(Chicken(mContext).apply {
                        width = ((screenWidth - 100) / 5)
                        height = width
                        ori = Ori.RIGHT
                        x = screenWidth
                        y = 5 * height.toFloat() + 50f
                    })
                }
                3 -> {
                    listChickenRight.add(Chicken(mContext).apply {
                        width = ((screenWidth - 100) / 5)
                        height = width
                        ori = Ori.RIGHT
                        x = screenWidth
                        y = 5 * height.toFloat() + 50f
                    })
                }
            }
        }

        listChicken = (listChickenLeft + listChickenRight) as ArrayList<Chicken>
        listBomb.add(FireBomb(mContext))
    }

    private fun moveAllChicken2() {
        for (i in 0 until listChickenRight.size) {
            if (i == 0) {
                if (listChickenRight[i].x > 20f) {
                    listChickenRight[i].x -= 20
                }
            } else {
                if (listChickenRight[i].x - listChickenRight[i - 1].x > (listChickenRight[i].width)) {
                    listChickenRight[i].x -= 20
                }
            }
        }

        for (i in 0 until listChickenLeft.size) {
            if (i == 0) {
                if (listChickenLeft[i].x < screenWidth - 20f - listChickenLeft[i].width) {
                    listChickenLeft[i].x += 20
                }
            } else {
                if (listChickenLeft[i - 1].x - listChickenLeft[i].x > (listChickenLeft[i].width)) {
                    listChickenLeft[i].x += 20
                }
            }
        }
    }


}