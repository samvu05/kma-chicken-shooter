package com.example.chickenshooter.objects

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.chickenshooter.R
import com.example.chickenshooter.gameview.GameView
import java.util.*

class FireBomb(context: Context) : BaseObject2D(context) {
    var targetList = arrayOfNulls<Bitmap>(15)
    var targetFrame: Int = 0

    init {
        targetList[0] = BitmapFactory.decodeResource(context.resources, R.drawable.yellowtarget_1)
        targetList[1] = BitmapFactory.decodeResource(context.resources, R.drawable.yellowtarget_1)
        targetList[2] = BitmapFactory.decodeResource(context.resources, R.drawable.yellowtarget_1)
        targetList[3] = BitmapFactory.decodeResource(context.resources, R.drawable.yellowtarget_1)
        targetList[4] = BitmapFactory.decodeResource(context.resources, R.drawable.yellowtarget_1)
        targetList[5] = BitmapFactory.decodeResource(context.resources, R.drawable.yellowtarget_2)
        targetList[6] = BitmapFactory.decodeResource(context.resources, R.drawable.yellowtarget_2)
        targetList[7] = BitmapFactory.decodeResource(context.resources, R.drawable.yellowtarget_2)
        targetList[8] = BitmapFactory.decodeResource(context.resources, R.drawable.yellowtarget_2)
        targetList[9] = BitmapFactory.decodeResource(context.resources, R.drawable.yellowtarget_2)

        width = targetList[0]!!.width * GameView.screenRatioX
        height = targetList[0]!!.height * GameView.screenRatioY
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
