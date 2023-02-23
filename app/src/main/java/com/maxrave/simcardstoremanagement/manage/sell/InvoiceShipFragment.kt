package com.maxrave.simcardstoremanagement.manage.sell

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maxrave.simcardstoremanagement.R
import com.maxrave.simcardstoremanagement.databinding.FragmentInvoiceShipBinding
import com.maxrave.simcardstoremanagement.model.invoice.shipinvoice.ShipInvoice
import com.maxrave.simcardstoremanagement.model.invoice.shipinvoice.ShipInvoiceAdapter


class InvoiceShipFragment : Fragment() {
    private var _binding: FragmentInvoiceShipBinding? = null
    private val binding get() = _binding!!
    private lateinit var listShipInvoice : ArrayList<ShipInvoice>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val db = Firebase.firestore
        // Inflate the layout for this fragment
        _binding = FragmentInvoiceShipBinding.inflate(inflater, container, false)
        listShipInvoice = ArrayList()
        updateData()
        binding.fabAddInvoice.setOnClickListener {
            val alertDialogView = LayoutInflater.from(context).inflate(R.layout.edit_ship_invoice_dialog_layout, null)
            MaterialAlertDialogBuilder(requireContext())
                .setView(alertDialogView)
                .setTitle("Thêm hóa đơn giao hàng")
                .setPositiveButton("Lưu"){dialog, which ->
                    val maBL = alertDialogView.findViewById<EditText>(R.id.etReceiptCode)
                    val tenKH = alertDialogView.findViewById<EditText>(R.id.etCustomerName)
                    val diaChi = alertDialogView.findViewById<EditText>(R.id.etAddress)
                    val sDT = alertDialogView.findViewById<EditText>(R.id.etPhoneNumber)
                    val cOD = alertDialogView.findViewById<EditText>(R.id.etCOD)
                    val ngayGiao = alertDialogView.findViewById<EditText>(R.id.etDate)
                    val dVVC = alertDialogView.findViewById<EditText>(R.id.etShipService)

                    if (maBL.text.toString().isNotEmpty() && tenKH.text.toString().isNotEmpty() && diaChi.text.toString().isNotEmpty() && sDT.text.toString().isNotEmpty() && cOD.text.toString().isNotEmpty() && ngayGiao.text.toString().isNotEmpty() && dVVC.text.toString().isNotEmpty()){
                        db.collection("HDGiaoHang").add(
                            hashMapOf(
                                "MaBL" to maBL.text.toString(),
                                "TenKH" to tenKH.text.toString(),
                                "DiaChi" to diaChi.text.toString(),
                                "SDT" to sDT.text.toString(),
                                "GiaTienCOD" to cOD.text.toString().toInt(),
                                "NgayGiao" to ngayGiao.text.toString(),
                                "DVVC" to dVVC.text.toString()
                            )
                        ).addOnSuccessListener {
                            Log.d(TAG, "DocumentSnapshot added with ID: ${it.id}")
                            updateData()
                        }.addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }
                    }
                }.setNegativeButton("Hủy"){dialog, which ->  }.show()
        }
        return binding.root
    }

    private fun updateData() {
        val db = Firebase.firestore
        listShipInvoice.clear()
        db.collection("HDGiaoHang").get().addOnSuccessListener {result ->
            for (document in result){
                val dVVC = document.get("DVVC")
                val diaChi = document.get("DiaChi")
                val giaTienCOD = document.get("GiaTienCOD")
                val maBL = document.get("MaBL")
                val ngayGiao = document.get("NgayGiao")
                val sDT = document.get("SDT")
                val tenKH = document.get("TenKH")
                val id = document.id
                val shipInvoice = ShipInvoice(dVVC.toString(), diaChi.toString(), giaTienCOD.toString().toInt(), maBL.toString(), ngayGiao.toString(), sDT.toString(), tenKH.toString(), id)
                listShipInvoice.add(shipInvoice)
            }
            val layoutManager = LinearLayoutManager(context)
            val adapter = ShipInvoiceAdapter(listShipInvoice, requireContext())
            val itemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            val recyclerView = binding.rvListShipInvoice
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
            recyclerView.addItemDecoration(itemDecoration)
            recyclerView.setHasFixedSize(true)
        }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception) }
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
}