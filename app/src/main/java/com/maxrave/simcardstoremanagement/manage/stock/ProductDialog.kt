package com.maxrave.simcardstoremanagement.manage.stock

import android.os.Bundle
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
import com.maxrave.simcardstoremanagement.databinding.ProductDialogBinding
import com.maxrave.simcardstoremanagement.model.product.Product
import com.maxrave.simcardstoremanagement.model.product.ProductAdapter

class ProductDialog: DialogFragment() {
    private var _binding: ProductDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var listProduct: ArrayList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProductDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listProduct = ArrayList()
        val db = Firebase.firestore

        binding.topAppBar.setNavigationOnClickListener {
            dismiss()
        }

        db.collection("SanPham").get().addOnSuccessListener { result ->
            for (document in result) {
                val maSP = document.get("MaSP")
                val tenSP = document.get("TenSP")
                val maNhaCC = document.get("MaNhaCC")
                val giaNhap = document.get("GiaNhap")
                val giaBan = document.get("GiaBan")
                val ghiChu = document.get("GhiChu")
                val ID = document.id
                val product = Product(
                    ghiChu.toString(),
                    giaBan.toString().toInt(),
                    giaNhap.toString().toInt(),
                    maNhaCC.toString(),
                    maSP.toString(),
                    tenSP.toString(),
                    ID
                )
                listProduct.add(product)
            }
            binding.rvListProduct.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = ProductAdapter(listProduct, context)
                addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            }
        }.addOnFailureListener {

        }
        binding.btAddProduct.setOnClickListener {
            val alertDialogView = LayoutInflater.from(context).inflate(R.layout.product_add_dialog_layout, null)
            MaterialAlertDialogBuilder(requireContext())
                .setView(alertDialogView)
                .setTitle("Thêm sản phẩm")
                .setPositiveButton("Lưu") { dialog, which ->
                    // Respond to positive button press
                    val productNameEdit = alertDialogView.findViewById<EditText>(R.id.etProductName)
                    val productCodeEdit = alertDialogView.findViewById<EditText>(R.id.etProductCode)
                    val providerCodeEdit = alertDialogView.findViewById<EditText>(R.id.etProviderCode)
                    val sellPriceEdit = alertDialogView.findViewById<EditText>(R.id.etSellPrice)
                    val importPriceEdit = alertDialogView.findViewById<EditText>(R.id.etImportPrice)
                    val noteEdit = alertDialogView.findViewById<EditText>(R.id.etNote)


                    if (productNameEdit.text.toString() != "" && productCodeEdit.text.toString() != "" && providerCodeEdit.text.toString() != "" && sellPriceEdit.text.toString() != "" && importPriceEdit.text.toString() != "") {
                        db.collection("SanPham").add(
                            mapOf(
                                "GhiChu" to noteEdit.text.toString(),
                                "GiaBan" to sellPriceEdit.text.toString(),
                                "GiaNhap" to importPriceEdit.text.toString(),
                                "MaNhaCC" to providerCodeEdit.text.toString(),
                                "MaSP" to productCodeEdit.text.toString(),
                                "TenSP" to productNameEdit.text.toString()
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
        val frg = parentFragmentManager.findFragmentByTag("ProductDialog")
        frgTransaction.beginTransaction().detach(frg!!).commit()
        frgTransaction.beginTransaction().attach(frg).commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}