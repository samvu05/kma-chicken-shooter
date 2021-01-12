package com.example.chickenshooter.objects

import android.content.Context
import android.graphics.Rect
import com.example.chickenshooter.enum.Ori

open class BaseObject2D(context: Context) {
    var x: Float = 0f
    var y: Float = 0f
    var width = 0f
    var height = 0f
    lateinit var ori: Ori


    fun getCollisionShape(): Rect {
        return Rect(x.toInt(), y.toInt(), (x + width).toInt(), (y + height).toInt())
    }

}