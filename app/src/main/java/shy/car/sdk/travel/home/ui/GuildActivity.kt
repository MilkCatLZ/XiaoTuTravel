package shy.car.sdk.travel.home.ui

import android.databinding.ViewDataBinding
import android.databinding.generated.callback.OnClickListener
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.base.databinding.DataBindingPagerAdapter
import com.base.util.Version
import kotlinx.android.synthetic.main.activity_guild.*
import shy.car.sdk.BR
import shy.car.sdk.BR.click
import shy.car.sdk.BR.start
import shy.car.sdk.MainActivity
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap
import kotlin.reflect.jvm.internal.impl.protobuf.LazyStringArrayList

@Route(path = RouteMap.Guild)
class GuildActivity : XTBaseActivity() {
    lateinit var adapter: DataBindingPagerAdapter<String>
    var items = ArrayList<String>()
    var currentPage = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guild)
        for (i in 0..4) {
            items.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534753065&di=d779429db8b2e4a8f79dfee72de9d661&imgtype=jpg&er=1&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20180331%2F0860b3aa6a7c49d5afb8150bb28cd7aa.jpeg")
        }
        viewPagerGuild.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                currentPage = position
            }
        })

        adapter = DataBindingPagerAdapter(this, R.layout.item_guild, BR.url, IntArray(BR.click), Array(1) {
            View.OnClickListener {
                if (currentPage == items.size - 1) {
                    ARouter.getInstance()
                            .build("/app/homeActivity")
                            .navigation()
                    Version.firstInstallComplete(this@GuildActivity)
                    finish()
                }
            }
        }, DataBindingPagerAdapter.CallBack { container, position, binding -> })
        adapter.items.addAll(items)
        viewPagerGuild.adapter = adapter
    }


}