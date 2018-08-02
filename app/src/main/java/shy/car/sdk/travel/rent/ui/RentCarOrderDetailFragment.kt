package shy.car.sdk.travel.rent.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import com.base.databinding.DataBindingAdapter
import com.base.util.Phone
import com.base.widget.FullLinearLayoutManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_rent_car_order_detail.*
import shy.car.sdk.BR
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.constant.ParamsConstant.Object1
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentRentCarOrderDetailBinding
import shy.car.sdk.travel.order.data.RentOrderDetail
import shy.car.sdk.travel.rent.data.RentOrderState
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
        val adapter = DataBindingAdapter<RentOrderDetail.DiscountsBean>(R.layout.item_order_car_discount, BR.discount, null)
        adapter.setItems(t.discounts, 1)
        recyclerView_car_discount.adapter = adapter

        val adapter1 = DataBindingAdapter<shy.car.sdk.travel.order.data.RentOrderDetail.PreferentialBean>(R.layout.item_rent_car_discount, BR.preferential, null)
        adapter1.setItems(t.preferential, 1)
        activity?.let {
            recyclerView_order_car_discount.layoutManager = FullLinearLayoutManager(it)
        }
        recyclerView_order_car_discount.adapter = adapter1
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
        if ("0" == binding.detail?.commentId)
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


    override fun onResume() {
        super.onResume()
        Observable.create<String> {
            while (orderID.isNullOrEmpty()) {
                Thread.sleep(200)
            }
            it.onNext(orderID)
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    presenter.getRentOrderDetail(orderID)
                }, {})

    }
}