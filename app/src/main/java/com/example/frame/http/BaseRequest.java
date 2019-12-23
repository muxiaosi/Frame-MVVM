package com.example.frame.http;

import com.example.frame.config.AppConstants;


/**
 * 加签前对象
 *
 * @author sxs
 * @date 2018/12/25
 */
public class BaseRequest {
    private String appid = "CRM-APP";
    private String content;
    /**
     * 加签秘钥
     */
    private String key = AppConstants.INSTANCE.getKEY();
    private String method;
    private String token;
    private String version;
    private String deviceId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAppid() {
        return appid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
