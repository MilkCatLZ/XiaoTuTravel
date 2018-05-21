package shy.car.sdk.travel.rent.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.base.databinding.DataBindingAdapter
import com.base.widget.UltimateRecyclerView
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseUltimateRecyclerViewFragment
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.databinding.FragmentCarSelectBinding
import shy.car.sdk.travel.rent.data.CarSelectInfo
import shy.car.sdk.travel.rent.presenter.CarSelectPresenter

/**
 * create by LZ at 2018/05/21
 * 选择车辆类型
 */
class CarSelectFragment : XTBaseUltimateRecyclerViewFragment(), CarSelectPresenter.CallBack {
    override fun onCarSelected(carSelectInfo: CarSelectInfo) {
        listener?.onCarSelected(carSelectInfo)
        finish()
    }

    interface CarSelectListener {
        fun onCarSelected(carSelectInfo: CarSelectInfo)
    }

    var listener: CarSelectListener? = null

    lateinit var binding: FragmentCarSelectBinding
    lateinit var presenter: shy.car.sdk.travel.rent.presenter.CarSelectPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { presenter = CarSelectPresenter(it, this) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_car_select, null, false)
        return binding.root
    }

    override fun getFragmentName(): String {
        return "选择车辆"
    }

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
        return binding.recyclerViewCarSelect
    }

    override fun getAdapter(): DataBindingAdapter<*> {
        return presenter.adapter
    }

    override fun getListSuccess(list: ArrayList<CarSelectInfo>) {
        refreshOrLoadMoreComplete()
        checkHasMore()
    }

    override fun onError(e: Throwable) {

    }

    private fun checkHasMore() {
        if (presenter.hasMore()) {
            setFooterLoading()
        } else {
            setFooterNoMore()
        }
    }
}