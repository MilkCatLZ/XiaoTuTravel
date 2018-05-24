package shy.car.sdk.travel.user.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap

/**
 * create by lz at 2018/05/24
 * 提交验证用户信息
 */
@Route(path = RouteMap.VerifyUser)
class VerifyUserActivity : XTBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_user)
    }
}