package shy.car.sdk.travel.update;


import android.content.Context;
import android.support.annotation.NonNull;

import com.base.update.update.BaseUpdateHelper;
import com.base.util.SPCache;
import com.google.gson.Gson;


/**
 * Created by LZ on 2017/3/20.
 * 更新
 */
public class UpdateHelper extends BaseUpdateHelper<UpdateInfo> {
    
    /**
     * @param context
     * @param logo         默认logo
     * @param isAutoUpdate 是否是自动更新
     * @param needMessage  是否需要显示更新信息
     */
    public UpdateHelper(@NonNull Context context, int logo, boolean isAutoUpdate, boolean needMessage) {
        super(context, logo, isAutoUpdate, needMessage);
    }
    
    
    @Override
    protected UpdateInfo getNewUpdateInfo(String result) {
        return new Gson().fromJson(result, UpdateInfo.class);
    }
    
    @Override
    protected UpdateInfo getTodayInfo(String nowString) {
        return SPCache.getObject(context, nowString, UpdateInfo.class);
    }
    
    /**
     * @param nowVersion
     * @param newVersion
     *
     * @return true:有新版本，false：没有新版本
     */
    @Override
    protected boolean checkHasNewVersion(String nowVersion, String newVersion) {
        String[] nowV = nowVersion.split("\\.");
        String[] newV = newVersion.split("\\.");
        if (Integer.parseInt(nowV[0]) < Integer.parseInt(newV[0])) {
            return true;
        } else if (Integer.parseInt(nowV[1]) < Integer.parseInt(newV[1])) {
            return true;
        } else if (Integer.parseInt(nowV[2]) < Integer.parseInt(newV[2])) {
            return true;
        }
        return false;
    }
    
    @Override
    protected String getAlreadyNewMessage() {
        return "当前已是最新版本";
    }
    
    @Override
    protected String getAppName() {
        return "连你配送-";
    }
}
