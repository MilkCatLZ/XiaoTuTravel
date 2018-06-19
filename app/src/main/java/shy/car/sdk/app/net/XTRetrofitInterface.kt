package shy.car.sdk.app.net

import com.base.network.retrofit.BaseRetrofitInterface
import com.base.util.ToastManager
import org.greenrobot.eventbus.EventBus
import org.json.JSONException
import retrofit2.HttpException
import shy.car.sdk.R
import shy.car.sdk.app.Application
import shy.car.sdk.app.data.LoginOutOfDateException
import shy.car.sdk.travel.user.data.User
import java.net.SocketException
import java.net.SocketTimeoutException

class XTRetrofitInterface(var app: Application) : BaseRetrofitInterface {
    override fun onError(ex: Throwable) {
        if (ex is HttpException) {
            val httpException = ex as HttpException
            //登录超时，需要重新登录，或者token失效都要重新登录
            if (httpException.code() == 401) {
                if (User.instance
                                .login) {
                    User.logout(app)
                    EventBus.getDefault()
                            .post(LoginOutOfDateException())
                    ToastManager.showShortToast(app, R.string.str_login_out_date)
                }
            } else if (httpException.code() == 502) {
                ToastManager.showShortToast(app, R.string.str_network_error)
            }
        } else if (ex is SocketException || ex is SocketTimeoutException) {
            ToastManager.showShortToast(app, R.string.str_network_error)
        } else if (ex is JSONException) {
            ToastManager.showShortToast(app, R.string.str_data_parse_error)
        }
        ex.printStackTrace()
    }
}