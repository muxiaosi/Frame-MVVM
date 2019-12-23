package com.example.frame.http

import com.example.frame.base.BaseResult
import com.example.frame.ui.LoginBean
import com.example.frame.ui.LoginRequest
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @author mxs
 * @date 2019-06-27 10:25
 * @description 网络请求接口地址
 */
interface ApiService {

    @POST("fshows.market.api.user.login")
    fun login(@Body request: LoginRequest): Observable<BaseResult<LoginBean>>

}