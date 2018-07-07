package shy.car.sdk.travel.invoice.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.constant.ParamsConstant.String1
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentInvoiceDetailBinding

/**
 * create by Sharon at 2018/07/07
 * 发票详情
 */
@Route(path = RouteMap.InvoiceDetail)
class InvoiceDetailActivity : XTBaseActivity() {


    @Autowired(name = String1)
    @JvmField
    var id = ""

    lateinit var binding: FragmentInvoiceDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_invoice_detail)
    }

}