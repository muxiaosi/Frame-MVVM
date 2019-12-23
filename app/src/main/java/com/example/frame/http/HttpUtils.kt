package com.example.frame.http

import com.example.frame.App
import com.example.frame.BuildConfig
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author mxs on 2019/3/12
 */
object HttpUtils {

    private const val connectTimeoutMills = 10 * 1000L
    private const val readTimeoutMills = 30 * 1000L

    private val mProviderMap = HashMap<String, NetProvider>()
    private val mRetrofitMap = HashMap<String, Retrofit>()
    private val mClientMap = HashMap<String, OkHttpClient>()

    private val mCommonNetProvider by lazy { CommonNetProvider(App.getInstance()) }

    private fun getRetrofit(baseUrl: String, provider: NetProvider? = null): Retrofit {
        if (empty(baseUrl)) {
            throw IllegalAccessException("baseUrl can not be null")
        }
        if (mRetrofitMap[baseUrl] != null) {
            return mRetrofitMap[baseUrl]!!
        }
        /**
         * 设置网络请求参数
         */
        var curProvider = provider
        if (curProvider == null) {
            curProvider = mProviderMap[baseUrl]
            if (curProvider == null) {
                curProvider = mCommonNetProvider
            }
        }

        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getOkHttpClient(baseUrl, curProvider))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .build()
        mRetrofitMap[baseUrl] = retrofit
        mProviderMap[baseUrl] = curProvider
        return retrofit
    }

    private fun getOkHttpClient(baseUrl: String, provider: NetProvider): OkHttpClient {
        if (empty(baseUrl)) {
            throw IllegalStateException("baseUrl can not be null")
        }
        if (mClientMap[baseUrl] != null) {
            return mClientMap[baseUrl]!!
        }
        val builder = OkHttpClient.Builder()
                .connectTimeout(if (provider.configConnectTimeoutSecs() != 0L) {
                    provider.configConnectTimeoutSecs()
                } else {
                    connectTimeoutMills
                }, TimeUnit.SECONDS)
                .readTimeout(if (provider.configReadTimeoutSecs() != 0L) {
                    provider.configReadTimeoutSecs()
                } else {
                    readTimeoutMills
                }, TimeUnit.SECONDS)
                .writeTimeout(if (provider.configWriteTimeoutSecs() != 0L) {
                    provider.configWriteTimeoutSecs()
                } else {
                    readTimeoutMills
                }, TimeUnit.SECONDS)

        // Debug模式下引入Stetho Http Interceptor
        // 可以在chrome://inspect -> Network中直接查看Http(s)请求和返回内容
        // 免代理、https免安装证书
        if (BuildConfig.DEBUG) {
            builder.addNetworkInterceptor(StethoInterceptor())
        }

        val handler = provider.headerInterceptors()
        builder.addInterceptor(NetInterceptor(handler))

        val interceptors = provider.configInterceptors()
        if (!empty(interceptors)) {
            for (interceptor in interceptors!!) {
                builder.addInterceptor(interceptor)
            }
        }
        if (provider.configLogEnable()) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
            builder.addInterceptor(loggingInterceptor)
        }

        val client = builder.build()
        mClientMap[baseUrl] = client
        mProviderMap[baseUrl] = provider
        return client
    }

    private fun empty(baseUrl: String?): Boolean {
        return baseUrl == null || baseUrl.isEmpty()
    }

    private fun empty(interceptors: Array<Interceptor>?): Boolean {
        return interceptors == null || interceptors.isEmpty()
    }

    operator fun <S> get(baseUrl: String, service: Class<S>): S {
        return getRetrofit(baseUrl).create(service)
    }
}