package shy.car.sdk.app.data;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.base.util.StringUtils;
import com.base.util.ToastManager;
import com.google.gson.Gson;

import org.xutils.ex.HttpException;


/**
 * Created by Syokora on 2016/8/20.
 * 在网络回调onError中，统一调用，显示错误信息
 * ErrorManager.managerError(context, ex, 默认提示语);
 */
public class ErrorManager {
    
    String code;
    String error;
    int httpCode;
    
    public int getHttpCode() {
        return httpCode;
    }
    
    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
    
    /**
     * 逻辑为：
     * 1、如果有返回的错误有Result，就将其解析，展示error信息
     * 2、如果error为空，则展示默认信息
     *
     * @param context        上下文
     * @param ex             错误内容
     * @param defaultMessage 默认的出错提示语
     *
     * @return Error的Model，带有code和error信息
     */
    @Nullable
    public static ErrorManager managerError(Context context, Throwable ex, @StringRes int defaultMessage) {
        if (context == null) {
            return null;
        }
        return managerError(context, ex, context.getString(defaultMessage));
    }
    
    /**
     * 这个需要手动调用
     *
     * @param context        上下文
     * @param ex             Xutil返回的错误信息
     * @param defaultMessage 传null的话就不显示任何提示信息
     *
     * @return ErrorManager
     */
    @Nullable
    public static ErrorManager managerError(@Nullable Context context, @NonNull Throwable ex, @Nullable String defaultMessage) {
        if (context == null) {
            return null;
        }
        ErrorManager err = null;
        if (ex instanceof HttpException) {
            HttpException httpException = (HttpException) ex;
            try {
                err = new Gson().fromJson(httpException.getResult(), ErrorManager.class);
            } catch (Exception e) {
            
            }
            if (err != null) {
                err.setHttpCode(httpException.getCode());
                if (StringUtils.isNotEmpty(defaultMessage)) {
                    {
                        if (StringUtils.isNotEmpty(err.getError())) {
                            ToastManager.showShortToast(context, err.getError());
                        } else {
                            if (StringUtils.isNotEmpty(defaultMessage))
                                ToastManager.showShortToast(context, defaultMessage);
                        }
                    }
                }
            }
        } else {
            if (StringUtils.isNotEmpty(defaultMessage))
                ToastManager.showShortToast(context, defaultMessage);
        }
        return err;
    }
}
