package com.maxrave.simcardstoremanagement.mainFragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maxrave.simcardstoremanagement.R
import com.maxrave.simcardstoremanagement.databinding.FragmentHomeBinding
import com.maxrave.simcardstoremanagement.databinding.FragmentManageBinding
import com.maxrave.simcardstoremanagement.model.user.UserAdapter
import com.maxrave.simcardstoremanagement.model.user.user

class ManageFragment : Fragment() {
    private var _binding : FragmentManageBinding? = null
    private val binding get() = _binding!!
    private lateinit var listUsers: ArrayList<user>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentManageBinding.inflate(inflater, container, false)
        val view = binding.root

        //Quản lý nhân viên
        listUsers = ArrayList()
        var db = Firebase.firestore
        db.collection("NhanVien").get().addOnSuccessListener { result ->
            for (document in result)
            {
                Log.d(TAG, "${document.id} => ${document.data}")
                var avatar = document.get("Avatar")
                var chucVu = document.get("ChucVu")
                var diaChi = document.get("DiaChi")
                var email = document.get("Email")
                var hoNV = document.get("HoNV")
                var luong = document.get("Luong").toString().toInt()
                var maNQL = document.get("MaNQL")
                var maNV = document.get("MaNV")
                var matKhau = document.get("MatKhau")
                var ngaySinh = document.get("NgaySinh")
                var phai = document.get("Phai")
                var sDT = document.get("SDT")
                var tenLot = document.get("TenLot")
                var tenNV = document.get("TenNV")
                var user = user(
                    avatar.toString(),
                    chucVu.toString(),
                    diaChi.toString(),
                    email.toString(),
                    hoNV.toString(),
                    luong,
                    maNQL.toString(),
                    maNV.toString(),
                    matKhau.toString(),
                    ngaySinh.toString(),
                    phai.toString(),
                    sDT.toString(),
                    tenLot.toString(),
                    tenNV.toString()
                )
                listUsers.add(user)
            }
            var recyclerView: RecyclerView = binding.rvListEmployee
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            val adapter = UserAdapter(listUsers)
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)


        }
                .addOnFailureListener { exception ->

                }
        //Quản lý kho
        binding.StockExpandable.visibility = View.GONE
        binding.ivStockExpandDown.visibility = View.VISIBLE
        binding.ivStockExpandDown.setOnClickListener {
            if (binding.StockExpandable.visibility == View.GONE) {
                binding.StockExpandable.visibility = View.VISIBLE
                binding.ivStockExpandDown.visibility = View.GONE
                binding.ivStockExpandUp.visibility = View.VISIBLE
            }
        }
        binding.ivStockExpandUp.setOnClickListener {
            if (binding.StockExpandable.visibility == View.VISIBLE) {
                binding.StockExpandable.visibility = View.GONE
                binding.ivStockExpandDown.visibility = View.VISIBLE
                binding.ivStockExpandUp.visibility = View.GONE
            }
        }

        //Quản lý bán hàng
        binding.SellExpandable.visibility = View.GONE
        binding.ivSellExpandDown.visibility = View.VISIBLE
        binding.ivSellExpandDown.setOnClickListener {
            if (binding.SellExpandable.visibility == View.GONE) {
                binding.SellExpandable.visibility = View.VISIBLE
                binding.ivSellExpandDown.visibility = View.GONE
                binding.ivSellExpandUp.visibility = View.VISIBLE
            }
        }
        binding.ivSellExpandUp.setOnClickListener {
            if (binding.SellExpandable.visibility == View.VISIBLE) {
                binding.SellExpandable.visibility = View.GONE
                binding.ivSellExpandDown.visibility = View.VISIBLE
                binding.ivSellExpandUp.visibility = View.GONE
            }
        }



        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}