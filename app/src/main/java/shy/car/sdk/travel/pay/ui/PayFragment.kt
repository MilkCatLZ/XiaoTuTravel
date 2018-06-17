package shy.car.sdk.travel.pay.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.constant.ParamsConstant
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentPayBinding
import shy.car.sdk.travel.pay.data.PayAmount
import shy.car.sdk.travel.pay.data.PayMethod
import shy.car.sdk.travel.pay.dialog.PayMethodSelectDialog
import shy.car.sdk.travel.pay.presenter.PayPresenter

/**
 * create by lz at 2018/06/17
 *  充值
 */
class PayFragment : XTBaseFragment(), PayPresenter.CallBack {
    override fun onGetListSuccess(t: List<PayAmount>) {

    }

    override fun onGetListError(e: Throwable) {

    }

    lateinit var binding: FragmentPayBinding
    lateinit var presenter: PayPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            presenter = PayPresenter(it, this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pay, null, false)
        binding.fragment = this
        binding.presenter = presenter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.getPayAmoutList()
    }

    fun selectPayMethod() {
        val dialog = ARouter.getInstance().build(RouteMap.PaySelect).withObject(ParamsConstant.Object1, presenter.payMethod.get()).withInt(ParamsConstant.Int1, 1).navigation() as PayMethodSelectDialog
        dialog.listener = object : PayMethodSelectDialog.OnPayClick {
            override fun onPaySelect(payMethod: PayMethod) {
                presenter.payMethod.set(payMethod)
            }
        }
        dialog.show(childFragmentManager,"fragment_pay")
    }

    fun pay(){
        presenter.pay()
    }
}