package com.example.frame.http

import android.text.TextUtils
import com.alibaba.fastjson.JSON
import okhttp3.Interceptor
import okhttp3.Response
import java.net.URLDecoder

/**
 * @author mxs on 2019/3/12
 */
class NetInterceptor(private val handler: RequestHandler?) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (handler != null) {
            //处理请求头
            request = handler.onBeforeRequest(request, chain)
        }

        try {
            val response = chain.proceed(request)
            if (handler != null) {
                //处理结果
                return handler.onAfterRequest(response, chain)
            }
            return response
        } catch (e: Exception) {
            //用于网络请求解析异常  上报服务器
            val method = request.header("method")
            val content = request.header("content")
            val bean = if (TextUtils.isEmpty(content)) {
                NetErrorBean(method, e, null)
            } else {
                NetErrorBean(method, e, URLDecoder.decode(content, "utf-8"))
            }
            val errorMsg = JSON.toJSONString(bean)
            throw e
        }
    }

}