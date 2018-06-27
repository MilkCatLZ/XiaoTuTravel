package shy.car.sdk.travel.wallet.ui

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
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
import shy.car.sdk.travel.user.data.User
import shy.car.sdk.travel.wallet.presenter.WalletPresenter

/**
 * create by LZ at 2018/05/15
 * 钱包
 */
@Route(path = RouteMap.Wallet)
class WalletActivity : XTBaseActivity(),
        WalletPresenter.CallBack {

    lateinit var binding: ActivityWalletBinding

    lateinit var presenter: WalletPresenter
    val remainText = ObservableField<String>()
    val bankCardText = ObservableField<String>()
    val couponText = ObservableField<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = WalletPresenter(this, this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet)
        binding.ac = this

        remainText.set("可提现金额${User.instance.balance}元")
        bankCardText.set("${User.instance.bankCardNum}张")
        couponText.set("${User.instance.couponNum}张")
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
        ARouter.getInstance()
                .build(RouteMap.TiXian)
                .navigation()
    }

    /**
     * 银行卡
     */
    fun onBankCarClick() {
        ARouter.getInstance()
                .build(RouteMap.BankCard)
                .navigation()
    }

    /**
     * 优惠券
     */
    fun onDiscountClick() {
        ARouter.getInstance()
                .build(RouteMap.Coupon)
                .navigation()
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