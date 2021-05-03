package com.example.chickenshooter.objects

import android.graphics.Rect
/**
 * Created by Dinh Sam Vu on 1/4/2021.
 */
open class BaseObject2D {
    var x: Float = 0f
    var y: Float = 0f
    var width = 0f
    var height = 0f


    fun getCollisionShape(): Rect {
        return Rect(x.toInt(), y.toInt(), (x + width).toInt(), (y + height).toInt())
    }

}