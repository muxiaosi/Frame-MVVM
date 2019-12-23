package com.example.frame.base

/**
 * @author sxs
 * @date 2019/1/2
 */
open class BaseResult<T> {
    val errorCode: String = ""
    val errorMsg: String = ""
    val success: Boolean = false
    var data: T? = null
}