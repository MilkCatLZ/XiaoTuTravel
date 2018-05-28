package shy.car.sdk.travel.location.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import me.yokeyword.indexablerv.IndexableAdapter
import shy.car.sdk.R
import shy.car.sdk.databinding.ItemCityBinding
import shy.car.sdk.databinding.ItemCityTitleBinding
import shy.car.sdk.travel.location.data.CurrentLocation

class CityIndexAdapter(var context: Context) : IndexableAdapter<CurrentLocation>() {
    private var layoutInflate = LayoutInflater.from(context)

    override fun onCreateTitleViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        var binding = DataBindingUtil.inflate<ItemCityTitleBinding>(layoutInflate, R.layout.item_city_title, parent, false)
        return TitleHolder(binding)
    }

    override fun onBindContentViewHolder(holder: RecyclerView.ViewHolder, entity: CurrentLocation?) {
        val h = holder as CityHolder
        h.binding.city = entity
    }

    override fun onBindTitleViewHolder(holder: RecyclerView.ViewHolder?, indexTitle: String?) {
        val h = holder as TitleHolder
        h.binding.name = indexTitle
    }

    override fun onCreateContentViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        var binding = DataBindingUtil.inflate<ItemCityBinding>(layoutInflate, R.layout.item_city, parent, false)
        return CityHolder(binding)
    }

    class TitleHolder(b: ItemCityTitleBinding) : RecyclerView.ViewHolder(b.root) {
        var binding = b

    }

    class CityHolder(b: ItemCityBinding) : RecyclerView.ViewHolder(b.root) {
        var binding = b
    }
}