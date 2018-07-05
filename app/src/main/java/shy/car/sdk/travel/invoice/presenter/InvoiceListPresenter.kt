package shy.car.sdk.travel.invoice.presenter

import android.content.Context
import com.base.databinding.DataBindingItemClickAdapter
import shy.car.sdk.BR
import shy.car.sdk.R
import shy.car.sdk.app.presenter.BasePresenter
import shy.car.sdk.travel.interfaces.CommonCallBack
import shy.car.sdk.travel.invoice.data.InvoiceList

class InvoiceListPresenter(context: Context, var callBack: CommonCallBack<InvoiceList>) : BasePresenter(context) {
    val adapter = DataBindingItemClickAdapter<InvoiceList>(R.layout.item_invoice_list, BR.invoice, BR.click, {}, { holder, position ->

    })
}