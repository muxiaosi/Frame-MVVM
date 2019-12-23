package com.example.frame.utils

import android.app.Activity
import java.util.*

/**
 * @author mxs
 * @date 2019-12-23 09:54
 * @description 页面管理类
 */
class AppManager {

    companion object {

        private val activityStack = Stack<Activity>()

        /**
         * 添加Activity到堆栈
         */
        fun addActivity(activity: Activity) {
            activityStack.add(activity)
        }

        /**
         * 获取当前Activity（堆栈中最后一个压入的）
         */
        fun currentActivity(): Activity? {
            return if (activityStack.size > 0) {
                activityStack.lastElement()
            } else {
                null
            }
        }

        /**
         * 结束指定的Activity
         */
        fun finishActivity(activity: Activity) {
            activityStack.remove(activity)
            if (!activity.isFinishing) {
                activity.finish()
            }
        }

        /**
         * 结束指定类名的Activity
         */
        fun finishActivity(cls: Class<Any>) {
            for (activity in activityStack) {
                if (activity.javaClass == cls) {
                    finishActivity(activity)
                }
            }
        }

        /**
         * 结束所有Activity
         */
        fun finishAllActivity() {
            for (activity in activityStack) {
                activity.finish()
            }
            activityStack.clear()
        }

    }
}