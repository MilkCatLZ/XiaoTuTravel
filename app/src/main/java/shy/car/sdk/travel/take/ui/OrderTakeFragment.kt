package shy.car.sdk.travel.take.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.base.databinding.DataBindingAdapter
import com.base.widget.UltimateRecyclerView
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseUltimateRecyclerViewFragment
import shy.car.sdk.app.constant.ParamsConstant
import shy.car.sdk.app.constant.ParamsConstant.Object1
import shy.car.sdk.app.data.LoginSuccess
import shy.car.sdk.app.eventbus.RefreshOrderList
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentOrderTakeBinding
import shy.car.sdk.travel.interfaces.onLoginDismiss
import shy.car.sdk.travel.take.data.DeliveryOrderList
import shy.car.sdk.travel.take.dialog.UserVerifyHintDialogFragment
import shy.car.sdk.travel.take.presenter.OrderTakePresenter
import shy.car.sdk.travel.user.data.User

/**
 * create by LZ at 2018/05/11
 * 首页-接单
 */
@Route(path = RouteMap.OrderTake)
class OrderTakeFragment : XTBaseUltimateRecyclerViewFragment() {
    override fun getPrecenter(): BasePresenter? {
        return presenter
    }

    var isGotoDetailClick = false
    lateinit var binding: FragmentOrderTakeBinding
    var takeOrderList: DeliveryOrderList? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            presenter = OrderTakePresenter(it, object : OrderTakePresenter.CallBack {
                override fun getListError(e: Throwable) {
                    refreshOrLoadMoreComplete()
                    checkHasMore()
                }

                override fun onItemClick(takeOrderList: DeliveryOrderList) {
                    this@OrderTakeFragment.takeOrderList = takeOrderList
                    if (User.instance.login) {
                        gotoDetailIsLogin()
                    } else {
                        gotoDetailIsNoLogin()
                    }
                }

                override fun getListSuccess(list: List<DeliveryOrderList>) {
                    refreshOrLoadMoreComplete()
                    checkHasMore()
                }
            })
        }
        eventBusDefault.register(this)
    }

    /**
     * 未登录 查看详情
     */
    private fun gotoDetailIsNoLogin() {
        isGotoDetailClick = true
        this@OrderTakeFragment.takeOrderList = takeOrderList
        app.startLoginDialog(null, null, object : onLoginDismiss {
            override fun onCancel() {
                isGotoDetailClick = false
            }
        })
    }

    /**
     * 已登录 查看详情
     */
    private fun gotoDetailIsLogin() {
        if (User.instance.identityAuth == 2) {
            ARouter.getInstance()
                    .build(RouteMap.OrderDetail)
                    .withString(ParamsConstant.String1, takeOrderList?.freightId)
                    .navigation()
        } else {
            var userVerifyDialogFragment = UserVerifyHintDialogFragment()
            userVerifyDialogFragment.takeOrderList = takeOrderList
            userVerifyDialogFragment.show(childFragmentManager, "fragment_user_verify_hint_dialog")
        }

    }


    private fun checkUerVerify() {
        if (User.instance.isIdentityAuth()) {
            ARouter.getInstance()
                    .build(RouteMap.OrderDetail)
                    .withString(ParamsConstant.String1, takeOrderList?.freightId)
                    .navigation()
        } else {
            var userVerifyDialogFragment = UserVerifyHintDialogFragment()
            userVerifyDialogFragment.takeOrderList = takeOrderList
            userVerifyDialogFragment.show(childFragmentManager, "fragment_user_verify_hint_dialog")
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_take, null, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.presenter = presenter
        onRefresh()
    }

    lateinit var presenter: OrderTakePresenter


    override fun getFragmentName(): String {
        return "首页-接单"
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
        return binding.recyclerViewOrderTake
    }

    override fun getAdapter(): DataBindingAdapter<*> {
        return presenter.adapter
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginSuccess(success: LoginSuccess) {
        if (isGotoDetailClick) {
            isGotoDetailClick = false
            checkUerVerify()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshOrderList(refresh: RefreshOrderList) {
        onRefresh()
    }
}