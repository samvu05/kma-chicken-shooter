package com.example.chickenshooter.objects

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.chickenshooter.gameview.GameView.Companion.screenRatioX
import com.example.chickenshooter.gameview.GameView.Companion.screenRatioY
import com.example.chickenshooter.R
/**
 * Created by Dinh Sam Vu on 1/4/2021.
 */
class Figure(screenWidth: Float, screenHeight: Float, context: Context) : BaseObject2D() {
    var targetList = arrayOfNulls<Bitmap>(15)
    var targetFrame: Int = 0

    init {
        targetList[0] = BitmapFactory.decodeResource(context.resources, R.drawable.asset_spaceship1)
        targetList[1] = BitmapFactory.decodeResource(context.resources, R.drawable.asset_spaceship2)
        targetList[2] = BitmapFactory.decodeResource(context.resources, R.drawable.asset_spaceship3)
        targetList[3] = BitmapFactory.decodeResource(context.resources, R.drawable.asset_spaceship4)
        targetList[4] = BitmapFactory.decodeResource(context.resources, R.drawable.asset_spaceship5)
        targetList[5] = BitmapFactory.decodeResource(context.resources, R.drawable.asset_spaceship6)
        targetList[6] = BitmapFactory.decodeResource(context.resources, R.drawable.asset_spaceship7)

        width = targetList[0]!!.width * screenRatioX
        height = targetList[0]!!.height * screenRatioY
        x = (screenWidth / 2 - width / 2) * screenRatioX
        y = (screenHeight - 2 * height) * screenRatioY
    }

    open fun getBitmap(): Bitmap {
        return Bitmap.createScaledBitmap(
            targetList[targetFrame]!!,
            width.toInt(),
            height.toInt(),
            false
        )
    }
}