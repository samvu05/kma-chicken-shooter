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
class Chicken(context: Context) : BaseObject2D() {
    var targetList = arrayOfNulls<Bitmap>(15)
    var targetFrame: Int = 0
    var livesCount = 1

    init {
        targetList[0] = BitmapFactory.decodeResource(context.resources, R.drawable.asset_chicken1)
        targetList[1] = BitmapFactory.decodeResource(context.resources, R.drawable.asset_chicken2)
        targetList[2] = BitmapFactory.decodeResource(context.resources, R.drawable.asset_chicken3)
        targetList[3] = BitmapFactory.decodeResource(context.resources, R.drawable.asset_chicken4)
        targetList[4] = BitmapFactory.decodeResource(context.resources, R.drawable.asset_chicken5)
        targetList[5] = BitmapFactory.decodeResource(context.resources, R.drawable.asset_chicken6)
        targetList[6] = BitmapFactory.decodeResource(context.resources, R.drawable.asset_chicken7)

        width = targetList[0]!!.width * screenRatioX
        height = targetList[0]!!.height * screenRatioY
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
