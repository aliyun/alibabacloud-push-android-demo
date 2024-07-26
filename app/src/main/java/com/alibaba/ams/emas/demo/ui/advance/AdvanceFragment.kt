package com.alibaba.ams.emas.demo.ui.advance

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.alibaba.ams.emas.demo.ui.IShowDialog
import com.alibaba.sdk.android.push.CloudPushService
import com.alibaba.sdk.android.push.CommonCallback
//import com.alibaba.sdk.android.tool.NetworkUtils
import com.aliyun.emas.pocdemo.R
import com.aliyun.emas.pocdemo.databinding.FragmentAdvanceBinding
import com.google.android.material.chip.Chip


class AdvanceFragment : Fragment(), IShowDialog {

    private var _binding: FragmentAdvanceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: AdvanceViewModel

    private val currentAliasList: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[AdvanceViewModel::class.java]
        viewModel.showDialogCallback = this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentAdvanceBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initObserver()

        viewModel.initData()

        binding.advanceAddDeviceTag.setOnClickListener {
            addDeviceTag()
        }

        binding.advanceBindAccount.setOnClickListener {
            bindAccount()
        }

        binding.advanceAddAccountTag.setOnClickListener {
            addAccountTag()
        }

        binding.advanceAddAlias.setOnClickListener {
            addAlias()
        }

        binding.advanceAddAliasTag.setOnClickListener {
            showChooseAliasDialog()
        }

        binding.advanceBindPhone.setOnClickListener {
            bindPhoneNumber()
        }

        binding.advanceBoundAccount.setOnCloseIconClickListener {
//            if (!NetworkUtils.isNetworkConnected(activity)) {
//                toast(getString(R.string.network_not_connect))
//            } else {
                viewModel.unbindAccount()
//            }
        }

