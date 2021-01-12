package com.example.chickenshooter.objects

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.chickenshooter.R
import com.example.chickenshooter.gameview.GameView

class AnimHeart(context: Context) : BaseObject2D(context) {

    internal var hearstList = arrayOfNulls<Bitmap>(7)
    internal var targetFrame = 0

    init {
        hearstList[0] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion0)
        hearstList[1] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion1)
        hearstList[2] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion2)
        hearstList[3] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion3)
        hearstList[4] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion4)
        hearstList[5] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion5)
        hearstList[6] = BitmapFactory.decodeResource(context.resources, R.drawable.explosion6)

        width = hearstList[0]!!.width * GameView.screenRatioX
        height = hearstList[0]!!.height * GameView.screenRatioY
    }

    fun getBitmap(): Bitmap {
        return Bitmap.createScaledBitmap(
            hearstList[targetFrame]!!,
            width.toInt(),
            height.toInt(),
            false
        )
    }
}