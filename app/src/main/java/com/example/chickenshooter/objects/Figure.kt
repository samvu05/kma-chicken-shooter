package com.example.chickenshooter.objects

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.chickenshooter.gameview.GameView.Companion.screenRatioX
import com.example.chickenshooter.gameview.GameView.Companion.screenRatioY
import com.example.chickenshooter.R

class Figure(screenWidth: Float, screenHeight: Float, context: Context) : BaseObject2D(context) {
    var isDied = 0
    var liveCount = 5
    var fingure: Bitmap

    init {
        fingure = BitmapFactory.decodeResource(context.resources, R.drawable.icon_fingure)
        width = fingure.width * screenRatioX
        height = fingure.width * screenRatioY
        x = (screenWidth / 2 - width / 2) * screenRatioX
        y = (screenHeight - 50) * screenRatioY
        fingure = Bitmap.createScaledBitmap(fingure, width.toInt(), height.toInt(), false)
    }
}