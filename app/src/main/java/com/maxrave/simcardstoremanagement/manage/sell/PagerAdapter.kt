package com.maxrave.simcardstoremanagement.manage.sell

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(activity: FragmentActivity?, val listItems: List<TabItem>) : FragmentStateAdapter(activity!!){

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> InvoiceBuyFragment()
            1 -> InvoiceShipFragment()
            else -> InvoiceBuyFragment()
        }
    }

}