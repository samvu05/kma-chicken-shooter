package com.example.chickenshooter.gameview

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.media.AudioManager
import android.media.SoundPool
import android.view.MotionEvent
import android.view.SurfaceView
import android.view.ViewConfiguration
import com.example.chickenshooter.R
import com.example.chickenshooter.activity.GameActivity
import com.example.chickenshooter.activity.GameOverActivity
import com.example.chickenshooter.enum.ItemType
import com.example.chickenshooter.objects.*
import java.util.*
import kotlin.math.abs

/**
 * Created by Dinh Sam Vu on 1/2/2021.
 */
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
    private var isReady = false
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
    private var level = 1 // max 3
    private var bulletSpeed = 1 //max 4
    private var bulletLine = 1 //max 3
    private var lives = 4
    private var listBonusItem = arrayListOf<BonusItem>()
    private var listBonusItemType = listOf(
        ItemType.LINE_BUFF,
        ItemType.LINE_NEFF,
        ItemType.SPEED_BUFF,
        ItemType.SPEED_NEFF
    )

    private var lastChickenBorn: Long = 0
    private var lastBombBorn: Long = 0
    private var lastBonusItemBorn: Long = 0
    private var lastBulletBorn: Long = 0
    private var lastAnimHeartBorn: Long = 0

    private var readyButton: ReadyButton

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
    private var bombSoundID: Int = 0

    // Mark Chicken
    private var listBullet: ArrayList<Bullet>
    private var listChicken: ArrayList<Chicken>
    private var explosionList: ArrayList<Explosion>

    private var listBomb: ArrayList<FireBomb>


    private var background1: Background
    private var background2: Background


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
        heartIcon = BitmapFactory.decodeResource(mContext.resources, R.drawable.asset_heart1)
        heartIcon =
            Bitmap.createScaledBitmap(
                heartIcon,
                50 * screenRatioX.toInt(),
                50 * screenRatioY.toInt(),
                false
            )

        highScore = activity.preferenceHelper.getHighscore()

        readyButton = ReadyButton(mContext)
        readyButton.x = screenWidth / 2 - readyButton.width / 2
        readyButton.y = screenHeight / 2 - readyButton.height / 2

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
        bombSoundID = soundPool.load(context, R.raw.bomb, 1)



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
        if (!isReady) {
            if (motionTouchEventX in readyButton.x..readyButton.x + readyButton.width
                && motionTouchEventY in readyButton.y..readyButton.y + readyButton.width
            ) {
                isReady = true
            }
        }
    }


    private fun touchMove() {
        if (!isReady) return

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

            if (!isReady && !isGameOver) {
                canvas.drawBitmap(readyButton.readyButton, readyButton.x, readyButton.y, paint)
            }
            canvas.drawText("HighScore: $highScore", 20f, 60f, scorePaint)
            canvas.drawText("Score: $score", 20f, 120f, scorePaint)
            canvas.drawText("Live: ", 20f, 180f, scorePaint)
            for (i in 1..lives) {
                canvas.drawBitmap(heartIcon, 210f + i * heartIcon.width, 140f, null)
            }
            drawFigure(canvas)
            for (bullet in listBullet) {
                canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint)
            }

            drawExplodes(canvas)
            drawAllChicken(canvas)
            drawAllBombAndBonusItem(canvas)
            drawAnimHeart(canvas)
            holder.unlockCanvasAndPost(canvas)
        }
    }

    private fun drawFigure(canvas: Canvas) {
        canvas.drawBitmap(
            figure.getBitmap(),
            (figure.x),
            (figure.y),
            null
        )
        figure.targetFrame++
        if (figure.targetFrame > 6) figure.targetFrame = 0
    }


    private fun fireBullets() {
        //stockSpeed == 20
        if (currentTime - lastBulletBorn < if (bulletSpeed < 4) 25 - 5 * bulletSpeed else 5) {
            return
        }
        lastBulletBorn = currentTime
        when (bulletLine) {
            1 -> {
                val bullet = Bullet(mContext)
                bullet.x = figure.x + figureWidth / 2 - bullet.width / 2
                bullet.y = figure.y
                listBullet.add(bullet)
            }
            2 -> {
                listBullet.addAll(
                    listOf(
                        Bullet(mContext).apply {
                            x = figure.x - bullet.width / 2
                            y = figure.y
                        },
                        Bullet(mContext).apply {
                            x = figure.x + figure.width - bullet.width / 2
                            y = figure.y
                        })
                )
            }
            else -> {
                listBullet.addAll(
                    listOf(
                        Bullet(mContext).apply {
                            x = figure.x - bullet.width / 2
                            y = figure.y
                        },
                        Bullet(mContext).apply {
                            x = figure.x + figure.width - bullet.width / 2
                            y = figure.y
                        },
                        Bullet(mContext).apply {
                            x = figure.x + figure.width / 2 - bullet.width / 2
                            y = figure.y
                        })
                )
            }
        }

        if (fireShotSoundId != 0 && !activity.isMute) {
            soundPool.play(fireShotSoundId, 0.5f, 0.5f, 0, 0, 1f)
        }
    }

    private fun moveAllBullets() {
        val trashBullet: MutableList<Bullet> = arrayListOf()
        val trashChicken: MutableList<Chicken> = arrayListOf()

        for (bullet in listBullet) {
            if (bullet.y < 0) trashBullet.add(bullet)
            bullet.y -= 25 * screenRatioY
            if (bullet.y < -100f) {
                trashBullet.add(bullet)
            }
            for (chicken in listChicken) {
                if (Rect.intersects(
                        chicken.getCollisionShape(),
                        bullet.getCollisionShape()
                    )
                ) {
                    trashBullet.add(bullet)
                    bullet.y = -100f
                    if (bombSoundID != 0 && !activity.isMute) {
                        soundPool.play(bombSoundID, 0.8f, 0.8f, 0, 0, 1f)
                    }

                    chicken.livesCount--
                    if (chicken.livesCount == 0) {
                        score++
                        if (score > highScore) {
                            highScore = score
                            activity.preferenceHelper.setHighscore(score)
                            activity.preferenceHelper.setTopScoreName(activity.preferenceHelper.getName())
                        }
                        explosionList.add(Explosion(mContext).apply {
                            x = chicken.x + chicken.width / 2 - this.width / 2
                            y = chicken.y + chicken.height / 2 - this.height / 2
                        })
                        trashChicken.add(chicken)
                        chicken.x = -300f
                        if (fireShotSoundId != 0 && !activity.isMute) {
                            soundPool.play(fireShotSoundId, 1f, 1f, 0, 0, 1f)
                        }
                    }

                }
            }

        }
        for (bullet in trashBullet) listBullet.remove(bullet)
        if (trashBullet.size > 50) trashBullet.removeAll(trashBullet)
        for (chicken in trashChicken) listChicken.remove(chicken)
        if (trashChicken.size > 50) trashChicken.removeAll(trashChicken)
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
                if (explosionList[j].explosionFrame > 7) {
                    explosionList.removeAt(j)
                }
            }
        }
    }

    private fun bornChicken() {
        if (currentTime - lastChickenBorn < if (level < 3) 30 - (level - 1) * 5 else 20) {
            return
        }
        lastChickenBorn = currentTime
        when (level) {
            1 -> {
                listChicken.add(Chicken(mContext).apply {
                    x = random.nextInt((screenWidth - this.width - 200).toInt() + 50).toFloat()
                    y = -this.height
                    livesCount = 1
                })
            }
            else -> {
                val firstChickenX = random.nextInt((screenWidth - 350).toInt() + 50).toFloat()
                listChicken.add(Chicken(mContext).apply {
                    x = firstChickenX
                    y = -this.height
                    livesCount = if (level == 3) 2 else 5
                })

                listChicken.add(Chicken(mContext).apply {
                    x =
                        if (firstChickenX < screenWidth / 2) {
                            random.nextInt((screenWidth - firstChickenX - this.width).toInt()) + firstChickenX - 50
                        } else {
                            random.nextInt((firstChickenX - this.width).toInt()).toFloat()
                        }
                    y = -this.height
                    livesCount = if (level == 2) 2 else 5
                })

            }
        }
    }

    private fun moveAllChicken() {
        val trash: MutableList<Chicken> = arrayListOf()
        for (chicken in listChicken) {
            when (level) {
                1 -> chicken.y += 5
                2 -> chicken.y += 8
                else -> chicken.y += 17
            }


            if (chicken.y > screenHeight) {
                trash.add(chicken)
                lives--
                if (lives == 0) {
                    gameOver()
                }
            }

            if (Rect.intersects(chicken.getCollisionShape(), figure.getCollisionShape())) {
                trash.add(chicken)
                gameOver()
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
            if (chicken.targetFrame > 6) chicken.targetFrame = 0
        }
    }

    private fun fireBombAndBonusItem() {
        if (listChicken.size < 3 || currentTime - lastBombBorn < if (level == 1) 75 else if (level == 2) 50 else 20) {
            return
        }
        lastBombBorn = currentTime

        val i = random.nextInt(listChicken.size)
        var chickenFired = listChicken[i]
        listBomb.add(FireBomb(mContext).apply {
            x = chickenFired.x + chickenFired.width - width
            y = chickenFired.y
        })

        if (listChicken.size < 3 || currentTime - lastBonusItemBorn < if (level == 1) 150 else if (level == 2) 100 else 50) {
            return
        }
        lastBonusItemBorn = currentTime

        var j = random.nextInt(listChicken.size)
        while (j == i) {
            j = random.nextInt(listChicken.size)
        }
        chickenFired = listChicken[j]
        listBonusItem.add(BonusItem(mContext).apply {
            width = 100f
            height = 100f
            x = chickenFired.x + chickenFired.width - width
            y = chickenFired.y
            listBonusItemType = listOf(
                ItemType.LINE_BUFF,
                ItemType.LINE_NEFF,
                ItemType.SPEED_BUFF,
                ItemType.SPEED_NEFF
            )
            if (bulletLine == 1)
                listBonusItemType = listBonusItemType.filter { it != ItemType.LINE_NEFF }
            if (bulletLine == 3) {
                listBonusItemType = listBonusItemType.filter { it != ItemType.LINE_BUFF }
            }
            if (bulletSpeed == 1) {
                listBonusItemType = listBonusItemType.filter { it != ItemType.SPEED_NEFF }
            }
            if (bulletSpeed == 4) {
                listBonusItemType = listBonusItemType.filter { it != ItemType.SPEED_BUFF }
            }
            if (listBonusItemType.isNotEmpty()) {
                itemType = listBonusItemType.random()
            }

        })
    }

    private fun moveAllBomb() {
        val trash: MutableList<FireBomb> = arrayListOf()
        for (bomb in listBomb) {
            bomb.y += if (level == 1) 15 else if (level == 2) 20 else 30
            if (bomb.y > screenHeight) trash.add(bomb)

            if (Rect.intersects(bomb.getCollisionShape(), figure.getCollisionShape())) {
                trash.add(bomb)
                if (bombSoundID != 0 && !activity.isMute) {
                    soundPool.play(bombSoundID, 0.8f, 0.8f, 0, 0, 1f)
                }
                explosionList.add(Explosion(mContext).apply {
                    x = figure.x + figure.width / 2 - this.width / 2
                    y = figure.y + figure.height / 2 - this.height / 2
                })
                lives--
                if (lives == 0) gameOver()
            }
        }
        for (bomb in trash) listBomb.remove(bomb)
        if (trash.size > 100) trash.removeAll(trash)
    }

    private fun moveAllBonusItem() {
        val trash: MutableList<BonusItem> = arrayListOf()
        for (item in listBonusItem) {
            item.y += if (level == 1) 10 else if (level == 2) 15 else 25
            if (item.y > screenHeight) trash.add(item)

            if (Rect.intersects(item.getCollisionShape(), figure.getCollisionShape())) {
                trash.add(item)
                if (fireShotSoundId != 0 && !activity.isMute) {
                    soundPool.play(fireShotSoundId, 1f, 1f, 0, 0, 1f)
                }
                when (item.itemType) {
                    ItemType.SPEED_BUFF -> if (bulletSpeed < 4) bulletSpeed++
                    ItemType.SPEED_NEFF -> if (bulletSpeed > 1) bulletSpeed--
                    ItemType.LINE_BUFF -> if (bulletLine < 3) bulletLine++
                    ItemType.LINE_NEFF -> if (bulletLine > 1) bulletLine--
                }
            }
        }

        for (item in trash) listBonusItem.remove(item)
        if (trash.size > 100) trash.removeAll(trash)
    }


    private fun drawAllBombAndBonusItem(canvas: Canvas) {
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
        for (item in listBonusItem) {
            canvas.drawBitmap(
                item.getBitmap(),
                (item.x),
                (item.y),
                null
            )
        }
    }

    private fun addRandomAnimHeart() {
        if (currentTime - lastAnimHeartBorn < 500 || listAnimHeart.size == 1 || lives >= 4) {
            return
        }
        lastAnimHeartBorn = currentTime
        listAnimHeart.add(AnimHeart(mContext).apply {
            x = 50f + random.nextInt(screenWidth.toInt() - 200)
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
            if (animHeart.targetFrame > 2) animHeart.targetFrame = 0

            if (Rect.intersects(animHeart.getCollisionShape(), figure.getCollisionShape())) {
                if (lives < 4) lives++
                trash.add(animHeart)
            }
        }
        for (animHeart in trash) listAnimHeart.remove(animHeart)
    }


    private fun updateBackground() {
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
            if (isReady && !isGameOver) {
                fireBullets()
                moveAllBullets()

                bornChicken()
                moveAllChicken()

                fireBombAndBonusItem()
                moveAllBomb()
                moveAllBonusItem()

                addRandomAnimHeart()

                currentTime += 1
                try {
                    Thread.sleep(1)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            updateMainValue()
            updateBackground()
            drawAll()
        }

    }

    private fun updateMainValue() {
        if (currentTime in 3001..8999) {
            level = 2
        }
        if (currentTime >= 9000) {
            level = 3
        }
    }

    fun resume() {
        isPlaying = true
        isReady = false
        thread = Thread(this)
        thread.start()
    }

    fun pause() {
        try {
            isReady = false
            isPlaying = false
            thread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun destroy() {
        isPlaying = false
    }

    private fun gameOver() {
        isPlaying = false
        val intent = Intent(mContext, GameOverActivity::class.java)
        intent.putExtra("score", score)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        mContext.startActivity(intent)
        activity.finish()
    }
}