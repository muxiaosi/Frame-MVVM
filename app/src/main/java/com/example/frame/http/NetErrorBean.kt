package com.example.frame.http

import java.lang.Exception

/**
 * @author mxs on 2019/4/28
 */
class NetErrorBean (
        val method : String?,
        val exception: Exception,
        val content : String?
)