package com.example.frame.widge;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;
import com.example.frame.R;

/**
 * @author mxs on 2019-06-21
 * 自定义toast
 */
public class CustomToast extends Toast {

    /**
     * 上下文对象
     */
    private static Context mContext;
    /**
     * Toast对象
     */
    private static CustomToast mToast;
    /**
     * Toast中显示的文本
     */
    private static TextView mView;

    public synchronized static void getInstance(Context context) {
        if (mToast == null) {
            mContext = context;
            mToast = new CustomToast(context);
        }
    }

    /**
     * 单例模式
     * @param context 全局上下文
     */
    private CustomToast(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        mView = new TextView(mContext);
        mView.setBackgroundResource(R.drawable.bg_toast);
        //设置最大宽度
        mView.setMaxWidth((int)(displayMetrics.widthPixels - displayMetrics.density * 100));
        //设置内边距
        mView.setPadding(30,15,30,15);
        //设置对其方式
        mView.setGravity(Gravity.CENTER);
        //设置字体颜色
        mView.setTextColor(ContextCompat.getColor(mContext,R.color.color_white_fff));
        //设置新展示样式
        setView(mView);

    }

    public static void show(String message){
        show(message, Toast.LENGTH_LONG);
    }

    /**
     * 显示toast
     * @param messageId 文本string资源
     * @param duration 显示的时长
     */
    public static void show(int messageId, int duration){
        if(mView == null){
            throw new NullPointerException("CustomToast is Null");
        }
        mView.setText(messageId);
        mToast.setDuration(duration);
        mToast.setGravity(Gravity.CENTER,0,0);
        mToast.show();
    }

    /**
     * 显示toast
     * @param message  中文文本
     * @param duration 显示的时长
     */
    public static void show(String message, int duration){
        if(mView == null){
            throw new NullPointerException("CustomToast is Null");
        }
        mView.setText(message);
        mToast.setDuration(duration);
        mToast.setGravity(Gravity.CENTER,0,0);
        mToast.show();
    }
}
