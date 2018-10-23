package shy.car.sdk.app.base


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import android.view.View
import com.base.base.BaseUltimateRecyclerViewActivity
import com.base.util.StringUtils
import shy.car.sdk.R
import shy.car.sdk.app.Application


/**
 * Created by LZ on 2017/3/20.
 */

abstract class XTBaseUltimateRecyclerViewActivity : BaseUltimateRecyclerViewActivity<Application>() {
    override fun getBaseApplicationInterface(): Application {
        return applicationContext as Application
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
//        PushAgent.getInstance(this).onAppStart()
    }

    override fun onResume() {
        super.onResume()
        //        MobclickAgent.onResume(this);
    }

    override fun onPause() {
        super.onPause()
        //        MobclickAgent.onPause(this);
    }

    override fun startActivity(intent: Intent) {
        super.startActivity(intent)
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right)
    }

    override fun setSupportActionBar(toolbar: Toolbar?) {
        super.setSupportActionBar(toolbar)
        val textView = XTBaseActivity.getToolbarTitleView(this, toolbar)
        if (textView != null && StringUtils.isNotEmpty(textView.text)) {
            tintHomeAsUpWithColor(textView.textColors.defaultColor)
        }

    }


    fun back(view: View) {
        onBackPressed()
    }
}
