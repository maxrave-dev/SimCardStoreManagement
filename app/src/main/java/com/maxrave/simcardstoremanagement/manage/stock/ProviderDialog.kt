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
import com.maxrave.simcardstoremanagement.databinding.ProviderDialogBinding
import com.maxrave.simcardstoremanagement.model.provider.Provider
import com.maxrave.simcardstoremanagement.model.provider.ProviderAdapter

class ProviderDialog: DialogFragment() {
    private var _binding: ProviderDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var listProvider: ArrayList<Provider>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProviderDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listProvider = ArrayList()
        val db = Firebase.firestore

        binding.topAppBar.setNavigationOnClickListener {
            dismiss()
        }

        db.collection("NhaCungCap").get().addOnSuccessListener { result ->
            for (document in result) {
                val diaChi = document.get("DiaChi")
                val maNCC = document.get("MaNhaCC")
                val tenNCC = document.get("TenNhaCC")
                val sDT = document.get("SDT")
                val ID = document.id
                val provider = Provider(diaChi.toString(), maNCC.toString(), sDT.toString(),tenNCC.toString(), ID)
                listProvider.add(provider)
            }
            binding.rvListProvider.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = ProviderAdapter(listProvider, context)
                addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            }
        }.addOnFailureListener {

        }

        binding.btAddProvider.setOnClickListener {
            val alertDialogView = LayoutInflater.from(context).inflate(R.layout.edit_provider_dialog_layout, null)
            MaterialAlertDialogBuilder(requireContext())
                .setView(alertDialogView)
                .setTitle("Thêm Nhà cung cấp")
                .setPositiveButton("Lưu") { dialog, which ->
                    // Respond to positive button press
                    val providerNameEdit = alertDialogView.findViewById<EditText>(R.id.etProviderName)
                    val providerAddressEdit = alertDialogView.findViewById<EditText>(R.id.etAddress)
                    val providerCodeEdit = alertDialogView.findViewById<EditText>(R.id.etProviderCode)
                    val providerPhoneNumber = alertDialogView.findViewById<EditText>(R.id.etProviderPhoneNumber)

                    if (providerNameEdit.text.toString() != "" && providerAddressEdit.text.toString() != "" && providerCodeEdit.text.toString() != "" && providerPhoneNumber.text.toString() != "") {
                        db.collection("NhaCungCap").add(
                            mapOf(
                                "TenNhaCC" to providerNameEdit.text.toString(),
                                "DiaChi" to providerAddressEdit.text.toString(),
                                "MaNhaCC" to providerCodeEdit.text.toString(),
                                "SDT" to providerPhoneNumber.text.toString()
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
        val frg = parentFragmentManager.findFragmentByTag("ProviderDialog")
        frgTransaction.beginTransaction().detach(frg!!).commit()
        frgTransaction.beginTransaction().attach(frg).commit()
    }
}