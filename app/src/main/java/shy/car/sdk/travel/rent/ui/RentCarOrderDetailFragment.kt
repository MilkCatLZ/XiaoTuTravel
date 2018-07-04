package shy.car.sdk.travel.rent.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import com.base.databinding.DataBindingAdapter
import com.base.util.Phone
import kotlinx.android.synthetic.main.fragment_rent_car_order_detail.*
import shy.car.sdk.BR
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.constant.ParamsConstant.Object1
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentRentCarOrderDetailBinding
import shy.car.sdk.travel.order.data.RentOrderDetail
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
        set(value) {
            field = value
            presenter.getRentOrderDetail(value)
        }

    override fun getDetailSuccess(t: RentOrderDetail) {
        binding.detail = t
        val adapter = DataBindingAdapter<RentOrderDetail.DiscountsBean>(R.layout.item_order_car_discount, BR.discount, null)
        adapter.setItems(t.discounts, 1)
        recyclerView_car_discount.adapter = adapter
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
    }


    fun call() {
        activity?.let { Phone.call(it, app.servicePhone) }
    }

    fun gotuCommentProcess() {
        ARouter.getInstance()
                .build(RouteMap.RentComment)
                .withObject(Object1, binding.detail)
                .navigation()
    }

    fun gotuInsuranceProcess() {
        ARouter.getInstance()
                .build(RouteMap.InsuranceProcess)
                .navigation()
    }

}