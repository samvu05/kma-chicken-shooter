package com.example.chickenshooter.objects

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.chickenshooter.R
import com.example.chickenshooter.gameview.GameView

/**
 * Created by Dinh Sam Vu on 1/14/2021.
 */
class ReadyButton(context: Context) : BaseObject2D() {
    var readyButton: Bitmap =
        BitmapFactory.decodeResource(context.resources, R.drawable.ic_btn_play)

    init {
        width = 200 * GameView.screenRatioX
        height = 200 * GameView.screenRatioY
        readyButton = Bitmap.createScaledBitmap(readyButton, width.toInt(), height.toInt(), false)
    }
}