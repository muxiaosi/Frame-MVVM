package com.example.frame.base

/**
 * @author mxs on 2019/3/13
 */
interface BaseView {

    /**
     * 展示加载过渡页
     */
    fun showLoadingLayout()

    /**
     * 隐藏加载过渡页
     */
    fun hideLoadingLayout()

    /**
     * 现在空页面
     */
    fun showEmptyLayout(isShow: Boolean)

    /**
     * 显示网络请求loading
     */
    fun showLoading()

    /**
     * 隐藏网络请求loading
     */
    fun hideLoading()

    /**
     * 网络请求错误页面
     */
    fun showNetErrorLayout(isShow: Boolean)
}