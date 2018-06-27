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
import shy.car.sdk.databinding.FragmentUserDetailBinding
import shy.car.sdk.travel.user.data.RefreshUserInfo
import shy.car.sdk.travel.user.data.User


/**
 * create by LZ at 2018/06/05
 * 个人中心
 */
class UserDetailFragment : XTBaseFragment() {

    lateinit var binding: FragmentUserDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_detail, null, false)
        binding.user = User.instance
        binding.fragment = this
        return binding.root

    }

    override fun onResume() {
        super.onResume()
        eventBusDefault.post(RefreshUserInfo())
    }

    fun goVipHomeClick() {

    }

    fun goUserVerifyClick() {
        ARouter.getInstance()
                .build(RouteMap.UserVerify)
                .navigation()
    }

    fun goMoneyVerifyClick() {
        ARouter.getInstance()
                .build(RouteMap.MoneyVerify)
                .navigation()
    }

    fun onAvatarClick() {
        ARouter.getInstance()
                .build(RouteMap.UserInfoEdit)
                .navigation()
    }

}