package com.example.frame.ui

import android.arch.lifecycle.MutableLiveData
import com.example.frame.base.BaseViewModel
import com.example.frame.base.expand.dispatchNewDefault
import com.example.frame.http.ApiCallback

/**
 * @author mxs
 * @date 2019-12-23 10:14
 * @description
 */
class MainViewModel : BaseViewModel() {

    val mLoginBean: MutableLiveData<LoginBean> = MutableLiveData()

    fun login (request: LoginRequest){
        mHttp.login(request)
            .dispatchNewDefault()
            .doOnSubscribe { mShowLoading.value = true }
            .doAfterTerminate { mShowLoading.value = false }
            .subscribeWith(object : ApiCallback<LoginBean>() {
                override fun onSuccess(data: LoginBean) {

                    mLoginBean.value = data
                }
            }).addDispose()
    }
}