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
import shy.car.sdk.databinding.FragmentMessageServiceBinding
import shy.car.sdk.databinding.FragmentMessageSystemBinding
import shy.car.sdk.travel.message.data.MessageList
import shy.car.sdk.travel.message.presenter.MessageServicePresenter
import shy.car.sdk.travel.message.presenter.MessageSystemPresenter

/**
 * create by LZ at 2018/05/16
 * 系统消息
 */

class MessageServiceFragment : XTBaseUltimateRecyclerViewFragment() {
    lateinit var binding: FragmentMessageServiceBinding
    lateinit var presenter: MessageServicePresenter

    val checkedTab = ObservableInt(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            presenter = MessageServicePresenter(it, object : MessageServicePresenter.CallBack {
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_message_service, null, false)
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