package shy.car.sdk.travel.location.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.base.databinding.DataBindingAdapter

import me.yokeyword.indexablerv.IndexableHeaderAdapter
import shy.car.sdk.BR
import shy.car.sdk.R
import shy.car.sdk.databinding.ItemCityHotBinding
import shy.car.sdk.travel.location.data.City

class CityHeaderAdapter(var inflater: LayoutInflater, index: String, indexTitle: String, list: List<City>) : IndexableHeaderAdapter<City>(index, indexTitle, list) {


    override fun onBindContentViewHolder(holder: RecyclerView.ViewHolder?, entity: City?) {
        var binding = (holder as CityHotHolder).binding
        binding.name = entity?.cityName
        var adapter = DataBindingAdapter<City>(R.layout.item_city, BR.city, null)
        var list = ArrayList<City>()
        for (i in 0..40) {
            list.add(City("南宁$i", i.toString()))
        }
        adapter.setItems(list, 1)
        binding.adapter = adapter
    }

    override fun getItemViewType(): Int {
        return 1 //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateContentViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        var binding = DataBindingUtil.inflate<ItemCityHotBinding>(inflater, R.layout.item_city_hot, parent, false)
        return CityHotHolder(binding)
    }

    class CityHotHolder(var binding: ItemCityHotBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}