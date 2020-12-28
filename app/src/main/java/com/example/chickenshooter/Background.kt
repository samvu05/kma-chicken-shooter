package com.example.chickenshooter

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Background {
    var background: Bitmap
    var x: Float = 0f
    var y: Float = 0f

    constructor(screenX: Float, screenY: Float, res: Resources) {
        background = BitmapFactory.decodeResource(res, R.drawable.background1)
        background = Bitmap.createScaledBitmap(background, screenX.toInt(), screenY.toInt(), false)
    }

}