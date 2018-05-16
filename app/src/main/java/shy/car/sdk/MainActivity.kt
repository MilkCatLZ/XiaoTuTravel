package shy.car.sdk

import android.Manifest
import android.databinding.DataBindingUtil
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.amap.api.location.AMapLocation
import com.base.util.ToastManager
import com.lianni.mall.location.AmapLocationManager
import com.lianni.mall.location.AmapOnLocationReceiveListener
import com.lianni.mall.location.Location
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_city_select.*
import kotlinx.android.synthetic.main.layout_home_top.*
import me.yokeyword.indexablerv.IndexableLayout
import me.yokeyword.indexablerv.SimpleHeaderAdapter
import shy.car.sdk.app.base.XTBaseActivity
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.ActivityMainBinding
import shy.car.sdk.travel.location.LocationPresenter
import shy.car.sdk.travel.location.LocationPresenter.CallBack
import shy.car.sdk.travel.location.SearchFragment
import shy.car.sdk.travel.location.adapter.CityIndexAdapter
import shy.car.sdk.travel.location.data.City


@Route(path = "/app/homeActivity")
class MainActivity : XTBaseActivity() {

    private val LOCATING = "正在定位..."

    var isSearchVisible = ObservableBoolean(false)
    var isSearchMode = ObservableBoolean(false)
    //首页顶部显示的城市
    val cityName = ObservableField<String>(LOCATING)
    //搜索栏 显示的当前城市
    val cityNameLocating = ObservableField<String>(LOCATING)
    var currentCity: City? = null
    lateinit var binding: ActivityMainBinding

    private val carRentFragment = ARouter.getInstance().build(RouteMap.CarRent).navigation() as Fragment
    private val orderTakeFragment = ARouter.getInstance().build(RouteMap.OrderTake).navigation() as Fragment
    private val orderSendFragment = ARouter.getInstance().build(RouteMap.OrderSend).navigation() as Fragment

    lateinit var locationPresenter: LocationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //inject
        ARouter.getInstance()
                .inject(this)

