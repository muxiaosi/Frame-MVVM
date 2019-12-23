package com.example.frame.http

import android.text.TextUtils
import com.example.frame.App
import com.example.frame.base.BaseResult
import com.example.frame.base.expand.toast
import io.reactivex.observers.DisposableObserver

/**
 * @author mxs on 2019/3/13
 */
abstract class ApiCallback<T> : DisposableObserver<BaseResult<T>>() {

    override fun onComplete() {
        onFinish()
    }

    override fun onNext(t: BaseResult<T>) {
        when {
            t.success -> {
                onSuccess(t.data as T)
            }
            t.errorCode == ErrorCode.TOKEN_ERROR_RULE -> {
                //正对特殊code处理
            }
            else -> onFailure(t)
        }
    }

    override fun onError(e: Throwable) {
        val apiException = if (e is ApiException) {
            e
        } else {
            ApiException(
                ErrorCode.UNKNOWN, if (TextUtils.isEmpty(e.message)) {
                    "网络异常，请稍后再试"
                } else {
                    e.message
                }, e
            )
        }
        onNetError(apiException)
        onFinish()
    }

    open fun onNetError(apiException: ApiException) {
        App.getInstance().toast(apiException.msg!!)
    }

    open fun onFailure(t: BaseResult<T>) {
        App.getInstance().toast(t.errorMsg)
    }

    abstract fun onSuccess(data: T)

    open fun onFinish() {

    }
}