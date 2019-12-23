package com.example.frame.ui

import android.view.View
import com.example.frame.R
import com.example.frame.base.BaseActivity
import com.example.frame.base.expand.toast
import com.example.frame.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding,MainViewModel>() {

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initView() {

    }

    override fun loadData() {
        mViewModel.login(LoginRequest("88a86eb0ab986d3f","824f051f391fdc0bd3e5f5c376c249c4","17607185665"))
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.tv -> toast("点击了")
        }
    }

}
