package shy.car.sdk.travel.wallet.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.constant.ParamsConstant
import shy.car.sdk.app.constant.ParamsConstant.Int1
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityWalletBinding
import shy.car.sdk.travel.pay.data.PayMethod
import shy.car.sdk.travel.pay.dialog.PayMethodSelectDialog
import shy.car.sdk.travel.wallet.presenter.WalletPresenter

/**
 * create by LZ at 2018/05/15
 * 钱包
 */
@Route(path = RouteMap.Wallet)
class WalletActivity : XTBaseActivity(), WalletPresenter.CallBack {

    lateinit var binding: ActivityWalletBinding

    lateinit var presenter: WalletPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = WalletPresenter(this, this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet)
        binding.ac = this
    }

    /**
     * 支付方式
     */
    fun onPayClick() {
        var dialog = ARouter.getInstance().build(RouteMap.PaySelect).withInt(Int1, 1).navigation() as PayMethodSelectDialog
        dialog.listener = object : PayMethodSelectDialog.OnPayClick {
            override fun onPaySelect(payMethod: PayMethod) {

            }
        }
        dialog.show(supportFragmentManager, "fragment_pay_method")
    }

    /**
     * 余额
     */
    fun onRemainClick() {
        ARouter.getInstance()
                .build(RouteMap.RemainDetail)
                .navigation()
    }

    /**
     * 充值
     */
    fun onChargeMoneyClick() {
        ARouter.getInstance()
                .build(RouteMap.Pay)
                .navigation()
    }

    /**
     * 提现
     */
    fun onTixianClick() {

    }

    /**
     * 银行卡
     */
    fun onBankCarClick() {
        ARouter.getInstance()
                .build(RouteMap.BankCard)
                .withBoolean(ParamsConstant.Boolean1, false)
                .navigation()
    }

    /**
     * 银行卡
     */
    fun onDiscountClick() {

    }

    /**
     * 保证金
     */
    fun onBaoZhengJinClick() {

    }

    /**
     *  保险
     */
    fun onWalletApplyClick() {

    }

    /**
     *  发票
     */
    fun onFaPiaoClick() {

    }
}