package shy.car.sdk.travel.user.ui

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import com.wq.photo.widget.PickConfig
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentUserDetailBinding
import shy.car.sdk.travel.user.data.User
import shy.car.sdk.travel.user.presenter.UserDetailPresenter


/**
 * create by LZ at 2018/06/05
 * 个人中心
 */
class UserDetailFragment : XTBaseFragment(),
        UserDetailPresenter.UploadListener {
    override fun onUploadSuccess() {

    }

    lateinit var binding: FragmentUserDetailBinding
    lateinit var presenter: UserDetailPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            presenter = UserDetailPresenter(it)
            presenter.listener = this
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_detail, null, false)
        binding.user = User.instance
        binding.fragment = this
        return binding.root

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
        activity?.let {
            PickConfig.with(it)
                    .pickMode(PickConfig.MODE_SINGLE_PICK)
                    .isneedcamera(true)
                    .isneedactionbar(true)
                    .isneedcrop(true)
                    .start()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == PickConfig.PICK_REQUEST_CODE) {
            val imgs = data!!.getStringArrayListExtra(PickConfig.DATA)
            if (imgs != null && imgs.size > 0) {
                presenter.uploadAvatar(imgs[0])
            }
        }
    }
}