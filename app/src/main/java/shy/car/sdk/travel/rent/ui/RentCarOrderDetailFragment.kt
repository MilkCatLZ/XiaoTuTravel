package shy.car.sdk.travel.rent.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.base.util.Phone
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.databinding.FragmentRentCarOrderDetailBinding
import shy.car.sdk.travel.rent.data.RentOrderDetail
import shy.car.sdk.travel.rent.presenter.RentCarOrderDetailPresenter

/**
 * create by LZ at 2018/06/11
 * 租车详情
 */
class RentCarOrderDetailFragment : XTBaseFragment(),
        RentCarOrderDetailPresenter.CallBack {


    lateinit var binding: FragmentRentCarOrderDetailBinding
    lateinit var presenter: RentCarOrderDetailPresenter
    var orderID = ""

    override fun getDetailSuccess(t: RentOrderDetail) {
        binding.detail = t
    }

    override fun onError(e: Throwable) {
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { presenter = RentCarOrderDetailPresenter(it, this) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rent_car_order_detail, null, false)
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.getRentOrderDetail(orderID)
    }


    fun call() {
        activity?.let { Phone.call(it, "400-056-5317") }
    }

}