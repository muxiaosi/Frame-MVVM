package com.example.frame.config

import com.example.frame.BuildConfig

/**
 * @author mxs
 * @date 2019-12-20 15:12
 * @description 框架常量
 */
object AppConstants {

    /**
     * 正式服务器域名
     */
    private val HOSTS = arrayOf(
        "https://crmgw-test.51youdian.com/gateway/",
        "https://cxxxxxxxxxxxxxxxxxxxx.com/gateway/",
        "https://cxxxxxxxxxxxxxxxxxxxx.com/gateway/"
    )

    /**
     * 0.测试环境, 1.预发布环境, 2.正式环境
     */
    val HOST = HOSTS[BuildConfig.SERVER]

    /**
     * 网络请求签名key
     */
    private val KEYS = arrayOf(
        "88888888",
        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
    )

    /**
     * BaseRequest里面加签秘钥
     */
    val KEY = KEYS[BuildConfig.SERVER]

    /**
     * 绩效h5域名
     */
    private val HTML_HOSTS = arrayOf(
        "https://cxxxxxxxxxxxxxxxxxxxx.com",
        "https://cxxxxxxxxxxxxxxxxxxxx.com",
        "https://cxxxxxxxxxxxxxxxxxxxx.com"
    )

    /**
     * 绩效h5域名
     */
    val h5Host = HTML_HOSTS[BuildConfig.SERVER]

    /**
     * 登录账号
     */
    const val LOGIN_NAME = "loginName"
    /**
     * 设备ID
     */
    const val DEVICE_ID = "deviceID"
    /**
     * token
     */
    const val TOKEN = "token"
    /**
     * userId
     */
    const val USER_ID = "userId"

    const val IS_LOGIN = "isLogin"
}