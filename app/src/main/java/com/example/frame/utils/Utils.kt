package com.example.frame.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.text.method.DigitsKeyListener
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.example.frame.App
import com.example.frame.config.AppConstants
import java.io.File
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*


object Utils {
    fun showIme(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.RESULT_SHOWN)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    fun hideIme(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun hideImeByActivity(activity: Activity) {
        activity.runOnUiThread {
            val mInputKeyBoard = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (activity.currentFocus != null) {
                mInputKeyBoard.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
            }
        }
    }

    /**
     * 销毁系统键盘
     */
    fun hideImeByActivity2(activity: Activity) {
        activity.runOnUiThread {
            val mInputKeyBoard = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (activity.currentFocus != null) {
                mInputKeyBoard.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
            }
        }
    }

    /**
     * 获取设备id
     */
    @SuppressLint("HardwareIds")
    fun getUniqueId(context: Context): String {
        var androidId by Preference(AppConstants.DEVICE_ID, "")
        if (androidId.isEmpty()) {
            androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            if (TextUtils.isEmpty(androidId)) {
                androidId = getPhoneSign(context)
            }
        }
        return androidId
    }

    //获取手机的唯一标识
    @SuppressLint("MissingPermission", "HardwareIds")
    fun getPhoneSign(context: Context): String {
        val deviceId = StringBuilder()
        try {
            //IMEI（imei）
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val imei = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tm.imei
            } else {
                tm.deviceId
            }
            if (!TextUtils.isEmpty(imei)) {
                deviceId.append(imei)
                return deviceId.toString()
            }
            //序列号（sn）
            val sn = tm.simSerialNumber
            if (!TextUtils.isEmpty(sn)) {
                deviceId.append(sn)
                return deviceId.toString()
            }
            //如果上面都没有， 则生成一个id：随机码
            val uuid = getUUID(context)
            if (!TextUtils.isEmpty(uuid)) {
                deviceId.append(uuid)
                return deviceId.toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            deviceId.append(getUUID(context))
        }

        return deviceId.toString()
    }

    /**
     * 得到全局唯一UUID
     */
    private var uuid: String? = null

    private fun getUUID(context: Context): String? {
        val mShare = context.getSharedPreferences("uuid", MODE_PRIVATE)
        if (mShare != null) {
            uuid = mShare.getString("uuid", "")
        }
        if (TextUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString()
            mShare!!.edit().putString("uuid", uuid).apply()
        }
        return uuid
    }

    fun getIPAddress(context: Context = App.getInstance()): String? {
        val info = (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        if (info != null && info.isConnected) {
            if (info.type == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    val en = NetworkInterface.getNetworkInterfaces()
                    while (en.hasMoreElements()) {
                        val intf = en.nextElement()
                        val enumIpAddr = intf.inetAddresses
                        while (enumIpAddr.hasMoreElements()) {
                            val inetAddress = enumIpAddr.nextElement()
                            if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                                return inetAddress.getHostAddress()
                            }
                        }
                    }
                } catch (e: SocketException) {
                    e.printStackTrace()
                }

            } else if (info.type == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val wifiInfo = wifiManager.connectionInfo
                return intIP2StringIP(wifiInfo.ipAddress)
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    fun intIP2StringIP(ip: Int): String {
        return (ip and 0xFF).toString() + "." +
                (ip shr 8 and 0xFF) + "." +
                (ip shr 16 and 0xFF) + "." +
                (ip shr 24 and 0xFF)
    }

    /**
     * 获取uid
     */
    fun getUserId(): String {
        return Preference.preference.getString(AppConstants.USER_ID, "")
    }


    /**
     * 获取登录状态
     */
    fun getIsLogin(): Boolean {
        return Preference.preference.getBoolean(AppConstants.IS_LOGIN, false)
    }


    /**
     * 图片压缩保存路径
     */
    fun getPath(): String {
        val path = Environment.getExternalStorageDirectory().toString() + "/Luban/image/"
        val file = File(path)
        return if (file.mkdirs()) {
            path
        } else path
    }

    /**
     * 限制输入数字或字母
     */
    fun setKeyListener() = DigitsKeyListener.getInstance("0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM")




}