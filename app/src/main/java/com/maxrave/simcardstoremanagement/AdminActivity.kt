package com.maxrave.simcardstoremanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import coil.load
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.color.DynamicColors
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maxrave.simcardstoremanagement.databinding.ActivityAdminBinding
import com.maxrave.simcardstoremanagement.mainFragment.HomeFragment
import com.maxrave.simcardstoremanagement.mainFragment.ManageFragment
import com.maxrave.simcardstoremanagement.mainFragment.NotificationFragment
import com.maxrave.simcardstoremanagement.other.AccountDialog
import com.maxrave.simcardstoremanagement.other.SettingDialog

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    private lateinit var fm: FragmentManager

    private lateinit var currentUserBundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.getStringExtra("Restart Setting") == "true") {
            if(intent.getStringExtra("MaterialYou") == "true")
            {
                DynamicColors.applyToActivityIfAvailable(this)
            }
            val settingDialog = SettingDialog()
            settingDialog.show(supportFragmentManager, "SettingDialog")
        }
        binding = ActivityAdminBinding.inflate(layoutInflater)
        val botomNav = binding.bottomNavigation
        val topAppBar = binding.topAppBar
        fm = supportFragmentManager
        val homeFragment = HomeFragment()
        val manageFragment = ManageFragment()
        val notificationFragment = NotificationFragment()
        var activeFragment: Fragment = homeFragment

        val user = Firebase.auth.currentUser
        val emailCurrent = user?.email

        currentUserBundle = Bundle()

        val db = Firebase.firestore
        val userRef = db.collection("NhanVien").whereEqualTo("Email", emailCurrent)
        userRef.get().addOnSuccessListener { result ->
            for (document in result)
            {
                val avatar = document.get("Avatar")
                val chucVu = document.get("ChucVu")
                val diaChi = document.get("DiaChi")
                val email = document.get("Email")
                val hoNV = document.get("HoNV")
                val luong = document.get("Luong").toString().toInt()
                val maNQL = document.get("MaNQL")
                val maNV = document.get("MaNV")
                val matKhau = document.get("MatKhau")
                val ngaySinh = document.get("NgaySinh")
                val phai = document.get("Phai")
                val sDT = document.get("SDT")
                val tenLot = document.get("TenLot")
                val tenNV = document.get("TenNV")
                val id = document.id

                currentUserBundle.putString("Avatar", avatar.toString())
                currentUserBundle.putString("ChucVu", chucVu.toString())
                currentUserBundle.putString("DiaChi", diaChi.toString())
                currentUserBundle.putString("Email", email.toString())
                currentUserBundle.putString("HoNV", hoNV.toString())
                currentUserBundle.putString("Luong", luong.toString())
                currentUserBundle.putString("MaNQL", maNQL.toString())
                currentUserBundle.putString("MaNV", maNV.toString())
                currentUserBundle.putString("MatKhau", matKhau.toString())
                currentUserBundle.putString("NgaySinh", ngaySinh.toString())
                currentUserBundle.putString("Phai", phai.toString())
                currentUserBundle.putString("SDT", sDT.toString())
                currentUserBundle.putString("TenLot", tenLot.toString())
                currentUserBundle.putString("TenNV", tenNV.toString())
                currentUserBundle.putString("ID", id.toString())
            }
        }



        fm.beginTransaction()
            .add(R.id.flFragment, homeFragment, "HomeFragment")
            .add(R.id.flFragment, manageFragment, "ManageFragment").hide(manageFragment)
            .add(R.id.flFragment, notificationFragment, "NotificationFragment").hide(notificationFragment).commit()

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
                    var accountDialog = AccountDialog()
                    accountDialog.arguments = currentUserBundle
                    accountDialog.show(supportFragmentManager, "AccountDialog")
                    true
                }
                else -> false
            }
        }
        topAppBar.setNavigationOnClickListener {
            val settingDialog = SettingDialog()
            settingDialog.show(supportFragmentManager, "SettingDialog")
        }


        val view = binding.root
        setContentView(view)
    }
}