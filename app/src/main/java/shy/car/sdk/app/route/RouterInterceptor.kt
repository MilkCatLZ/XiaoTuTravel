package shy.car.sdk.app.route

import android.content.Context
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.base.util.ToastManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import shy.car.sdk.BuildConfig
import shy.car.sdk.app.Application
import shy.car.sdk.travel.user.data.User

@Interceptor(priority = 1, name = "拦截登录状态")
open class RouterInterceptor : IInterceptor {
    lateinit var app: Application

    override fun process(postcard: Postcard, callback: InterceptorCallback) {
        // 处理完成，交还控制权
        if (User.instance.isLogin) {
            callback.onContinue(postcard)
        } else {
            if(BuildConfig.DEBUG){
                callback.onContinue(postcard)
            }else {
                Observable.just("").observeOn(AndroidSchedulers.mainThread()).subscribe({ ToastManager.showShortToast(app, "请先登录...") })
                app.startLoginDialog(postcard, callback)
            }
        }
        // 觉得有问题，中断路由流程
        // callback.onInterrupt(new RuntimeException("我觉得有点异常"));

        // 以上两种至少需要调用其中一种，否则不会继续路由
    }

    override fun init(context: Context?) {
        app = context as Application
    }

}