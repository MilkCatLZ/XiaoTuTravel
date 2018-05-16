package shy.car.sdk.travel.setting.ui;


import android.os.Bundle;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import shy.car.sdk.app.base.XTBaseActivity;
import shy.car.sdk.travel.update.UpdateHelper;


/**
 * Created by LZ on 2016/8/17.
 * 取消下载
 */
public class CancelActivity extends XTBaseActivity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getEventBusDefault().register(this);
    }
    
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void cancelUpdate(UpdateHelper updateHelper) {
        if (updateHelper != null) {
            updateHelper.cancelDownload();
//            updateHelper.clearCacheFile();
        }
        finish();
    }
    
    @Override
    protected void onDestroy() {
        getEventBusDefault().removeStickyEvent(UpdateHelper.class);
        super.onDestroy();
    }
}
