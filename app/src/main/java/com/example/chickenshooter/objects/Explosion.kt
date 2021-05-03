package com.example.chickenshooter.objects

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.chickenshooter.R
import com.example.chickenshooter.gameview.GameView

/**
 * Created by Dinh Sam Vu on 1/14/2021.
 */
class Explosion(context: Context) : BaseObject2D() {

    internal var explosion = arrayOfNulls<Bitmap>(8)
    internal var explosionFrame = 0

    init {
        explosion[0] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion_01)
        explosion[1] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion_02)
        explosion[2] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion_03)
        explosion[3] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion_04)
        explosion[4] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion_05)
        explosion[5] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion_06)
        explosion[6] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion_07)
        explosion[7] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion_08)

        width = explosion[0]!!.width * GameView.screenRatioX
        height = explosion[0]!!.height * GameView.screenRatioY
    }

    fun getExplosion(explosionFrame: Int): Bitmap {
        return Bitmap.createScaledBitmap(explosion[explosionFrame]!!, width.toInt(), height.toInt(), false)
    }
}