        binding.advanceBoundPhone.setOnCloseIconClickListener {
//            if (!NetworkUtils.isNetworkConnected(activity)) {
//                toast(getString(R.string.network_not_connect))
//            } else {
                viewModel.unbindPhone()
//            }
        }
        return root
    }

    private fun initObserver() {
        viewModel.deviceTagList.observe(viewLifecycleOwner) {
            it?.let {
                val tagList = it.split(",")
                tagList.reversed().forEach { deviceTag ->
                    val chip: Chip = LayoutInflater.from(activity)
                        .inflate(R.layout.chip_close_item, null) as Chip
                    chip.text = deviceTag
                    chip.setOnCloseIconClickListener {
                        unbindDeviceTag(deviceTag, chip)
                    }
                    binding.advanceDeviceTagContainer.addView(chip, 0)
                }
            }
        }

        viewModel.accountTagList.observe(viewLifecycleOwner) {
            it?.let { list ->
                repeat(list.size) { index ->
                    val chip: Chip = LayoutInflater.from(activity)
                        .inflate(R.layout.chip_close_item, null) as Chip
                    chip.text = list[index]
                    chip.setOnCloseIconClickListener {
                        unbindAccountTag(list[index], chip)
                    }
                    binding.advanceAccountTagContainer.addView(chip, 0)
                }
            }
        }

        viewModel.aliasTagList.observe(viewLifecycleOwner) {
            it?.let { list ->
                repeat(list.size) { index ->
                    val chip: Chip = LayoutInflater.from(activity)
                        .inflate(R.layout.chip_close_item, null) as Chip
                    chip.text = list[index]
                    chip.setOnCloseIconClickListener {
                        unbindAccountTag(list[index], chip)
                    }
                    binding.advanceAliasTagContainer.addView(chip, 0)
                }
            }
        }


        viewModel.aliasList.observe(viewLifecycleOwner) {
            it?.let {
                val aliasList = it.split(",")
                aliasList.reversed().forEach { alias ->
                    currentAliasList.add(alias)
                    val chip: Chip = LayoutInflater.from(activity)
                        .inflate(R.layout.chip_close_item, null) as Chip
                    chip.text = alias
                    chip.setOnCloseIconClickListener {
                        removeAlias(alias, chip)
                    }
                    binding.advanceAliasContainer.addView(chip, 0)
                }
            }
        }

        viewModel.boundDeviceTag.observe(viewLifecycleOwner) { boundTag ->
            boundTag?.let {
                val chip: Chip = LayoutInflater.from(activity)
                    .inflate(R.layout.chip_close_item, null) as Chip
                chip.text = it
                chip.setOnCloseIconClickListener {
                    unbindDeviceTag(boundTag, chip)
                }
                binding.advanceDeviceTagContainer.addView(chip, 0)
            }
        }

        viewModel.boundAccount.observe(viewLifecycleOwner) { boundAccount ->
            boundAccount?.let {
                binding.advanceBoundAccount.visibility = if (it == "") View.GONE else View.VISIBLE
                binding.advanceBoundAccount.text = it
            }
        }

        viewModel.boundAccountTag.observe(viewLifecycleOwner) { boundTag ->
            boundTag?.let {
                val chip: Chip = LayoutInflater.from(activity)
                    .inflate(R.layout.chip_close_item, null) as Chip
                chip.text = it
                chip.setOnCloseIconClickListener {
                    unbindAccountTag(boundTag, chip)
                }
                binding.advanceAccountTagContainer.addView(chip, 0)
            }
        }


        viewModel.addedAlias.observe(viewLifecycleOwner) { addedAlias ->
            addedAlias?.let {
                currentAliasList.add(it)
                val chip: Chip = LayoutInflater.from(activity)
                    .inflate(R.layout.chip_close_item, null) as Chip
                chip.text = it
                chip.setOnCloseIconClickListener {
                    removeAlias(addedAlias, chip)
                }
                binding.advanceAliasContainer.addView(chip, 0)
            }
        }

        viewModel.boundAliasTag.observe(viewLifecycleOwner) { aliasTag ->
            aliasTag?.let {
                val tmp = it.split("|")
                val tag = tmp[0]
                val alias = tmp[1]
                val chip: Chip = LayoutInflater.from(activity)
                    .inflate(R.layout.chip_close_item, null) as Chip
                chip.text = tag
                chip.setOnCloseIconClickListener {
                    unbindAliasTag(tag, alias, chip)
                }
                binding.advanceAliasTagContainer.addView(chip, 0)
            }
        }

        viewModel.boundPhone.observe(viewLifecycleOwner) { boundPhone ->
            boundPhone?.let {
                binding.advanceBoundPhone.visibility = if (it == "") View.GONE else View.VISIBLE
                binding.advanceBoundPhone.text = it
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.showDialogCallback = null
    }

    private fun bindAccount() {
//        if (!NetworkUtils.isNetworkConnected(activity)) {
//            toast(getString(R.string.network_not_connect))
//            return
//        }
        val input = LayoutInflater.from(activity).inflate(R.layout.dialog_input, null)
        val editText = input.findViewById<AppCompatEditText>(R.id.add_input)
        editText.hint = getString(R.string.account_hint)

        val builder = activity?.let { act -> AlertDialog.Builder(act) }
        builder?.apply {
            setTitle(R.string.bind_account)
            setView(input)
            setPositiveButton(R.string.confirm) { dialog, _ ->
                when (val account = editText.text.toString()) {
                    "" -> Toast.makeText(activity, R.string.account_empty, Toast.LENGTH_SHORT)
                        .show()
                    else -> viewModel.bindAccount(account)
                }
                dialog.dismiss()
            }
            setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    private fun addTag(target: Int) {
//        if (!NetworkUtils.isNetworkConnected(activity)) {
//            toast(getString(R.string.network_not_connect))
//            return
//        }
        val input = LayoutInflater.from(activity).inflate(R.layout.dialog_input, null)
        val editText = input.findViewById<AppCompatEditText>(R.id.add_input)
        editText.hint = getString(R.string.tag_hint)

        val title = when (target) {
            CloudPushService.ACCOUNT_TARGET -> getString(R.string.add_account_tag)
            CloudPushService.ALIAS_TARGET -> getString(R.string.add_alias_tag)
            else -> getString(R.string.add_device_tag)
        }

        val builder = activity?.let { act -> AlertDialog.Builder(act) }
        builder?.apply {
            setTitle(title)
            setView(input)
            setPositiveButton(R.string.confirm) { dialog, _ ->
                when (val deviceTag = editText.text.toString()) {
                    "" -> Toast.makeText(activity, R.string.tag_is_empty, Toast.LENGTH_SHORT).show()
                    else -> viewModel.bindTag(target, deviceTag, null)
                }
                dialog.dismiss()
            }
            setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    private fun addDeviceTag() {
        addTag(CloudPushService.DEVICE_TARGET)
    }

    private fun addAccountTag() {
        addTag(CloudPushService.ACCOUNT_TARGET)
    }

    private fun showChooseAliasDialog() {
        if (currentAliasList.isEmpty()) {
            showErrorDialog(getString(R.string.add_alias_first))
            return
        }
        var alias: String = currentAliasList[0]
        val builder = activity?.let { act -> AlertDialog.Builder(act) }
        builder?.apply {
            setTitle(getString(R.string.choose_alias))
            setSingleChoiceItems(currentAliasList.toTypedArray(), 0) { _, which ->
                alias = currentAliasList[which]
            }
            setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
                dialog.dismiss()
                addAliasTag(alias)
            }
            setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
        }
        builder?.show()
    }

    private fun addAliasTag(alias: String) {
//        if (!NetworkUtils.isNetworkConnected(activity)) {
//            toast(getString(R.string.network_not_connect))
//            return
//        }
        val input = LayoutInflater.from(activity).inflate(R.layout.dialog_input, null)
        val editText = input.findViewById<AppCompatEditText>(R.id.add_input)
        editText.hint = getString(R.string.tag_hint)

        val builder = activity?.let { act -> AlertDialog.Builder(act) }
        builder?.apply {
            setTitle(getString(R.string.add_alias_tag))
            setView(input)
            setPositiveButton(R.string.confirm) { dialog, _ ->
                when (val aliasTag = editText.text.toString()) {
                    "" -> Toast.makeText(activity, R.string.tag_is_empty, Toast.LENGTH_SHORT).show()
                    else -> viewModel.bindTag(CloudPushService.ALIAS_TARGET, aliasTag, alias)
                }
                dialog.dismiss()
            }
            setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    private fun unbindDeviceTag(tag: String, view: View) {
//        if (!NetworkUtils.isNetworkConnected(activity)) {
//            toast(getString(R.string.network_not_connect))
//            return
//        }
        viewModel.removeDeviceTag(tag, object : CommonCallback {
            override fun onSuccess(response: String?) {
                //remove view
                binding.advanceDeviceTagContainer.removeView(view)
            }

            override fun onFailed(errorCode: String?, errorMsg: String?) {
                val message = getString(
                    R.string.common_error,
                    getString(R.string.remove_tag),
                    "$errorCode - $errorMsg"
                )
                showErrorDialog(message)
            }
        })
    }

    private fun unbindAccountTag(tag: String, view: View) {
//        if (!NetworkUtils.isNetworkConnected(activity)) {
//            toast(getString(R.string.network_not_connect))
//            return
//        }
        viewModel.removeAccountTag(tag, object : CommonCallback {
            override fun onSuccess(response: String?) {
                //remove view
                binding.advanceAccountTagContainer.removeView(view)
            }

            override fun onFailed(errorCode: String?, errorMsg: String?) {
                val message = getString(
                    R.string.common_error,
                    getString(R.string.remove_tag),
                    "$errorCode - $errorMsg"
                )
                showErrorDialog(message)
            }
        })
    }

    private fun unbindAliasTag(tag: String, alias: String, view: View) {
//        if (!NetworkUtils.isNetworkConnected(activity)) {
//            toast(getString(R.string.network_not_connect))
//            return
//        }
        viewModel.removeAliasTag(tag, alias, object : CommonCallback {
            override fun onSuccess(response: String?) {
                //remove view
                binding.advanceAliasTagContainer.removeView(view)
            }

            override fun onFailed(errorCode: String?, errorMsg: String?) {
                val message = getString(
                    R.string.common_error,
                    getString(R.string.remove_tag),
                    "$errorCode - $errorMsg"
                )
                showErrorDialog(message)
            }
        })
    }

    private fun addAlias() {

//        if (!NetworkUtils.isNetworkConnected(activity)) {
//            toast(getString(R.string.network_not_connect))
//            return
//        }

        val input = LayoutInflater.from(activity).inflate(R.layout.dialog_input, null)
        val editText = input.findViewById<AppCompatEditText>(R.id.add_input)
        editText.hint = getString(R.string.alis_hint)

        val builder = activity?.let { act -> AlertDialog.Builder(act) }
        builder?.apply {
            setTitle(R.string.add_alias)
            setView(input)
            setPositiveButton(R.string.confirm) { dialog, _ ->
                when (val alias = editText.text.toString()) {
                    "" -> Toast.makeText(activity, R.string.alis_is_empty, Toast.LENGTH_SHORT)
                        .show()
                    else -> viewModel.addAlias(alias)
                }
                dialog.dismiss()
            }
            setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    private fun removeAlias(alias: String, view: View) {
//        if (!NetworkUtils.isNetworkConnected(activity)) {
//            toast(getString(R.string.network_not_connect))
//            return
//        }
        viewModel.removeAlias(
            alias,
            object : CommonCallback {
                override fun onSuccess(response: String?) {
                    //remove view
                    binding.advanceAliasContainer.removeView(view)
                    currentAliasList.remove(alias)
                }

                override fun onFailed(errorCode: String?, errorMsg: String?) {
                    val message = getString(
                        R.string.common_error,
                        getString(R.string.remove_alias),
                        "$errorCode - $errorMsg"
                    )
                    showErrorDialog(message)
                }
            })
    }


    private fun bindPhoneNumber() {
//        if (!NetworkUtils.isNetworkConnected(activity)) {
//            toast(getString(R.string.network_not_connect))
//            return
//        }
        val input = LayoutInflater.from(activity).inflate(R.layout.dialog_input, null)
        val editText = input.findViewById<AppCompatEditText>(R.id.add_input)
        editText.hint = getString(R.string.phone_hint)
        editText.inputType = EditorInfo.TYPE_CLASS_NUMBER

        val builder = activity?.let { act -> AlertDialog.Builder(act) }
        builder?.apply {
            setTitle(R.string.bind_phone)
            setView(input)
            setPositiveButton(R.string.confirm) { dialog, _ ->
                when (val phone = editText.text.toString()) {
                    "" -> Toast.makeText(activity, R.string.phone_empty, Toast.LENGTH_SHORT)
                        .show()
                    else -> viewModel.bindPhoneNumber(phone)
                }
                dialog.dismiss()
            }
            setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    private fun toast(message: String) {
        activity?.let {
            val toast = Toast.makeText(
                activity,
                message,
                Toast.LENGTH_SHORT
            )
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }

    override fun showErrorDialog(message: String) {
        val builder = activity?.let { act -> AlertDialog.Builder(act) }
        builder?.apply {
            setTitle(R.string.tips)
            setMessage(message)
            setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }


}