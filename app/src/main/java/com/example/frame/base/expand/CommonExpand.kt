package com.example.frame.base.expand

import android.content.Context
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.widget.Toast
import com.example.frame.base.BaseResult
import com.example.frame.http.CustomException
import com.example.frame.widge.CustomToast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

/**
 * @author mxs
 * @date 2019-06-26 17:48
 * @description Kotlin 类拓展
 */

/**
 * toast 拓展类
 */
fun Context.toast(msg: String, duration: Int = Toast.LENGTH_LONG) {
    CustomToast.show(msg, duration)
}

/**
 * 在fragment中使用toast
 */
fun <T : Fragment> T.toast(msg: String, duration: Int = Toast.LENGTH_LONG) {
    CustomToast.show(msg, duration)
}

fun <T : Fragment> T.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_LONG) {
    CustomToast.show(getString(resId), duration)
}

fun <T> Observable<BaseResult<T>>.dispatchNewDefault(): Observable<BaseResult<T>> =
    this.subscribeOn(Schedulers.io())
        //出错统一处理
        .onErrorResumeNext(Function { throwable ->
            Observable.error(
                CustomException.handleException(
                    throwable
                )
            )
        })
        .observeOn(AndroidSchedulers.mainThread())

