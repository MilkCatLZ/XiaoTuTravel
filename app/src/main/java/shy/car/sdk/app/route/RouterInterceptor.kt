package shy.car.sdk.app.route

import android.content.Context
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import shy.car.sdk.app.Application
import shy.car.sdk.travel.user.data.User

@Interceptor(priority = 1, name = "拦截登录状态")
class RouterInterceptor : IInterceptor {
    lateinit var app: Application

    override fun process(postcard: Postcard, callback: InterceptorCallback) {
        // 处理完成，交还控制权
        if (User.instance.isLogin) {
            callback.onContinue(postcard)
        } else {
            app.startLoginDialog()
        }
        // 觉得有问题，中断路由流程
        // callback.onInterrupt(new RuntimeException("我觉得有点异常"));

        // 以上两种至少需要调用其中一种，否则不会继续路由
    }

    override fun init(context: Context?) {
        app = context as Application
    }

}