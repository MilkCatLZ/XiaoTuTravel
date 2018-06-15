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
import com.base.location.AmapLocationManager
import com.base.location.AmapOnLocationReceiveListener
import com.base.location.Location
import me.yokeyword.indexablerv.IndexableLayout
import me.yokeyword.indexablerv.SimpleHeaderAdapter
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.databinding.LayoutCitySelectBinding
import shy.car.sdk.travel.location.LocationPresenter
import shy.car.sdk.travel.location.SearchFragment
import shy.car.sdk.travel.location.adapter.CityIndexAdapter
import shy.car.sdk.travel.location.data.CurrentLocation

/**
 *
 */
class MainCitySelectFragment : XTBaseFragment() {
    interface CitySelectListener {
        fun onCitySelected(location: CurrentLocation)
        fun onSearchClosed()

    }

    var listener: CitySelectListener? = null

    private val LOCATING = "正在定位..."
    //搜索栏 显示的当前城市
    val cityNameLocating = ObservableField<String>(LOCATING)
    lateinit var binding: LayoutCitySelectBinding
    var currentCity = CurrentLocation()

    lateinit var locationPresenter: LocationPresenter
    val searchResultFragment = SearchFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentCity = app.location.copy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_city_select, null, false)
        binding.fragment = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearch()
        initCityList()
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.frame_search_result, searchResultFragment, "search_result")
        transaction.hide(searchResultFragment)
        transaction.commit()
    }

    private var searchEditText: TextView? = null

    private fun initSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
            searchEditText = binding.searchView.findViewById<TextView>(R.id.search_src_text)
            searchEditText?.setTextColor(resources.getColor(R.color.main_color_green))//字体颜色

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
                override fun getCitySuccess(list: List<CurrentLocation>) {
                    //添加数据
                    addCityInfo(list)
                }
            })
            locationPresenter.getCity()
        }
    }

    private fun addCityInfo(list: List<CurrentLocation>) {
        //添加城市列表
        activity?.let {
            binding.indexableLayout.setLayoutManager(LinearLayoutManager(it))
            val adapter = CityIndexAdapter(it)
            binding.indexableLayout.setAdapter(adapter)
            adapter.setDatas(list)
            adapter.setOnItemContentClickListener { _, _, _, entity ->
                //                ToastManager.showShortToast(it, entity.cityName)
                listener?.onCitySelected(entity)
            }

            //----------------------添加热门城市
            val hotCity = ArrayList<CurrentLocation>()
            var l1=CurrentLocation()
            l1.cityName="南宁市"
            l1.cityCode="0771"
            l1.lng=108.36637
            l1.lat=22.817746
            var l2=CurrentLocation()
            l2.cityName="柳州"
            l2.cityCode="0772"
            l2.lng=109.411703
            l2.lat=24.314617
            var l3=CurrentLocation()
            l3.cityName="北海"
            l3.cityCode="0773"
            l3.lng=109.119254
            l3.lat=21.473343
            hotCity.add(l1)
            hotCity.add(l2)
            hotCity.add(l3)

            //添加头部
            val headerAdapter = SimpleHeaderAdapter<CurrentLocation>(adapter, "热", "热门城市", hotCity)
            binding.indexableLayout.addHeaderAdapter(headerAdapter)

            binding.indexableLayout.setCompareMode(IndexableLayout.MODE_FAST)
            var filterList = ArrayList<CurrentLocation>()
            filterList.addAll(list)
            filterList.addAll(hotCity)

            searchResultFragment.bindDatas(filterList)
        }


    }

    /**
     * 搜索模式下 用于展示搜索的
     */
    fun getLocation() {
        AmapLocationManager.getInstance()
                .getLocation(object : AmapOnLocationReceiveListener {
                    override fun onLocationReceive(ampLocation: AMapLocation, location: Location) {
                        cityNameLocating.set(location.city)
                        currentCity.copy(location)
                    }
                })
    }

    fun onCitySelected() {
        listener?.onCitySelected(currentCity)
    }

    fun closeSearch() {
        searchEditText?.text = ""
        listener?.onSearchClosed()
    }

    override fun onRealResume() {
        super.onRealResume()
        getLocation()
    }
}