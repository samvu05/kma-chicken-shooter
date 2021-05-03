package com.example.chickenshooter.objects

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.chickenshooter.R
/**
 * Created by Dinh Sam Vu on 1/4/2021.
 */
class Background(screenWidth: Float, screenHeight: Float, res: Resources) {
    var background: Bitmap = BitmapFactory.decodeResource(res, R.drawable.bg_play5)
    var x = 0f
    var y = 0f

    init {
        background =
            Bitmap.createScaledBitmap(background, screenWidth.toInt(), screenHeight.toInt(), false)
    }

}