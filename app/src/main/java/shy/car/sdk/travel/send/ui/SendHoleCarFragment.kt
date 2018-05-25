package shy.car.sdk.travel.send.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.databinding.FragmentSendHoldCarBinding
import shy.car.sdk.travel.send.data.CarInfo
import shy.car.sdk.travel.send.presenter.SendHoleCarPresenter


/**
 * create by LZ at 2018/05/25
 * 整车发货填写
 */
class SendHoleCarFragment : XTBaseFragment(), SendHoleCarPresenter.CallBack {
    override fun getCarListSuccess(list: ArrayList<CarInfo>) {

    }

    lateinit var binding: FragmentSendHoldCarBinding
    lateinit var presenter: SendHoleCarPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { presenter = SendHoleCarPresenter(it, this) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_send_hold_car, null, false)
        binding.viewPagerSendHoldCar.adapter = presenter.adapter
        binding.viewPagerSendHoldCar.setPageTransformer(true, TransFormer())
        binding.viewPagerSendHoldCar.offscreenPageLimit = 5
        return binding.root
    }


}