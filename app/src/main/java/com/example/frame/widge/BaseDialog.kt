package com.example.frame.widge

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import kotlin.Exception as Exception1

/**
 * @ClassName: BaseDialog
 * @Description: Dialog公用属性配置
 * @Author: zhangsicong
 * @Date: 2019-06-19 11:52
 */
open class BaseDialog(context: Context) : Dialog(context) {

    init {
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE)
            //设置背景为透明，不设置的话圆角没效果
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        } catch (e: Exception) {

        }
    }

    /**
     * 设置dialog居中方式
     */
    fun setGravity(gravity:Int){
        window?.setGravity(gravity)
    }

}