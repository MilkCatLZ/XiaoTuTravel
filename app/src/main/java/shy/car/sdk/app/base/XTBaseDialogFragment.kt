package shy.car.sdk.app.base


import com.base.base.BaseDialogFragment
import shy.car.sdk.app.Application


/**
 * Created by LZ on 2017/3/20.
 */

open class XTBaseDialogFragment : BaseDialogFragment<Application>() {
    override fun getBaseApplicationInterface(): Application {
        return activity!!.application as Application
    }
}
