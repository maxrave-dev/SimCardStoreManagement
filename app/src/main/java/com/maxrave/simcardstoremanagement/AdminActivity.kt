package com.maxrave.simcardstoremanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maxrave.simcardstoremanagement.databinding.ActivityAdminBinding
import com.maxrave.simcardstoremanagement.mainFragment.HomeFragment
import com.maxrave.simcardstoremanagement.mainFragment.ManageFragment
import com.maxrave.simcardstoremanagement.mainFragment.NotificationFragment

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    private lateinit var fm: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        val botomNav = binding.bottomNavigation
        val topAppBar = binding.topAppBar
        fm = supportFragmentManager
        var homeFragment = HomeFragment()
        var manageFragment = ManageFragment()
        var notificationFragment = NotificationFragment()
        var activeFragment: Fragment = homeFragment






        fm.beginTransaction()
            .add(R.id.flFragment, homeFragment, "1")
            .add(R.id.flFragment, manageFragment, "2").hide(manageFragment)
            .add(R.id.flFragment, notificationFragment, "3").hide(notificationFragment).commit()

        botomNav.setOnItemSelectedListener {
            item ->
            when (item.itemId) {
                R.id.itHome -> {
                    fm.beginTransaction().hide(activeFragment).show(homeFragment).commit()
                    activeFragment = homeFragment
                    true
                }
                R.id.itManage -> {
                    fm.beginTransaction().hide(activeFragment).show(manageFragment).commit()
                    activeFragment = manageFragment
                    true
                }
                R.id.itNotification -> {
                    fm.beginTransaction().hide(activeFragment).show(notificationFragment).commit()
                    activeFragment = notificationFragment
                    true
                }
                else -> false
            }
        }
        topAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.itAccount -> {
                    Toast.makeText(this, "Account", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        val view = binding.root
        setContentView(view)
    }
}