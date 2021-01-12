package com.example.chickenshooter.objects

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.chickenshooter.R

class Background {
    var background: Bitmap
    var x = 0f
    var y = 0f

    constructor(screenWidth: Float, screenHeight: Float, res: Resources) {
        background = BitmapFactory.decodeResource(res, R.drawable.background1)
        background =
            Bitmap.createScaledBitmap(background, screenWidth.toInt(), screenHeight.toInt(), false)
    }

}