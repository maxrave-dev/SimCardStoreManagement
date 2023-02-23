package com.maxrave.simcardstoremanagement.manage.sell

import android.content.ContentValues
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
import com.maxrave.simcardstoremanagement.databinding.FragmentReceiptBinding
import com.maxrave.simcardstoremanagement.model.invoice.buyinvoice.BuyInvoice
import com.maxrave.simcardstoremanagement.model.invoice.buyinvoice.BuyInvoiceAdapter
import com.maxrave.simcardstoremanagement.model.invoice.receipt.Receipt
import com.maxrave.simcardstoremanagement.model.invoice.receipt.ReceiptAdapter

class ReceiptFragment : Fragment() {
    private var _binding: FragmentReceiptBinding? = null
    private val binding get() = _binding!!
    private lateinit var listReceipt: ArrayList<Receipt>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentReceiptBinding.inflate(inflater, container, false)

        listReceipt = ArrayList()
        val db = Firebase.firestore
        updateData()
        binding.fabAddReceipt.setOnClickListener {
            val alertDialogView = LayoutInflater.from(context).inflate(R.layout.edit_receipt_dialog_layout, null)
            MaterialAlertDialogBuilder(requireContext())
                .setView(alertDialogView)
                .setTitle("Thêm biên lai")
                .setPositiveButton("Lưu"){ dialog, which ->
                    val maBL = alertDialogView.findViewById<TextInputEditText>(R.id.etReceiptCode)
                    val maHDMH = alertDialogView.findViewById<TextInputEditText>(R.id.etInvoiceCode)
                    val maKH = alertDialogView.findViewById<TextInputEditText>(R.id.etCustomerCode)
                    val maNV = alertDialogView.findViewById<TextInputEditText>(R.id.etUserCode)
                    val ngayTao = alertDialogView.findViewById<TextInputEditText>(R.id.etDate)
                    val sDT = alertDialogView.findViewById<TextInputEditText>(R.id.etPhoneNumber)
                    if (maBL.text.toString() != "" && maHDMH.text.toString() != "" && maKH.text.toString() != "" && maNV.text.toString() != "" && ngayTao.text.toString() != "" && sDT.text.toString() != ""){
                        db.collection("BienLai").add(
                            hashMapOf(
                                "MaBL" to maBL.text.toString(),
                                "MaHDMH" to maHDMH.text.toString(),
                                "MaKH" to maKH.text.toString(),
                                "MaNV" to maNV.text.toString(),
                                "NgayTao" to ngayTao.text.toString(),
                                "SDT" to sDT.text.toString()
                            )
                        ).addOnSuccessListener {
                            Log.d(TAG, "DocumentSnapshot added with ID: ${it.id}")
                            updateData()
                        }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding document", e) }
                    }
                }
                .setNegativeButton("Hủy"){
                        dialog, which ->
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
        listReceipt.clear()
        val db = Firebase.firestore
        db.collection("BienLai").get().addOnSuccessListener { result ->
            for (document in result) {
                val id = document.id
                val maBL = document.get("MaBL")
                val maHDMH = document.get("MaHDMH")
                val maKH = document.get("MaKH")
                val maNV = document.get("MaNV")
                val ngayTao = document.get("NgayTao")
                val sDT = document.get("SDT")
                val receipt = Receipt(maBL.toString(), maHDMH.toString(), maKH.toString(), maNV.toString(), ngayTao.toString(), sDT.toString(), id)
                listReceipt.add(receipt)
            }
            val layoutManager = LinearLayoutManager(context)
            val adapter = ReceiptAdapter(listReceipt, requireContext())
            val itemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            val recyclerView = binding.rvListReceipt
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