package shy.car.sdk.travel.coupon.ui

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.base.databinding.DataBindingAdapter
import com.base.widget.UltimateRecyclerView
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseUltimateRecyclerViewFragment
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.databinding.FragmentCouponBinding
import shy.car.sdk.travel.coupon.data.Coupon
import shy.car.sdk.travel.coupon.presenter.CouponPresenter

/**
 * create by 过期猫粮 at 2018/06/18
 */
class CouPonFragment : XTBaseUltimateRecyclerViewFragment(),
        CouponPresenter.CallBack {
    override fun getPrecenter(): BasePresenter? {
        return presenter
    }

    override fun refresh() {
        presenter.refresh()
    }

    override fun getTotal(): Int {
        return presenter.getTotal()
    }

    override fun nextPage() {
        presenter.nextPage()
    }

    override fun getUltimateRecyclerView(): UltimateRecyclerView {
        return binding.recyclerViewCoupon
    }

    override fun getAdapter(): DataBindingAdapter<*> {
        return presenter.adapter
    }

    override fun onGetListSuccess(t: List<Coupon>) {
        refreshOrLoadMoreComplete()
        checkHasMore()
    }


    override fun onGetListError(e: Throwable) {
        refreshOrLoadMoreComplete()
        checkHasMore()
    }

    private fun checkHasMore() {
        if (presenter.hasMore()) {
            setFooterLoading()
        } else {
            setFooterNoMore()
        }
    }

    override fun getFragmentName(): String {
        return "优惠卷"
    }

    lateinit var binding: FragmentCouponBinding
    lateinit var presenter: CouponPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            presenter = CouponPresenter(it, this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_coupon, null, false)
        binding.fragment = this
        binding.presenter = presenter
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onRefresh()
    }

    fun setSelectedMode(selectMode: Boolean) {

    }


}