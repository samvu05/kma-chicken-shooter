package com.example.chickenshooter.objects

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.chickenshooter.R
import com.example.chickenshooter.gameview.GameView
/**
 * Created by Dinh Sam Vu on 1/4/2021.
 */
class AnimHeart(context: Context) : BaseObject2D() {

    private var hearstList = arrayOfNulls<Bitmap>(3)
    internal var targetFrame = 0

    init {
        hearstList[0] = BitmapFactory.decodeResource(context.resources, R.drawable.asset_heart1)
        hearstList[1] = BitmapFactory.decodeResource(context.resources, R.drawable.asset_heart2)
        hearstList[2] = BitmapFactory.decodeResource(context.resources, R.drawable.asset_heart3)
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