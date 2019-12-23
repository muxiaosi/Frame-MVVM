package com.example.frame.http

import android.content.Context
import com.example.frame.BuildConfig
import okhttp3.CookieJar
import okhttp3.Interceptor
import okhttp3.OkHttpClient

/**
 * @author mxs on 2019/3/12
 * 公共网络配置参数
 */
class CommonNetProvider(private val mContext: Context) : NetProvider {

    companion object {
         const val CONNECT_TIME_OUT: Long = 10
         const val READ_TIME_OUT: Long = 30
         const val WRITE_TIME_OUT: Long = 30
    }

    override fun headerInterceptors(): RequestHandler = CommonRequestHandler()

    override fun configInterceptors(): Array<Interceptor>? {
        return null
    }

    override fun configHttps(builder: OkHttpClient.Builder) {

    }

    override fun configCookie(): CookieJar? {
        return null
    }

    override fun configConnectTimeoutSecs(): Long = CONNECT_TIME_OUT

    override fun configReadTimeoutSecs(): Long = READ_TIME_OUT

    override fun configWriteTimeoutSecs(): Long = WRITE_TIME_OUT

    override fun configLogEnable(): Boolean {
        return BuildConfig.DEBUG
    }

}