package com.example.frame.base

import android.view.View

/**
 * @author mxs
 * @date 2019-06-26 16:12
 * @description 点击事件
 */
interface Presenter : View.OnClickListener {

    override fun onClick(v: View?)

    /**
     * 点击自定义控件ViewGroup中的某一个View
     * parentId 为ViewGroup的id
     * v为ViewGroup操作的view
     * msg 需要传递的text值
     */
    fun onViewClick(v: View?, parentId: Int,value : String?){

    }
}