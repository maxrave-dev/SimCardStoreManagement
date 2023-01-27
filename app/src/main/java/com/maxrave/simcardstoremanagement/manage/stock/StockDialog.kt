package com.maxrave.simcardstoremanagement.manage.stock

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maxrave.simcardstoremanagement.R
import com.maxrave.simcardstoremanagement.databinding.StockDialogBinding
import com.maxrave.simcardstoremanagement.model.stock.Stock
import com.maxrave.simcardstoremanagement.model.stock.StockAdapter


public class StockDialog : DialogFragment() {
    private var _binding: StockDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var listStock: ArrayList<Stock>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = StockDialogBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listStock = ArrayList()
        val db = Firebase.firestore

        db.collection("Kho").get().addOnSuccessListener { result ->
            for (document in result) {
                val diaChi = document.get("DiaChi")
                val maKho = document.get("MaKho")
                val tenKho = document.get("TenKho")
                val ID = document.id
                val stock = Stock(diaChi.toString(), maKho.toString(), tenKho.toString(), ID.toString())
                listStock.add(stock)
                val layoutManager = LinearLayoutManager(context)
                val adapter = StockAdapter(listStock, requireContext())
                val itemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
                val recyclerView = binding.rvListStock
                recyclerView.layoutManager = layoutManager
                recyclerView.adapter = adapter
                recyclerView.addItemDecoration(itemDecoration)
                recyclerView.setHasFixedSize(true)
            }
        }.addOnFailureListener { exception ->
            Log.d("Error getting documents: ", exception.toString())
        }

        binding.topAppBar.setNavigationOnClickListener {
            dismiss()
        }

        binding.btAddStock.setOnClickListener {
            val alertDialogView = LayoutInflater.from(context).inflate(R.layout.edit_stock_dialog_layout, null)
            MaterialAlertDialogBuilder(requireContext())
                .setView(alertDialogView)
                .setTitle("Thêm kho")
                .setPositiveButton("Lưu") { dialog, which ->
                    // Respond to positive button press
                    val stockNameEdit = alertDialogView.findViewById<EditText>(R.id.etStockName)
                    val stockAddressEdit = alertDialogView.findViewById<EditText>(R.id.etAddress)
                    val stockCodeEdit = alertDialogView.findViewById<EditText>(R.id.etStockCode)

                    if (stockNameEdit.text.toString() != "" && stockAddressEdit.text.toString() != "" && stockCodeEdit.text.toString() != "") {
                        db.collection("Kho").add(
                            mapOf(
                                "TenKho" to stockNameEdit.text.toString(),
                                "DiaChi" to stockAddressEdit.text.toString(),
                                "MaKho" to stockCodeEdit.text.toString()
                            )
                        ).addOnSuccessListener {
                            Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show()
                            reloadPage()
                        }.addOnFailureListener {
                            Toast.makeText(context, "Thêm thất bại", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Huỷ") { dialog, which ->
                    // Respond to negative button press
                }
                .show()
        }



        binding.refreshLayout.setOnRefreshListener {
            reloadPage()
            binding.refreshLayout.isRefreshing = false
        }
    }

    private fun reloadPage(){
        val frgTransaction = parentFragmentManager
        val frg = parentFragmentManager.findFragmentByTag("StockDialog")
        frgTransaction.beginTransaction().detach(frg!!).commit()
        frgTransaction.beginTransaction().attach(frg).commit()
    }

}