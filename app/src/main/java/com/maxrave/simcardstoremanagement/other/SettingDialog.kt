package com.maxrave.simcardstoremanagement.other

import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.color.DynamicColors
import com.maxrave.simcardstoremanagement.R
import com.maxrave.simcardstoremanagement.databinding.SettingDialogBinding

class SettingDialog: DialogFragment() {
    private var _binding: SettingDialogBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = SettingDialogBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cvExpandableView.visibility = View.GONE
        binding.ivExpandDown.visibility = View.VISIBLE
        binding.ivExpandDown.setOnClickListener {
            if (binding.cvExpandableView.visibility == View.GONE) {
                binding.cvExpandableView.visibility = View.VISIBLE
                binding.ivExpandDown.visibility = View.GONE
                binding.ivExpandUp.visibility = View.VISIBLE
            }
        }
        binding.ivExpandUp.setOnClickListener {
            if (binding.cvExpandableView.visibility == View.VISIBLE) {
                binding.cvExpandableView.visibility = View.GONE
                binding.ivExpandDown.visibility = View.VISIBLE
                binding.ivExpandUp.visibility = View.GONE
            }
        }
        binding.topAppBar.setNavigationOnClickListener {
            dismiss()
        }
        binding.swMaterialYou.isChecked = requireActivity().intent.getStringExtra("MaterialYou") == "true"
        binding.swMaterialYou.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Log.d("MaterialYou", "onViewCreated: ")
                DynamicColors.applyToActivityIfAvailable(requireActivity())
                val intent: Intent = requireActivity().intent
                intent.putExtra("MaterialYou", "true")
                intent.putExtra("Restart Activity", "true")
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                requireActivity().finish()

                startActivity(intent)
            }
            else{
                val intent: Intent = requireActivity().intent
                intent.putExtra("MaterialYou", "false")
                intent.putExtra("Restart Activity", "true")
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                requireActivity().finish()

                startActivity(intent)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogTheme)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}