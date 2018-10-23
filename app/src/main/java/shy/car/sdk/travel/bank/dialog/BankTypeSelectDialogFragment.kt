package shy.car.sdk.travel.bank.dialog

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseDialogFragment
import shy.car.sdk.databinding.DialogBankTypeSelectBinding
import shy.car.sdk.travel.bank.data.BankType
import shy.car.sdk.travel.bank.presenter.BankTypeSelectPresenter

/**
 * create by LZ at 2018/06/19
 * 选择可用银行
 */
class BankTypeSelectDialogFragment : XTBaseDialogFragment(),
        BankTypeSelectPresenter.CallBack {

    interface OnBankTypeSelectedListener {
        fun onBankTypeSelected(bankType: BankType)
    }

    override fun onItemClick(bankType: BankType) {
        listener?.onBankTypeSelected(bankType)
        dismissAllowingStateLoss()
    }

    override fun onGetListSuccess(t: List<BankType>) {
        binding.presenter = presenter
    }

    override fun onGetListError(e: Throwable) {

    }

    var listener: OnBankTypeSelectedListener? = null
    lateinit var binding: DialogBankTypeSelectBinding
    lateinit var presenter: BankTypeSelectPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { presenter = BankTypeSelectPresenter(it, this) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_bank_type_select, null, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.getBankTypeList()
    }

}