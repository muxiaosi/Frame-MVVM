package com.example.frame.widge

import android.content.Context
import android.view.Gravity
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.example.frame.R
import kotlinx.android.synthetic.main.common_loading.*

/**
 * @author zhangsicong
 * @date 2018/12/10 0010
 */
class CommonLoadingDialog(context: Context) : BaseDialog(context) {

    private var mContext: Context? = null

    init {
        this.mContext = context
        initView()
    }

    private fun initView() {
        window?.let {
            it.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            setGravity(Gravity.CENTER)
            setContentView(R.layout.common_loading)
            mContext?.let { context ->
                Glide.with(context)
                    .load(R.mipmap.loading_pic)
                    .into(loading_pic)
            }
            setCanceledOnTouchOutside(false)
        }
    }

}