package com.maxrave.simcardstoremanagement.manage.sell

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout.Tab
import com.google.android.material.tabs.TabLayoutMediator
import com.maxrave.simcardstoremanagement.R
import com.maxrave.simcardstoremanagement.databinding.InvoiceDialogBinding

class InvoiceDialog: DialogFragment() {
    private var _binding: InvoiceDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = InvoiceDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topAppBar.setNavigationOnClickListener {
            dismiss()
        }
        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager
        val tabItem = listOf(
            TabItem(InvoiceBuyFragment(), "Hoá đơn mua hàng"),
            TabItem(InvoiceShipFragment(), "Hoá đơn giao hàng"),
            TabItem(ReceiptFragment(), "Biên lai")
        )
        val tabTitles = tabItem.map { it.title }
        val pagerAdapter = PagerAdapter(requireActivity(), tabItem)
        viewPager.adapter = pagerAdapter
        viewPager.isUserInputEnabled = true
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()



    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class TabItem(val fragment: Fragment, val title: String)