package shy.car.sdk.travel.main.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.base.databinding.DataBindingAdapter
import com.base.widget.UltimateRecyclerView
import org.greenrobot.eventbus.Subscribe
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseUltimateRecyclerViewFragment
import shy.car.sdk.app.eventbus.RefreshNearCarList
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.databinding.LayoutNearCarListBinding
import shy.car.sdk.travel.rent.data.NearCarList
import shy.car.sdk.travel.rent.presenter.NearCarPresenter

class MainNearCarListFragment : XTBaseUltimateRecyclerViewFragment() {

    interface CancelListener {
        fun onCancelClick()
    }

    var listener: CancelListener? = null

    override fun getFragmentName(): String {
        return "附近网点车辆"
    }

    override fun getPrecenter(): BasePresenter? {
        return nearCarListPresenter
    }

    override fun refresh() {
        nearCarListPresenter.refresh()
    }

    override fun getTotal(): Int {
        return nearCarListPresenter.getTotal()
    }


    override fun nextPage() {
        nearCarListPresenter.nextPage()
    }

    override fun getUltimateRecyclerView(): UltimateRecyclerView {
        return binding.recyclerViewNearCarList
    }

    override fun getAdapter(): DataBindingAdapter<*> {
        return nearCarListPresenter.adapter
    }

    private fun checkHasMore() {
        if (nearCarListPresenter.hasMore()!!) {
            setFooterLoading()
        } else {
            setFooterNoMore()
        }
    }

    lateinit var binding: LayoutNearCarListBinding
    lateinit var nearCarListPresenter: NearCarPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            nearCarListPresenter = NearCarPresenter(it, object : NearCarPresenter.CallBack {
                override fun onError(e: Throwable) {
                    refreshOrLoadMoreComplete()
                    checkHasMore()
                }

                override fun getListSuccess(list: ArrayList<NearCarList>) {
                    refreshOrLoadMoreComplete()
                    checkHasMore()
                }
            })
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_near_car_list, null, false)
        binding.fragment = this
        binding.presenter = nearCarListPresenter
        binding.edtNearCar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                nearCarListPresenter.setKeyWord(p0.toString())
                refreshNearCarList()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eventBusDefault.register(this)
    }

    /**
     * 打开城市选择列表
     */
    fun onCityClick() {

    }

    /**
     * 取消
     */
    fun onCancel() {
        listener?.onCancelClick()
    }

    private fun refreshNearCarList() {
        activity?.let {
            nearCarListPresenter.getNearList(app.location?.latitude.toString(), app.location?.longitude.toString())
        }
    }

    @Subscribe(sticky = true)
    fun onRefreshCarListReceive(refresh: RefreshNearCarList) {
        refreshNearCarList()
    }

    @Subscribe
    fun onCarListReceive(list: List<NearCarList>) {
        nearCarListPresenter?.setItems(list)
    }
}