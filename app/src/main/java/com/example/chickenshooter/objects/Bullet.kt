package com.example.chickenshooter.objects

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.chickenshooter.R
import com.example.chickenshooter.gameview.GameView.Companion.screenRatioX
import com.example.chickenshooter.gameview.GameView.Companion.screenRatioY
/**
 * Created by Dinh Sam Vu on 1/4/2021.
 */
class Bullet(context: Context) : BaseObject2D() {
    var bullet: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.asset_bullet2)

    init {
        width = bullet.width * screenRatioX
        height = bullet.height * screenRatioY
        bullet = Bitmap.createScaledBitmap(bullet, width.toInt(), height.toInt(), false)
    }
}