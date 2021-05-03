package com.example.chickenshooter.objects

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.chickenshooter.R
import com.example.chickenshooter.gameview.GameView

/**
 * Created by Dinh Sam Vu on 1/14/2021.
 */
class FireBomb(context: Context) : BaseObject2D() {
    private var targetList = arrayOfNulls<Bitmap>(10)
    var targetFrame: Int = 0

    init {
        targetList[0] = BitmapFactory.decodeResource(context.resources, R.drawable.asset_bomb1)
        targetList[1] = BitmapFactory.decodeResource(context.resources, R.drawable.asset_bomb2)
        targetList[2] = BitmapFactory.decodeResource(context.resources, R.drawable.asset_bomb3)
        targetList[3] = BitmapFactory.decodeResource(context.resources, R.drawable.asset_bomb4)
        targetList[4] = BitmapFactory.decodeResource(context.resources, R.drawable.asset_bomb5)
        targetList[5] = BitmapFactory.decodeResource(context.resources, R.drawable.asset_bomb6)
        targetList[6] = BitmapFactory.decodeResource(context.resources, R.drawable.asset_bomb7)

        width = targetList[0]!!.width * GameView.screenRatioX
        height = targetList[0]!!.height * GameView.screenRatioY
    }

    fun getBitmap(): Bitmap {
        return Bitmap.createScaledBitmap(
            targetList[targetFrame]!!,
            width.toInt(),
            height.toInt(),
            false
        )
    }
}
