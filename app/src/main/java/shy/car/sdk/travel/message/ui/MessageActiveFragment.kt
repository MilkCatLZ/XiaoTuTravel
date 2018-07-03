package shy.car.sdk.travel.message.ui

import android.databinding.DataBindingUtil
import android.databinding.ObservableInt
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.base.databinding.DataBindingAdapter
import com.base.widget.UltimateRecyclerView
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseUltimateRecyclerViewFragment
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.databinding.FragmentMessageActiveBinding
import shy.car.sdk.travel.message.data.MessageList
import shy.car.sdk.travel.message.presenter.MessageActivePresenter

/**
 * create by LZ at 2018/05/16
 * 活动消息
 */

class MessageActiveFragment : XTBaseUltimateRecyclerViewFragment() {
    lateinit var binding: FragmentMessageActiveBinding
    lateinit var presenter: MessageActivePresenter

    val checkedTab = ObservableInt(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            presenter = MessageActivePresenter(it, object : MessageActivePresenter.CallBack {
                override fun getListError(e: Throwable) {
                    refreshOrLoadMoreComplete()
                    checkHasMore()
                }

                override fun getListSuccess(list: ArrayList<MessageList>) {
                    refreshOrLoadMoreComplete()
                    checkHasMore()
                }
            })
        }
    }

    private fun checkHasMore() {
        if (presenter.hasMore()) {
            setFooterLoading()
        } else {
            setFooterNoMore()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_message_active, null, false)
        binding.fragment = this
        binding.presenter = presenter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onRefresh()
    }


    override fun getFragmentName(): String {
        return "我的订单"
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
        return binding.recyclerViewMessage
    }

    override fun getAdapter(): DataBindingAdapter<*> {
        return presenter.adapter
    }

    fun checkChange(i: Int) {
        checkedTab.set(i)
        onRefresh()
    }
}