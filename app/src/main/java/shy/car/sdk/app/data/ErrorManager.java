package shy.car.sdk.app.data;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.base.util.JsonManager;
import com.base.util.Log;
import com.base.util.StringUtils;
import com.base.util.ToastManager;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.HttpException;


/**
 * Created by Syokora on 2016/8/20.
 * 在网络回调onError中，统一调用，显示错误信息
 * ErrorManager.managerError(context, ex, 默认提示语);
 */
public class ErrorManager {
    public void showError(Context context, String defaultMessage) {
        if (StringUtils.isEmpty(error_message)) {
            ToastManager.showShortToast(context, defaultMessage);
        } else {
            ToastManager.showShortToast(context, error_message);
        }
    }

    public class OrderCache {
        String id;
        int type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    /**
     * type : ShangYou\Sms\Exception
     * message : 短信发送失败：用户名或者密码不正确
     * file : /home/www/car/app/library/Sms/Adapter/Samton.php
     * line : 88
     */

    private String type;
    private String message;
    private String error_message;
    private int error_code;
    private String file;
    private String line;
    private int httpCode;
    private OrderCache order;

    public OrderCache getOrder() {
        return order;
    }

    public void setOrder(OrderCache order) {
        this.order = order;
    }

    /**
     * 逻辑为：
     * 1、如果有返回的错误有Result，就将其解析，展示error信息
     * 2、如果error为空，则展示默认信息
     *
     * @param context        上下文
     * @param ex             错误内容
     * @param defaultMessage 默认的出错提示语
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
                String body = httpException.response().errorBody().string();

                String result = JsonManager.getJsonString(body, "error");
                if (StringUtils.isEmpty(result)) {
                    result = body;
                }


                err = new Gson().fromJson(result, ErrorManager.class);
            } catch (Exception e) {

            }


            if (err != null) {
                err.setHttpCode(httpException.code());
                if (StringUtils.isNotEmpty(defaultMessage)) {
                    {
                        if (StringUtils.isNotEmpty(err.getError())) {
                            ToastManager.showShortToast(context, err.getError());
                        } else {
                            if (StringUtils.isNotEmpty(defaultMessage)) {
                                ToastManager.showShortToast(context, defaultMessage);
                                handleException(context, ex);
                                Log.e("ManagerError--------", "error message is empty\n", ex);
                            }
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return StringUtils.isEmpty(message) ? error_message : message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }


    static String CrashDir = "/XTCrashFile/";
    static Map<String, String> infos = new HashMap<String, String>();

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private static boolean handleException(final Context mContext, Throwable ex) {
        String className = "";
        try {
            className = ex.getStackTrace()[0].getClassName();
        } catch (Exception e) {

        }
        final String fileName = "接口错误日志-" + getNowDate() + "-" + className + ".log";
        if (ex == null) {
            return false;
        }
        // 收集设备参数信息
        collectDeviceInfo(mContext);
        // 保存日志文件
        ex.printStackTrace();
        saveCrashInfo2File(ex, fileName);
        return true;
    }

    public static String getNowDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.CHINA);
        String dateString = formatter.format(currentTime);
        // ParsePosition pos = new ParsePosition(8);
        // Date currentTime_2 = formatter.parse(dateString, pos);
        return dateString;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    static void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null)
                        .toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex
     * @return 返回错误信息字符串，传给服务器
     */
    static String saveCrashInfo2File(Throwable ex, String fileName) {

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {

            // long timestamp = System.currentTimeMillis();
            // String time = getNowDate();

            if (Environment.getExternalStorageState()
                    .equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory()
                        .getPath() + "/" + CrashDir;
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + "/" + fileName);
                fos.write(sb.toString()
                        .getBytes());
                fos.close();
            }
            return result;
        } catch (Exception e) {
            Log.e("ErrorManager-------", "an error occured while writing file...", e);
            e.printStackTrace();

        }
        return result;
    }

    public static final String TAG = "ErrorManager-------";
}
