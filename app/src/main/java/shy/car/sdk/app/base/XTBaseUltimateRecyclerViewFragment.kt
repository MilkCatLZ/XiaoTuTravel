package shy.car.sdk.app.base


import android.content.Intent

import com.base.base.BaseUltimateRecyclerViewFragment
import shy.car.sdk.R
import shy.car.sdk.app.Application
import shy.car.sdk.app.presenter.BasePresenter


/**
 * Created by LZ on 2016/10/10.
 */

abstract class XTBaseUltimateRecyclerViewFragment : BaseUltimateRecyclerViewFragment<Application>() {

    protected abstract val presenter: BasePresenter?
    abstract override fun getFragmentName(): String

    override fun onResume() {
        super.onResume()
        if (presenter != null) {
            presenter!!.onResume()
        }
        //        MobclickAgent.onPageStart(getFragmentName());
    }

    override fun onPause() {
        super.onPause()
        if (presenter != null) {
            presenter!!.onPause()
        }
        //        MobclickAgent.onPageEnd(getFragmentName());
    }

    override fun onDestroy() {
        if (presenter != null) {
            presenter!!.destroy()
        }
        super.onDestroy()
    }

    override fun getBaseApplicationInterface(): Application {
        return application as Application
    }

    override fun startActivity(intent: Intent) {
        super.startActivity(intent)
        activity!!.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left)
    }

    override fun finish() {
        super.finish()
        activity!!.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right)
    }

}
