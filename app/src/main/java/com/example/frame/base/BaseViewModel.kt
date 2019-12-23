package com.example.frame.base

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.frame.config.AppConstants
import com.example.frame.http.ApiService
import com.example.frame.http.HttpUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * @author mxs
 * @date 2019-06-26 15:51
 * @description
 */
open class BaseViewModel : ViewModel() {

    private val disposes = CompositeDisposable()

    protected val mHttp = HttpUtils[AppConstants.HOST, ApiService::class.java]

    /**
     * 是否第一次加载
     */
    var isFirst: Boolean = true

    /**
     * 控制是否显示加载中的视图
     * true：显示
     * false ：隐藏
     */
    val mShowLoadingLayout: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * 控制是否显示网络加载异常的视图
     * true：显示
     * false ：隐藏
     */
    val mShowNetErrorLayout: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * 控制是否显示空视图
     * true：显示
     * false ：隐藏
     */
    val mShowEmptyLayout: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * 控制是否显示加载loading
     * true：显示
     * false ：隐藏
     */
    val mShowLoading: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * 是否正在刷新
     */
    val isRefreshing: MutableLiveData<Boolean> = MutableLiveData()

    public override fun onCleared() {
        disposes.clear()
        super.onCleared()
    }

    protected fun Disposable.addDispose() {
        disposes.add(this)
    }
}