        initPageFragment()
        initSearch()
        checkPermissi()
    }

    private fun initPageFragment() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.ac = this
        var transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frame_fragment_content, carRentFragment, tag)
        transaction.add(R.id.frame_fragment_content, orderTakeFragment, tag)
        transaction.add(R.id.frame_fragment_content, orderSendFragment, tag)
        transaction.hide(orderSendFragment)
        transaction.hide(orderTakeFragment)
        transaction.commit()

        radio_car_rent.performClick()
    }

    private fun checkPermissi() {
        val per = RxPermissions(this)
        per.request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe { granted ->
                    if (granted) {
                        initLocation()
                        refreshLocation()
                    } else {

                    }
                }

    }

    /**
     * 初始化城市列表
     */
    private fun initLocation() {

        locationPresenter = LocationPresenter(this, object : CallBack {
            override fun getCitySuccess(list: java.util.ArrayList<City>) {
                //添加数据
                indexable_layout.setLayoutManager(LinearLayoutManager(this@MainActivity))
                val adapter = CityIndexAdapter(this@MainActivity)
                indexable_layout.setAdapter(adapter)
                adapter.setDatas(list)
                adapter.setOnItemContentClickListener { _, _, _, entity ->
                    ToastManager.showShortToast(this@MainActivity, entity.cityName)
                    cityName.set(entity.cityName)
                    isSearchVisible.set(false)
                    currentCity = entity
                }


                val hotCity = ArrayList<City>()
                hotCity.add(City("南宁", "南宁"))
                hotCity.add(City("柳州", "柳州"))
                hotCity.add(City("北海", "北海"))

                //添加头部
                val headerAdapter = SimpleHeaderAdapter<City>(adapter, "热", "热门城市", hotCity)
                indexable_layout.addHeaderAdapter(headerAdapter)

                indexable_layout.setCompareMode(IndexableLayout.MODE_FAST)
                var filterList = ArrayList<City>()
                filterList.addAll(list)
                filterList.addAll(hotCity)

                (fragment_search_result as SearchFragment).bindDatas(filterList)
            }
        })
        locationPresenter.getCity()
    }

    private var searchEditText: TextView? = null

    private fun initSearch() {
        supportFragmentManager.beginTransaction().hide(fragment_search_result).commit()

        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    if (newText.trim(' ').isNotEmpty()) {
                        if (fragment_search_result.isHidden) {
                            supportFragmentManager.beginTransaction().show(fragment_search_result).commit()
                            isSearchMode.set(true)
                        }
                    } else {
                        if (!fragment_search_result.isHidden) {
                            supportFragmentManager.beginTransaction().hide(fragment_search_result).commit()
                            isSearchMode.set(false)
                        }
                    }
                }

                (fragment_search_result as SearchFragment).bindQueryText(newText)
                return false
            }

        })


        try {
            searchEditText = search_view.findViewById<TextView>(R.id.search_src_text)
            searchEditText?.setTextColor(resources.getColor(R.color.main_color_green))//字体颜色

//            textView.textSize = resources.getDimensionPixelOffset(R.dimen.text_primary_14dp).toFloat()//字体、提示字体大小
            searchEditText?.textSize = 14f//字体、提示字体大小
            searchEditText?.setHintTextColor(resources.getColor(R.color.text_third_primary_999999))//提示字体颜色
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun onClick(v: View) {
        app.startLoginDialog(null, null)
    }

    fun changeToOrderTakeFragment() {
        changeFragment(orderTakeFragment)
    }

    fun changeToCarRentFragment() {
        changeFragment(carRentFragment)
    }

    fun changeToOrderSendFragment() {
        changeFragment(orderSendFragment)
    }

    fun onAvatarClick() {
        drawer.openDrawer(Gravity.START)
    }


    fun onCityClick() {
        isSearchVisible.set(!isSearchVisible.get())
        if (isSearchVisible.get()) {
            cityNameLocating.set(LOCATING)
            getLocation()
        }
    }

    fun onCitySelected() {
        cityName.set(cityNameLocating.get())
        currentCity = getCityDetail(cityName.get())
    }

    private fun getCityDetail(cityName: String?): City? {
        return currentCity
    }

    private fun refreshLocation() {
        AmapLocationManager.instance.getLocation(object : AmapOnLocationReceiveListener {
            override fun onLocationReceive(ampLocation: AMapLocation, location: Location) {
                cityName.set(location.city)
            }
        })
    }

    private fun getLocation() {
        AmapLocationManager.instance.getLocation(object : AmapOnLocationReceiveListener {
            override fun onLocationReceive(ampLocation: AMapLocation, location: Location) {
                cityNameLocating.set(location.city)
            }
        })
    }

    private fun changeFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.hide(carRentFragment)
        transaction.hide(orderTakeFragment)
        transaction.hide(orderSendFragment)
        transaction.show(fragment)
        transaction.commit()
    }

    override fun onBackPressed() {
        if (isSearchVisible.get()) {
            closeSearch()
        } else {
            super.onBackPressed()
        }
    }

    private fun closeSearch() {
        isSearchVisible.set(false)
        searchEditText?.text = ""
    }


    fun onUserPicClick() {
        ARouter.getInstance().build(RouteMap.UserDetail).navigation()
        app.startLoginDialog(null, null)
    }

    fun onWalletClick() {
        ARouter.getInstance().build(RouteMap.Wallet).navigation()
    }

    fun onSettingClick() {
        ARouter.getInstance().build(RouteMap.Setting).navigation()
    }

    fun onKeFuClick() {
        ARouter.getInstance().build(RouteMap.KeFu).navigation()
    }
    fun onOrderClick() {
        ARouter.getInstance().build(RouteMap.OrderMine).navigation()
    }
}
