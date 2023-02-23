package com.maxrave.simcardstoremanagement.manage.sell

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
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
        updateData()
        binding.fabAddInvoice.setOnClickListener{
            Log.d("Fab", "Fab clicked")
            val alertDialogView = LayoutInflater.from(context).inflate(R.layout.edit_buy_invoice_dialog_layout, null)
            MaterialAlertDialogBuilder(requireContext())
                .setView(alertDialogView)
                .setTitle("Thêm hóa đơn mua hàng")
                .setPositiveButton("Lưu"){ dialog, which ->
                    val maHDMH = alertDialogView.findViewById<TextInputEditText>(R.id.etInvoiceCode)
                    val maKH = alertDialogView.findViewById<TextInputEditText>(R.id.etCustomerCode)
                    val maNV = alertDialogView.findViewById<TextInputEditText>(R.id.etUserCode)
                    val maSP = alertDialogView.findViewById<TextInputEditText>(R.id.etProductCode)
                    val maKM = alertDialogView.findViewById<TextInputEditText>(R.id.etPromotionCode)
                    val soLuong = alertDialogView.findViewById<TextInputEditText>(R.id.etUnit)
                    val donGia = alertDialogView.findViewById<TextInputEditText>(R.id.etPrice)
                    val giamGia = alertDialogView.findViewById<TextInputEditText>(R.id.etPromotionValue)
                    val hinhThucMH = alertDialogView.findViewById<TextInputEditText>(R.id.etForm)
                    val ngayTao = alertDialogView.findViewById<TextInputEditText>(R.id.etDate)
                    val thanhTien = alertDialogView.findViewById<TextInputEditText>(R.id.etTotalValue)

                    if (maHDMH.text.toString().isNotEmpty() && maKH.text.toString().isNotEmpty() && maNV.text.toString().isNotEmpty() && maSP.text.toString().isNotEmpty() && maKM.text.toString().isNotEmpty() && soLuong.text.toString().isNotEmpty() && donGia.text.toString().isNotEmpty() && giamGia.text.toString().isNotEmpty() && hinhThucMH.text.toString().isNotEmpty() && ngayTao.text.toString().isNotEmpty() && thanhTien.text.toString().isNotEmpty()) {
                        db.collection("HDMuaHang").add(
                            hashMapOf(
                                "MaHDMH" to maHDMH.text.toString(),
                                "MaKH" to maKH.text.toString(),
                                "MaNV" to maNV.text.toString(),
                                "MaSP" to maSP.text.toString(),
                                "MaKM" to maKM.text.toString(),
                                "SoLuong" to soLuong.text.toString().toInt(),
                                "DonGia" to donGia.text.toString().toInt(),
                                "GiamGia" to giamGia.text.toString().toInt(),
                                "HinhThucMH" to hinhThucMH.text.toString(),
                                "NgayTao" to ngayTao.text.toString(),
                                "ThanhTien" to thanhTien.text.toString().toInt()
                            )
                        ).addOnSuccessListener { documentReference ->
                            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                            updateData()
                        }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding document", e)
                            }
                    }
                }
                .setNegativeButton("Huỷ"){ dialog, which ->

                }
                .show()
        }

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateData(){
        listBuyInvoice.clear()
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
    }
}