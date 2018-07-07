package shy.car.sdk.travel.invoice.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.layout_invoice_post_detail.*
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.databinding.FragmentInvocePostBinding
import shy.car.sdk.travel.interfaces.CommonCallBack
import shy.car.sdk.travel.invoice.data.InvoiceList
import shy.car.sdk.travel.invoice.presenter.InvoicePostPresenter

/**
 * create by LZ at 2018/07/06
 * 提交开发票申请
 */
class InvoicePostFragment : XTBaseFragment(),
        CommonCallBack<JsonObject> {
    override fun onSuccess(t: JsonObject) {

    }

    override fun onError(e: Throwable) {

    }

    var list = ArrayList<InvoiceList.Orders>()
    set(value){
        field=value
        presenter.setLists(list)
    }

    lateinit var binding: FragmentInvocePostBinding
    lateinit var presenter: InvoicePostPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            presenter = InvoicePostPresenter(it, this)

    }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_invoce_post, null, false)
        binding.presenter = presenter
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        radio_com.isChecked = true
        radio_taitou_type.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_com -> {
                    presenter.taitouType.set(1)
                }
                R.id.radio_person -> {
                    presenter.taitouType.set(2)
                }
            }
        }
    }

    fun post() {
        presenter.post()
    }
}