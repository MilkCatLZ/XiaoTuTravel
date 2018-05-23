package shy.car.sdk.travel.take.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.base.util.Log
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.route.ObjectSerialisation
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.travel.take.data.TakeOrderList

/**
 * create by LZ at 2018/05/23
 * 接单详情
 */
@Route(path = RouteMap.OrderTakeDetailFragment)
class OrderTakeDetailFragment : XTBaseFragment() {

    @Autowired
    @JvmField
    public var takeOrderList: TakeOrderList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance()
                .inject(this)
        Log.d(tag, takeOrderList?.toString())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}