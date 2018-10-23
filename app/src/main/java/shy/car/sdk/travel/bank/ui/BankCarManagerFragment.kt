package shy.car.sdk.travel.bank.ui

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.launcher.ARouter
import com.base.widget.FullLinearLayoutManager
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentBankCardManagerBinding
import shy.car.sdk.travel.bank.data.BankCard
import shy.car.sdk.travel.bank.presenter.BankCardManagerPresenter

/**
 * create by 过期猫粮 at 2018/06/18
 */
class BankCarManagerFragment : XTBaseFragment(),
        BankCardManagerPresenter.CallBack {
    override fun onBankSelected(bank: BankCard) {
        finish()
    }

    fun setSelectMode(selectMode: Boolean) {
        if (presenter != null) {
            presenter.selectedMode = selectMode
        }
    }

    override fun onGetListSuccess(t: List<BankCard>) {
        binding.swipeRefresh.isRefreshing = false
    }


    override fun onGetListError(e: Throwable) {
        binding.swipeRefresh.isRefreshing = false
    }

    override fun getFragmentName(): String {
        return "银行卡"
    }

    lateinit var binding: FragmentBankCardManagerBinding
    lateinit var presenter: BankCardManagerPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            presenter = BankCardManagerPresenter(it, this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bank_card_manager, null, false)
//        binding.recyclerViewBankCardManage.layoutManager = FullLinearLayoutManager(activity)
        binding.fragment = this
        binding.presenter = presenter
        binding.swipeRefresh.setOnRefreshListener {
            presenter.refresh()
        }
        return binding.root

    }


    fun addBankCard() {
        ARouter.getInstance()
                .build(RouteMap.AddBankCard)
                .navigation()
    }

    override fun onResume() {
        super.onResume()
        binding.swipeRefresh.isRefreshing = true
        presenter.getBankCardList()
    }
}