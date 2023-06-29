package com.alibaba.ams.emas.demo.ui.basic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.alibaba.sdk.android.push.CloudPushService
import com.aliyun.emas.pocdemo.R
import com.aliyun.emas.pocdemo.databinding.FragmentBasicBinding


class BasicFragment : Fragment(), IBasicShowDialog {

    private var _binding: FragmentBasicBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: BasicViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProvider(this)[BasicViewModel::class.java]
        viewModel.showDialogCallback = this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBasicBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        val root: View = binding.root
        binding.viewModel = viewModel
        initObserver()

        viewModel.initData()
        viewModel.checkPushStatus()


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initObserver() {
        viewModel.pushChannelOpenedData.observe(viewLifecycleOwner) {
            binding.basicOpenPush.apply {
                visibility = if (it == true) View.GONE else View.VISIBLE
            }
            binding.basicClosePush.apply {
                visibility = if (it == true) View.VISIBLE else View.GONE
            }
        }
    }

    override fun showSetLogDialog(index: Int) {
        val builder = activity?.let { act -> AlertDialog.Builder(act) }
        builder?.apply {
            setTitle(getString(R.string.set_log_level))
            val items = arrayOf("Error", "Info", "Debug", getString(R.string.off_log))
            var logLevel = CloudPushService.LOG_OFF

            setSingleChoiceItems(items, index) { _, which ->
                logLevel = when (which) {
                    0 -> CloudPushService.LOG_ERROR
                    1 -> CloudPushService.LOG_INFO
                    2 -> CloudPushService.LOG_DEBUG
                    else -> {
                        CloudPushService.LOG_OFF
                    }
                }
            }
            setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
                viewModel.saveLogLevel(logLevel)
                dialog.dismiss()
            }
        }
        builder?.show()
    }

    override fun showErrorDialog(message: String) {
        val builder = activity?.let { act -> AlertDialog.Builder(act) }
        builder?.apply {
            setTitle(getString(R.string.tips))
            setMessage(message)
            setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

}