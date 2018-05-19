package shy.car.sdk.travel.main.ui

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.amap.api.location.AMapLocation
import com.lianni.mall.location.AmapLocationManager
import com.lianni.mall.location.AmapOnLocationReceiveListener
import com.lianni.mall.location.Location
import kotlinx.android.synthetic.main.layout_city_select.*
import me.yokeyword.indexablerv.IndexableLayout
import me.yokeyword.indexablerv.SimpleHeaderAdapter
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.databinding.LayoutCitySelectBinding
import shy.car.sdk.travel.location.LocationPresenter
import shy.car.sdk.travel.location.SearchFragment
import shy.car.sdk.travel.location.adapter.CityIndexAdapter
import shy.car.sdk.travel.location.data.City

/**
 *
 */
class MainCitySelectFragment : XTBaseFragment() {
    interface CitySelectListener {
        fun onCitySelected(get: City)
        fun onSearchClosed()

    }

    var listener: CitySelectListener? = null

    private val LOCATING = "正在定位..."
    //搜索栏 显示的当前城市
    val cityNameLocating = ObservableField<String>(LOCATING)
    lateinit var binding: LayoutCitySelectBinding
    lateinit var currentCity: City

    lateinit var locationPresenter: LocationPresenter
    val searchResultFragment = SearchFragment()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_city_select, null, false)
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearch()
        initCityList()
        getLocation()
        val transaction=childFragmentManager.beginTransaction()
        transaction.add(R.id.frame_search_result, searchResultFragment, "search_result")
        transaction.hide(searchResultFragment)
        transaction .commit()
    }

    private var searchEditText: TextView? = null

    private fun initSearch() {





        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    if (newText.trim(' ').isNotEmpty()) {
                        if (searchResultFragment.isHidden) {
                            childFragmentManager.beginTransaction()
                                    .show(searchResultFragment)
                                    .commit()
                        }
                    } else {
                        if (!searchResultFragment.isHidden) {
                            childFragmentManager.beginTransaction()
                                    .hide(searchResultFragment)
                                    .commit()
                        }
                    }
                }

                searchResultFragment.bindQueryText(newText)
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

    /**
     * 初始化城市列表
     */
    private fun initCityList() {
        activity?.let {
            locationPresenter = LocationPresenter(it, object : LocationPresenter.CallBack {
                override fun getCitySuccess(list: java.util.ArrayList<City>) {
                    //添加数据
                    addCityInfo(list)
                }
            })
            locationPresenter.getCity()
        }
    }

    private fun addCityInfo(list: java.util.ArrayList<City>) {
        //添加城市列表
        activity?.let {
            indexable_layout.setLayoutManager(LinearLayoutManager(it))
            val adapter = CityIndexAdapter(it)
            indexable_layout.setAdapter(adapter)
            adapter.setDatas(list)
            adapter.setOnItemContentClickListener { _, _, _, entity ->
//                ToastManager.showShortToast(it, entity.cityName)
                listener?.onCitySelected(entity)
            }

            //----------------------添加热门城市
            val hotCity = ArrayList<City>()
            hotCity.add(City("南宁市", "南宁市"))
            hotCity.add(City("柳州市", "柳州市"))
            hotCity.add(City("北海市", "北海市"))

            //添加头部
            val headerAdapter = SimpleHeaderAdapter<City>(adapter, "热", "热门城市", hotCity)
            indexable_layout.addHeaderAdapter(headerAdapter)

            indexable_layout.setCompareMode(IndexableLayout.MODE_FAST)
            var filterList = ArrayList<City>()
            filterList.addAll(list)
            filterList.addAll(hotCity)

            searchResultFragment.bindDatas(filterList)
        }


    }

    private fun getCityDetail(cityName: String?): City? {
        return currentCity
    }

    /**
     * 搜索模式下 用于展示搜索的
     */
    private fun getLocation() {
        AmapLocationManager.instance.getLocation(object : AmapOnLocationReceiveListener {
            override fun onLocationReceive(ampLocation: AMapLocation, location: Location) {
                cityNameLocating.set(location.city)
                currentCity = City(location.city, getCode(location))
                currentCity.lat = location.latitude
                currentCity.lng = location.longitude
            }
        })
    }

    private fun getCode(location: Location): String {
        return ""
    }

    fun onCitySelected() {
        listener?.onCitySelected(currentCity)
    }

    fun closeSearch() {
        searchEditText?.text = ""
        listener?.onSearchClosed()
    }
}