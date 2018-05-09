package shy.car.sdk.app.presenter


import android.content.Context
import android.databinding.BaseObservable
import com.base.util.Log
import org.greenrobot.eventbus.EventBus
import shy.car.sdk.app.Application


/**
 * Created by Syokora on 2016/8/20.
 * Presenter全部都已经register到EventBus了
 * 子类使用的时候请注意不要重复
 */
abstract class BasePresenter(protected var context: Context) : BaseObservable() {

    protected var app: Application = context.applicationContext as Application
    private var isPresenterDestroy: Boolean = false


    /**
     * 主要功能是将presenter注册到eventbus
     * 要在Activity/fragment中和onPause配对使用
     */
    fun onResume() {
        if (!EventBus.getDefault()
                        .isRegistered(this)) {
            try {
                EventBus.getDefault()
                        .register(this)
            } catch (e: Exception) {
                Log.e("BasePresenter", "its super classes have no public methods with the @Subscribe annotation")
            }

        }
    }

    /**
     * 主要功能是将presenter反注册到eventbus
     * 要和onResume配对使用
     * 要在Activity/fragment中和onResume配对使用
     */
    fun onPause() {
        if (EventBus.getDefault()
                        .isRegistered(this)) {
            EventBus.getDefault()
                    .unregister(this)
        }
    }

    /**
     * 强烈建议这个方法在每个用到presenter的Activity/Fragment的onDestroy中调用
     */
    fun destroy() {
        EventBus.getDefault()
                .unregister(this)
        isPresenterDestroy = true
    }

}
