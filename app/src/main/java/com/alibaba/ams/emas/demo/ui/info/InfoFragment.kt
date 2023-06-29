package com.alibaba.ams.emas.demo.ui.info

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.alibaba.ams.emas.demo.util.Utils
import com.alibaba.ams.emas.demo.util.Utils.Companion.TAG
import com.alibaba.sdk.android.push.CloudPushService
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory
import com.aliyun.emas.pocdemo.databinding.FragmentInfoBinding

class InfoFragment : Fragment() {

    private var _binding: FragmentInfoBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val infoViewModel =
            ViewModelProvider(this)[InfoViewModel::class.java]

        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        infoViewModel.initData()
        binding.viewModel = infoViewModel

        binding.infoPkgName.text = activity?.packageName


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}