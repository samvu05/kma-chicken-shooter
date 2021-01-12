package com.example.chickenshooter.objects

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.chickenshooter.R
import com.example.chickenshooter.gameview.GameView


class Explosion(context: Context) : BaseObject2D(context) {

    internal var explosion = arrayOfNulls<Bitmap>(7)
    internal var explosionFrame = 0

    init {
        explosion[0] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion0)
        explosion[1] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion1)
        explosion[2] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion2)
        explosion[3] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion3)
        explosion[4] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion4)
        explosion[5] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion5)
        explosion[6] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion6)

        width = explosion[0]!!.width * GameView.screenRatioX
        height = explosion[0]!!.height * GameView.screenRatioY
    }

    fun getExplosion(explosionFrame: Int): Bitmap {
        return Bitmap.createScaledBitmap(explosion[explosionFrame]!!, width.toInt(), height.toInt(), false)
    }
}