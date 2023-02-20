package com.maxrave.simcardstoremanagement.manage.sell

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maxrave.simcardstoremanagement.R
import com.maxrave.simcardstoremanagement.databinding.FragmentInvoiceBuyBinding
import com.maxrave.simcardstoremanagement.model.invoice.buyinvoice.BuyInvoice
import com.maxrave.simcardstoremanagement.model.invoice.buyinvoice.BuyInvoiceAdapter


class InvoiceBuyFragment : Fragment() {

    private var _binding: FragmentInvoiceBuyBinding? = null
    private val binding get() = _binding!!
    private lateinit var listBuyInvoice: ArrayList<BuyInvoice>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        listBuyInvoice = ArrayList()
        // Inflate the layout for this fragment
        _binding = FragmentInvoiceBuyBinding.inflate(inflater, container, false)
        val db = Firebase.firestore
        db.collection("HDMuaHang").get().addOnSuccessListener { result ->
            for (document in result) {
                val id = document.id
                val donGia = document.get("DonGia")
                val giamGia = document.get("GiamGia")
                val hinhThucMH = document.get("HinhThucMH")
                val maHDMH = document.get("MaHDMH")
                val maKH = document.get("MaKH")
                val maKM = document.get("MaKM")
                val maNV = document.get("MaNV")
                val maSP = document.get("MaSP")
                val ngayTao = document.get("NgayTao")
                val soLuong = document.get("SoLuong")
                val thanhTien = document.get("ThanhTien")
                val buyInvoice = BuyInvoice(donGia.toString().toInt(), giamGia.toString().toInt(), hinhThucMH.toString(), maHDMH.toString(), maKH.toString(), maKM.toString(), maNV.toString(), maSP.toString(), ngayTao.toString(), soLuong.toString().toInt(), thanhTien.toString().toInt(), id)
                listBuyInvoice.add(buyInvoice)
            }
            val layoutManager = LinearLayoutManager(context)
            val adapter = BuyInvoiceAdapter(listBuyInvoice, requireContext())
            val itemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            val recyclerView = binding.rvListBuyInvoice
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)
            recyclerView.addItemDecoration(itemDecoration)
        }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }


        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        super.onDestroyView()
        _binding = null
    }

}