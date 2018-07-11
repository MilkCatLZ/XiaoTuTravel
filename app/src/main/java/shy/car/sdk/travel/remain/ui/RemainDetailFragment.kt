package shy.car.sdk.travel.remain.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import com.base.databinding.DataBindingAdapter
import com.base.widget.UltimateRecyclerView
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.base.XTBaseUltimateRecyclerViewFragment
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentRemainDetailBinding
import shy.car.sdk.travel.remain.data.RemainList
import shy.car.sdk.travel.remain.presenter.RemainDetailPresenter
import shy.car.sdk.travel.user.data.User

/**
 * create by LZ at 2018/06/13
 * 我的余额
 */
class RemainDetailFragment : XTBaseUltimateRecyclerViewFragment(),
        RemainDetailPresenter.OnRemainCallBack {
    override fun onGetListSuccess(list: List<RemainList>) {
        refreshOrLoadMoreComplete()
        checkHasMore()
    }

    override fun onGetListError() {

    }

    private fun checkHasMore() {
        if (presenter.hasMore()) {
            setFooterLoading()
        } else {
            setFooterNoMore()
        }
    }

    lateinit var binding: FragmentRemainDetailBinding
    lateinit var presenter: RemainDetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { presenter = RemainDetailPresenter(it, this) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_remain_detail, null, false)
        binding.fragment = this
        binding.presenter = presenter
        binding.user = User.instance
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onRefresh()
    }

    override fun getFragmentName(): String {
        return "我的余额"
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
        return binding.recyclerViewRemainDetail
    }

    override fun getAdapter(): DataBindingAdapter<*> {
        return presenter.adapter
    }

    fun gotoQA() {
        ARouter.getInstance()
                .build(RouteMap.QA)
                .navigation()
    }

}