package com.example.frame.utils

import java.lang.reflect.ParameterizedType

/**
 * @author mxs
 * @date 2019-12-20 15:56
 * @description class工具类
 */
class ClassUtils {

    companion object {

        /**
         * 获取类传递的泛型
         * @param index 第几个泛型
         */
        fun <T> getGenericityType(any: Any, index: Int): T? {
            val genericSuperclass = any.javaClass.genericSuperclass
            try {
                 if (genericSuperclass is ParameterizedType) {
                     return (genericSuperclass.actualTypeArguments[index] as Class<*>).newInstance() as T?
                }
            } catch (var3: InstantiationException) {
                var3.printStackTrace()
            } catch (var4: IllegalAccessException) {
                var4.printStackTrace()
            } catch (var5: ClassCastException) {
                var5.printStackTrace()
            }
            return null
        }
    }
}