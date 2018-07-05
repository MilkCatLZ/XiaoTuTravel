package shy.car.sdk.travel.user.ui

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.alibaba.android.arouter.facade.annotation.Route
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.app.route.RouteMap
import shy.car.sdk.databinding.FragmentVipBinding
import shy.car.sdk.travel.user.data.Rank
import shy.car.sdk.travel.user.data.User
import shy.car.sdk.travel.user.presenter.VipPresenter

/**
 * create by LZ at 2018/07/05
 * 我的会员
 */
@Route(path = RouteMap.Vip)
class VipFragment : XTBaseFragment(),
        VipPresenter.CallBack {
    override fun onGetRankListSuccess(list: List<Rank>) {
        activity?.let {
            val layoutManager = LinearLayoutManager(it)
            layoutManager.orientation = LinearLayout.HORIZONTAL
            binding.recyclerViewRank.layoutManager = layoutManager
            binding.recyclerViewRank.adapter = presenter.adapter
        }

        for (i in 0 until list.size) {
            val rank = list[i]
            if (User.instance.rank < rank.score) {
                nextRankText.set(rank.name)
                score.set((rank.score - User.instance.rank).toString())
                break
            }
        }

    }

    lateinit var binding: FragmentVipBinding
    lateinit var presenter: VipPresenter
    val nextRankText = ObservableField<String>()
    val score = ObservableField<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            presenter = VipPresenter(it, this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vip, null, false)
        binding.fragment = this
        binding.user = User.instance
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.getRankList()
    }

}