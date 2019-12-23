package com.example.frame.http

import android.text.TextUtils
import com.example.frame.App
import com.example.frame.BuildConfig
import com.example.frame.config.AppConstants
import com.example.frame.utils.*
import com.google.gson.GsonBuilder
import okhttp3.*
import okio.Buffer
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URLEncoder
import java.nio.charset.Charset

/**
 * @author mxs on 2019/3/12
 */
class CommonRequestHandler : RequestHandler {

    private var token by Preference(AppConstants.TOKEN, "")

    override fun onBeforeRequest(request: Request, chain: Interceptor.Chain): Request {
        val newRequest = if (TextUtils.equals("POST", request.method())) {
            rebuildPostRequest(request)
        } else {
            request
        }
        return newRequest!!
    }

    private fun rebuildPostRequest(request: Request): Request? {
        val oldRequestBody = request.body()
        try {
            val baseRequest = BaseRequest()
            baseRequest.content = bodyToString(oldRequestBody!!)
            baseRequest.deviceId = Utils.getUniqueId(App.getInstance())
            //拦截url获取Method
            baseRequest.method =
                request.url().toString().replace(AppConstants.HOST,"")
            baseRequest.version = "1.0"
            if (token.isNotEmpty()) {
                baseRequest.token = token
            }
            var beforeStr = GsonBuilder().disableHtmlEscaping().create().toJson(baseRequest)
            beforeStr = beforeStr.replace("\\", "")
            val builder = FormBody.Builder()
            //处理后端定义的参数
//            builder.add("token", token)
//            builder.add("deviceId", baseRequest.deviceId)
//            builder.add("method", baseRequest.method)
//            builder.add("content", baseRequest.content)
            LogUtils.e("okhttp接口method:\t${baseRequest.method} \n请求参数content:\t${baseRequest.content}")
            return request.newBuilder()
                .addHeader("method", baseRequest.method)
                .addHeader("content", URLEncoder.encode(baseRequest.content, "utf-8"))
                .url(HttpUrl.parse(AppConstants.HOST)!!)
                .post(builder.build())
                .build()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return request

    }

    override fun onAfterRequest(response: Response, chain: Interceptor.Chain): Response {
        var responseBody: ResponseBody? = null
        var jsonReader: InputStreamReader? = null
        var reader: BufferedReader? = null
        try {
            val charset = Charset.forName("UTF-8")
            responseBody = response.peekBody(java.lang.Long.MAX_VALUE)
            jsonReader = InputStreamReader(responseBody.byteStream(), charset)
            reader = BufferedReader(jsonReader)
            val sbJson = StringBuilder()
            var line: String? = reader.readLine()
            do {
                sbJson.append(line)
                line = reader.readLine()
            } while (line != null)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            reader?.close()
            jsonReader?.close()
            responseBody?.close()
        }
        return response
    }

    /**
     * 获取常规post请求参数
     *
     * @param body
     */
    private fun bodyToString(body: RequestBody): String {
        val buffer = Buffer()
        try {
            body.writeTo(buffer)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return buffer.readUtf8()
    }
}