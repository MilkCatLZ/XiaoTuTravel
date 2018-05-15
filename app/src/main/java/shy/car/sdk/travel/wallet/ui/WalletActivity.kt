package shy.car.sdk.travel.wallet.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityWalletBinding

/**
 * create by LZ at 2018/05/15
 * 钱包
 */
@Route(path = RouteMap.Wallet)
class WalletActivity : XTBaseActivity() {

    lateinit var binding: ActivityWalletBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet)
    }

    /**
     * 支付
     */
    fun onPayClick() {

    }

    /**
     * 余额
     */
    fun onRemainClick() {

    }

    /**
     * 充值
     */
    fun onChargeMoneyClick() {

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