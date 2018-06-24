package shy.car.sdk.travel.rent.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.databinding.FragmentDrivingBinding
import shy.car.sdk.travel.order.data.RentOrderDetail
import shy.car.sdk.travel.rent.presenter.DrivingPresenter

/**
 * create by 过期猫粮 at 2018/06/24
 * 行程中
 */
class DrivingFragment : XTBaseFragment(), DrivingPresenter.CallBack {
    override fun onGetDetailSuccess(t: RentOrderDetail) {

    }

    override fun onGetDetailError(e: Throwable) {

    }

    fun setOid(oid: String) {
        presenter.getOrderDetail(oid)
    }

    lateinit var binding: FragmentDrivingBinding
    lateinit var presenter: DrivingPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            presenter = DrivingPresenter(it, this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_driving, null, false)
        binding.fragment = this
        binding.presenter = presenter
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    /**
     * 联系调度员
     */
    fun callAdmin() {

    }

    /**
     * 上报故障
     */
    fun feedBackTrouble() {

    }

    /**
     * 上报故障
     */
    fun ringCar() {
        presenter.carRing()
    }
    /**
     * 开启车门
     */
    fun openDoor() {
        presenter.openDoor()
    }
    /**
     * 开启车门
     */
    fun lockDoor() {
        presenter.lockDoor()
    }
}