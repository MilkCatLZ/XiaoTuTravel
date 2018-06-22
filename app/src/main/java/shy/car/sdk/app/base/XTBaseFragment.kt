package shy.car.sdk.app.base


import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.base.base.BaseFragment
import shy.car.sdk.app.Application
import shy.car.sdk.app.presenter.BasePresenter


/**
 * Created by LZ on 2017/7/31.
 */

open class XTBaseFragment : BaseFragment<Application>() {

    open fun getPresenter(): BasePresenter? {
        return null
    }

    override fun getBaseApplicationInterface(): Application {
        return application as Application
    }

    override fun onResume() {
        super.onResume()
        getPresenter()?.onResume()
    }

    override fun onPause() {
        super.onPause()
        getPresenter()?.onPause()
    }

    override fun onDestroy() {
        getPresenter()?.destroy()
        super.onDestroy()
    }

    override fun setSupportActionBar(toolbar: Toolbar?) {
        super.setSupportActionBar(toolbar)
        val textView = getToolbarTitleView((activity as AppCompatActivity?)!!, toolbar)
        if (textView != null) {
            tintHomeAsUpWithColor(textView.textColors.defaultColor)
        }
    }

    companion object {

        fun getToolbarTitleView(activity: AppCompatActivity, toolbar: Toolbar?): TextView? {
            val actionBar = activity.supportActionBar
            var actionbarTitle: CharSequence? = null
            if (actionBar != null)
                actionbarTitle = actionBar.title
            actionbarTitle = if (TextUtils.isEmpty(actionbarTitle)) toolbar!!.title else actionbarTitle
            if (TextUtils.isEmpty(actionbarTitle)) return null
            // can't find if title not set
            for (i in 0 until toolbar!!.childCount) {
                val v = toolbar.getChildAt(i)
                if (v != null && v is TextView) {
                    val t = v
                    val title = v.text
                    if (!TextUtils.isEmpty(title) && actionbarTitle == title && v.id == View.NO_ID) {
                        //Toolbar does not assign freightId to views with layout params SYSTEM, hence getFreightId() == View.NO_ID
                        //in same manner subtitle TextView can be obtained.
                        return v
                    }
                }
            }
            return null
        }
    }

}
