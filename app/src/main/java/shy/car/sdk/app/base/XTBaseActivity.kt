package shy.car.sdk.app.base


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.base.util.StringUtils
import shy.car.sdk.R
import shy.car.sdk.app.Application
import shy.car.sdk.app.presenter.BasePresenter


/**
 * Created by LZ on 2017/7/31.
 * 基类
 */
open class XTBaseActivity : com.base.base.BaseActivity<Application>() {

    private val presenter: BasePresenter?
        get() = null

    private fun startAnim() {
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left)
    }

    private fun endAnim() {
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right)
    }

    override fun getBaseApplicationInterface(): Application {
        return application as Application
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        PushAgent.getInstance(this).onAppStart()
    }

    override fun startActivity(intent: Intent) {
        super.startActivity(intent)
        startAnim()
    }

    override fun finish() {
        super.finish()
        endAnim()
    }

    override fun onResume() {
        super.onResume()
        //友盟统计
        //        MobclickAgent.onResume(this);
        if (presenter != null) {
            presenter!!.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        //友盟统计
        //        MobclickAgent.onPause(this);
        if (presenter != null) {
            presenter!!.onPause()
        }
    }

    override fun onDestroy() {
        if (presenter != null) {
            presenter!!.destroy()
        }
        super.onDestroy()
    }

    override fun setSupportActionBar(toolbar: Toolbar?) {
        super.setSupportActionBar(toolbar)
        val textView = getToolbarTitleView(this, toolbar)
        if (textView != null && StringUtils.isNotEmpty(textView.text)) {
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

    fun back(view: View) {
        onBackPressed()
    }
}
