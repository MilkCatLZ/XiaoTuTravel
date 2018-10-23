package shy.car.sdk.travel.pay.ui

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import com.google.gson.JsonObject
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentPromiseMoneyReturnBinding
import shy.car.sdk.travel.interfaces.CommonCallBack
import shy.car.sdk.travel.pay.presenter.PromiseReturnPresenter
import shy.car.sdk.travel.user.data.User

class PromiseMoneyReturnFragment : XTBaseFragment(),
        CommonCallBack<JsonObject> {
    override fun onSuccess(t: JsonObject) {
        ARouter.getInstance()
                .build(RouteMap.ReturnPromiseMoneySuccess)
                .navigation()
        finish()
    }

    override fun onError(e: Throwable) {

    }

    lateinit var binding: FragmentPromiseMoneyReturnBinding
    lateinit var presenter: PromiseReturnPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            presenter = PromiseReturnPresenter(it, this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_promise_money_return, null, false)
        binding.fragment = this
        binding.user = User.instance
        return binding.root

    }

    fun promiseMoneyReturn() {
        presenter.promiseMoneyReturn()
    }
}