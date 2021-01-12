package com.example.chickenshooter.objects

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.chickenshooter.R
import com.example.chickenshooter.gameview.GameView.Companion.screenRatioX
import com.example.chickenshooter.gameview.GameView.Companion.screenRatioY

class SingleBullet(context: Context) : BaseObject2D(context) {
    var bullet: Bitmap

    init {
        bullet = BitmapFactory.decodeResource(context.resources, R.drawable.missile)
        width = bullet.width * screenRatioX
        height = bullet.height * screenRatioY
        bullet = Bitmap.createScaledBitmap(bullet, width.toInt(), height.toInt(), false)
    }
}