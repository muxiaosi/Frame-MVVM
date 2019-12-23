package com.example.frame.http

import okhttp3.CookieJar
import okhttp3.Interceptor
import okhttp3.OkHttpClient

/**
 * @author mxs on 2019/3/12
 * 配置网络参数
 */
interface NetProvider {

    /**
     * 公共请求头处理器
     */
    fun headerInterceptors(): RequestHandler

    /**
     * 除了公共请求头之后的参数
     */
    fun configInterceptors(): Array<Interceptor>?

    /**
     * 设置OkHttpClient
     */
    fun configHttps(builder: OkHttpClient.Builder)

    /**
     * 设置cookie
     */
    fun configCookie(): CookieJar?
    /**
     * 设置请求连接超时时间
     */
    fun configConnectTimeoutSecs(): Long

    /**
     * 设置请求读取超时时间
     */
    fun configReadTimeoutSecs(): Long

    /**
     * 设置请求写入超时时间
     */
    fun configWriteTimeoutSecs(): Long

    /**
     * 设置日志打印方式
     */
    fun configLogEnable(): Boolean

}