package com.maxrave.simcardstoremanagement.manage.sell

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maxrave.simcardstoremanagement.R
import com.maxrave.simcardstoremanagement.databinding.CustomerDialogBinding
import com.maxrave.simcardstoremanagement.model.customer.Customer
import com.maxrave.simcardstoremanagement.model.customer.CustomerAdapter

class CustomerDialog: DialogFragment() {
    private var _binding: CustomerDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var listCustomer: ArrayList<Customer>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = CustomerDialogBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listCustomer = ArrayList()
        val db = Firebase.firestore

        db.collection("KhachHang").get().addOnSuccessListener { result ->
            for (document in result) {
                val diaChi = document.get("DiaChi")
                val maKH = document.get("MaKH")
                val tenKH = document.get("TenKH")
                val sDT = document.get("SDT")
                val ID = document.id
                val customer = Customer(diaChi.toString(), maKH.toString(), sDT.toString(), tenKH.toString(), ID)
                listCustomer.add(customer)
            }
            val layoutManager = LinearLayoutManager(context)
            val adapter = CustomerAdapter(listCustomer, requireContext())
            val itemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            val recyclerView = binding.rvListCustomer
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
            recyclerView.addItemDecoration(itemDecoration)
            recyclerView.setHasFixedSize(true)
        }.addOnFailureListener { exception ->
            Log.d("Error getting documents: ", exception.toString())
        }

        binding.topAppBar.setNavigationOnClickListener {
            dismiss()
        }

        binding.btAddCustomer.setOnClickListener {
            val alertDialogView = LayoutInflater.from(context).inflate(R.layout.edit_customer_dialog_layout, null)
            MaterialAlertDialogBuilder(requireContext())
                .setView(alertDialogView)
                .setTitle("Thêm Khách hàng")
                .setPositiveButton("Lưu") { dialog, which ->
                    // Respond to positive button press
                    val customerNameEdit = alertDialogView.findViewById<EditText>(R.id.etCustomerName)
                    val customerAddressEdit = alertDialogView.findViewById<EditText>(R.id.etAddress)
                    val customerCodeEdit = alertDialogView.findViewById<EditText>(R.id.etCustomerCode)
                    val customerPhoneNumber = alertDialogView.findViewById<EditText>(R.id.etPhoneNumber)

                    if (customerNameEdit.text.toString() != "" && customerAddressEdit.text.toString() != "" && customerCodeEdit.text.toString() != "" && customerPhoneNumber.text.toString() != "") {
                        db.collection("KhachHang").add(
                            mapOf(
                                "TenKH" to customerNameEdit.text.toString(),
                                "DiaChi" to customerAddressEdit.text.toString(),
                                "MaKH" to customerCodeEdit.text.toString(),
                                "SDT" to customerPhoneNumber.text.toString()
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogTheme)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun reloadPage(){
        val frgTransaction = parentFragmentManager
        val frg = parentFragmentManager.findFragmentByTag("CustomerDialog")
        frgTransaction.beginTransaction().detach(frg!!).commit()
        frgTransaction.beginTransaction().attach(frg).commit()
    }
}