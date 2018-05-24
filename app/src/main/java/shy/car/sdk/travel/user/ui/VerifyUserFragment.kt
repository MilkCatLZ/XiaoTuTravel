package shy.car.sdk.travel.user.ui

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import shy.car.sdk.R
import shy.car.sdk.app.base.XTBaseFragment
import shy.car.sdk.databinding.FragmentVerifyUserBinding


/**
 * create by LZ at 2018/05/24
 * 验证用户
 */
class VerifyUserFragment : XTBaseFragment() {

    lateinit var binding: FragmentVerifyUserBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_verify_user, null, false);
        return binding.root
    }
}