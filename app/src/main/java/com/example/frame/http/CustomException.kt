package com.example.frame.http

import android.net.ParseException
import android.nfc.FormatException
import android.text.TextUtils
import com.example.frame.http.Error.Companion.ACCESS_DENIED
import com.example.frame.http.Error.Companion.BAD_GATEWAY
import com.example.frame.http.Error.Companion.BINKCARD_ERROR
import com.example.frame.http.Error.Companion.FORBIDDEN
import com.example.frame.http.Error.Companion.FORMAT_ERROR
import com.example.frame.http.Error.Companion.GATEWAY_TIMEOUT
import com.example.frame.http.Error.Companion.HANDEL_ERROR
import com.example.frame.http.Error.Companion.INTERNAL_SERVER_ERROR
import com.example.frame.http.Error.Companion.NETWORK_ERROR
import com.example.frame.http.Error.Companion.NOT_FOUND
import com.example.frame.http.Error.Companion.NULL
import com.example.frame.http.Error.Companion.PARSE_ERROR_OLD
import com.example.frame.http.Error.Companion.REQUEST_TIMEOUT
import com.example.frame.http.Error.Companion.SERVICE_UNAVAILABLE
import com.example.frame.http.Error.Companion.SSL_ERROR
import com.example.frame.http.Error.Companion.SSL_NOT_FOUND
import com.example.frame.http.Error.Companion.TIMEOUT_ERROR
import com.example.frame.http.Error.Companion.UNAUTHORIZED
import com.example.frame.http.Error.Companion.UNKNOWN
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException
import javax.net.ssl.SSLPeerUnverifiedException

/**
 * 本地错误，排除服务端因素
 */
class CustomException {
    companion object {

        fun handleException(e: Throwable): ApiException {
            if (e is HttpException) {
                val ex = ApiException(e.code(), e.message(), e)
                when (ex.code) {
                    UNAUTHORIZED -> ex.msg("未授权的请求")
                    FORBIDDEN -> ex.msg("禁止访问")
                    NOT_FOUND -> ex.msg("服务器地址未找到")
                    REQUEST_TIMEOUT -> ex.msg("请求超时")
                    GATEWAY_TIMEOUT -> ex.msg("网关响应超时")
                    INTERNAL_SERVER_ERROR -> {
                        ex.msg("服务器出错")
                    }
                    BAD_GATEWAY -> ex.msg("无效的请求")
                    SERVICE_UNAVAILABLE -> ex.msg("服务器不可用")
                    ACCESS_DENIED -> ex.msg("网络错误")
                    HANDEL_ERROR -> ex.msg("接口处理失败")
                    BINKCARD_ERROR -> ex.msg("该卡已被绑定11111")

                    else -> {
                        if (TextUtils.isEmpty(ex.msg)) {
                            ex.msg(e.message!!)
                        }

                        if (TextUtils.isEmpty(ex.msg) && e.getLocalizedMessage() != null) {
                            ex.msg(e.getLocalizedMessage())
                        }
                        if (TextUtils.isEmpty(ex.msg)) {
                            ex.msg("未知错误")
                        }
                    }
                }
                return ex
            } else if (e is JSONException || e is ParseException) {
                val ex = ApiException(PARSE_ERROR_OLD, e.message, e)
                ex.msg("解析错误")
                return ex
            } else if (e is ConnectException) {
                val ex = ApiException(NETWORK_ERROR, e.message, e)
                ex.msg("连接失败")
                return ex
            } else if (e is javax.net.ssl.SSLHandshakeException) {
                val ex = ApiException(SSL_ERROR, e.message, e)
                ex.msg("证书验证失败")
                return ex
            } else if (e is java.security.cert.CertPathValidatorException) {
                val ex = ApiException(SSL_NOT_FOUND, e.message, e)
                ex.msg("证书路径没找到")

                return ex
            } else if (e is SSLPeerUnverifiedException) {
                val ex = ApiException(SSL_NOT_FOUND, e.message, e)
                ex.msg("无有效的SSL证书")
                return ex

            } else if (e is ConnectTimeoutException) {
                val ex = ApiException(TIMEOUT_ERROR, e.message, e)
                ex.msg("连接超时")
                return ex
            } else if (e is java.net.SocketTimeoutException) {
                val ex = ApiException(TIMEOUT_ERROR, e.message, e)
                ex.msg("连接超时")
                return ex
            } else if (e is java.lang.ClassCastException) {
                val ex = ApiException(FORMAT_ERROR, e.message, e)
                ex.msg("类型转换出错")
                return ex
            } else if (e is NullPointerException) {
                val ex = ApiException(NULL, e.message, e)
                ex.msg("数据有空")
                return ex
            } else if (e is FormatException) {

                val ex = ApiException(-200, e.message, e)
                ex.msg("服务端返回数据格式异常")
                return ex
            } else if (e is UnknownHostException) {
                val ex = ApiException(NOT_FOUND, e.message, e)
                ex.msg("服务器地址未找到,请检查网络")
                return ex
            } else {
                val ex = ApiException(UNKNOWN, e.message, e)
                ex.msg("未知异常")
                return ex
            }
        }

    }
}