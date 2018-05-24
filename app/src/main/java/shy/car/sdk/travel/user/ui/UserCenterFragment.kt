package shy.car.sdk.travel.user.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.LayoutHomeUserCenterBinding
import shy.car.sdk.travel.user.data.User

class UserCenterFragment : XTBaseFragment() {
    lateinit var binding: LayoutHomeUserCenterBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_home_user_center, null, false)
        binding.user = User.instance
        binding.ac = this
        return binding.root
    }

    fun onUserPicClick() {
        ARouter.getInstance()
                .build(RouteMap.UserDetail)
                .navigation()
        app.startLoginDialog(null, null)
    }


    fun onWalletClick() {
        ARouter.getInstance()
                .build(RouteMap.Wallet)
                .navigation()
    }

    fun onSettingClick() {
        ARouter.getInstance()
                .build(RouteMap.Setting)
                .navigation()
    }

    fun onKeFuClick() {
        ARouter.getInstance()
                .build(RouteMap.KeFu)
                .navigation()
    }

    fun onOrderClick() {
        ARouter.getInstance()
                .build(RouteMap.OrderMine)
                .navigation()
    }
}