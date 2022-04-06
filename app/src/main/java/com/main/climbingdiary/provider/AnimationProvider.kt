package com.main.climbingdiary.provider

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils

object AnimationProvider {

    fun setAnimation(viewToAnimate: View,
                     context:Context,
                     animationId:Int ?= android.R.anim.slide_in_left ) {
        val animation: Animation =
            AnimationUtils.loadAnimation(context, animationId!!)
        viewToAnimate.startAnimation(animation)
    }
}