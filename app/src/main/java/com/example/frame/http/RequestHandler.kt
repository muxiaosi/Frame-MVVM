package com.example.frame.http

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * @author mxs on 2019/3/12
 * 网络拦截处理
 */
interface RequestHandler {

    /**
     * 处理请求参数
     */
    fun onBeforeRequest(request: Request, chain: Interceptor.Chain): Request

    /**
     * 处理相应参数
     */
    @Throws(IOException::class)
    fun onAfterRequest(response: Response, chain: Interceptor.Chain): Response
}