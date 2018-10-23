package shy.car.sdk.travel.invoice.ui

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.constant.ParamsConstant.String1
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentInvoiceDetailBinding
import shy.car.sdk.travel.interfaces.CommonCallBack
import shy.car.sdk.travel.invoice.data.InvoiceDetail
import shy.car.sdk.travel.invoice.presenter.InvoiceDetailPresenter

/**
 * create by Sharon at 2018/07/07
 * 发票详情
 */
@Route(path = RouteMap.InvoiceDetail)
class InvoiceDetailActivity : XTBaseActivity(), CommonCallBack<InvoiceDetail> {
    override fun onSuccess(t: InvoiceDetail) {
        binding.detail = t
    }

    override fun onError(e: Throwable) {

    }


    @Autowired(name = String1)
    @JvmField
    var id = ""

    lateinit var binding: FragmentInvoiceDetailBinding
    lateinit var presenter: InvoiceDetailPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance()
                .inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_invoice_detail)
        presenter = InvoiceDetailPresenter(this, this)
        presenter.id = id
        presenter.getInvoiceDetail()
    }

}