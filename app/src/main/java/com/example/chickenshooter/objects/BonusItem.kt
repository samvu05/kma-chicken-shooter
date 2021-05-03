package com.example.chickenshooter.objects

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.chickenshooter.R
import com.example.chickenshooter.enum.ItemType
import com.example.chickenshooter.gameview.GameView
/**
 * Created by Dinh Sam Vu on 1/14/2021.
 */
class BonusItem(context: Context) : BaseObject2D() {
    var itemType = ItemType.SPEED_BUFF

    private var bitmapLineBuff =
        BitmapFactory.decodeResource(context.resources, R.drawable.ic_item_bullet_line_buff)
    private var bitmapLineNeff =
        BitmapFactory.decodeResource(context.resources, R.drawable.ic_item_bullet_line_neff)
    private var bitmapSpeedBuff =
        BitmapFactory.decodeResource(context.resources, R.drawable.ic_item_speed_buff)
    private var bitmapSpeedNeff =
        BitmapFactory.decodeResource(context.resources, R.drawable.ic_item_speed_neff)

    init {
        width = bitmapLineBuff.width * GameView.screenRatioX
        height = bitmapLineBuff.width * GameView.screenRatioY
    }

    fun getBitmap(): Bitmap {
        var temp = bitmapLineBuff
        when (itemType) {
            ItemType.LINE_BUFF -> temp = bitmapLineBuff
            ItemType.LINE_NEFF -> temp = bitmapLineNeff
            ItemType.SPEED_BUFF -> temp = bitmapSpeedBuff
            ItemType.SPEED_NEFF -> temp = bitmapSpeedNeff
        }
        return Bitmap.createScaledBitmap(
            temp,
            width.toInt(),
            height.toInt(),
            false
        )

    }
}