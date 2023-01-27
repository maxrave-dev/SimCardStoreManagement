package com.maxrave.simcardstoremanagement.mainFragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maxrave.simcardstoremanagement.databinding.FragmentManageBinding
import com.maxrave.simcardstoremanagement.manage.stock.ProviderDialog
import com.maxrave.simcardstoremanagement.manage.stock.StockDialog
import com.maxrave.simcardstoremanagement.model.user.UserAdapter
import com.maxrave.simcardstoremanagement.model.user.user
import com.maxrave.simcardstoremanagement.other.AddUserDialog

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

        binding.linearProgressIndicator.visibility = View.VISIBLE
        //Quản lý nhân viên
        listUsers = ArrayList()
        var db = Firebase.firestore
        db.collection("NhanVien").get().addOnSuccessListener { result ->
            for (document in result)
            {
                Log.d(TAG, "${document.id} => ${document.data}")
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
                val user = user(
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
            val recyclerView: RecyclerView = binding.rvListEmployee
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            val adapter = UserAdapter(listUsers, requireActivity())
            recyclerView.adapter = adapter
            val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            recyclerView.addItemDecoration(decoration)
            recyclerView.setHasFixedSize(true)
            binding.linearProgressIndicator.visibility = View.GONE
        }
                .addOnFailureListener { exception ->

                }
        binding.btAddUser.setOnClickListener{
            val dialog = AddUserDialog()
            dialog.show(requireActivity().supportFragmentManager, "AddUserDialog")

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

            binding.btStockManage.setOnClickListener {
                Log.d("Click", "Kho")
                val stockDialog = StockDialog()
                stockDialog.show(requireActivity().supportFragmentManager, "StockDialog")

            }
            binding.btProviderManage.setOnClickListener {
                Log.d("Click", "Nhà cung cấp")
                val providerDialog = ProviderDialog()
                providerDialog.show(requireActivity().supportFragmentManager, "ProviderDialog")
            }
            binding.btArchiveManage.setOnClickListener {
                Log.d("Click", "Lưu trữ")
            }
            binding.btProductManage.setOnClickListener {
                Log.d("Click", "Sản phẩm")
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
        if (binding.SellExpandable.visibility == View.VISIBLE) {
            binding.btCustomerManage.setOnClickListener {
                Log.d("Click", "Click")
            }
            binding.btPromotionManage.setOnClickListener {
                Log.d("Click", "Click")
            }
            binding.btBillManage.setOnClickListener {
                Log.d("Click", "Click")
            }
        }

        binding.refreshSwipe.setOnRefreshListener {
             reloadPage()

            binding.refreshSwipe.isRefreshing = false
        }


        return view
    }

    private fun reloadPage() {
        val frgTransaction = parentFragmentManager
        val frg = parentFragmentManager.findFragmentByTag("ManageFragment")
        frgTransaction.beginTransaction().detach(frg!!).commit()
        frgTransaction.beginTransaction().attach(frg).commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}