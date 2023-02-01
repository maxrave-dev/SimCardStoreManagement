package com.maxrave.simcardstoremanagement.model.customer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maxrave.simcardstoremanagement.R

class CustomerAdapter(private var listCustomer: ArrayList<Customer>, var context: Context): RecyclerView.Adapter<CustomerAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var customerName = itemView.findViewById<TextView>(R.id.tvCustomerName)
        var customerCode = itemView.findViewById<TextView>(R.id.tvCustomerCode)
        var address = itemView.findViewById<TextView>(R.id.tvAddress)
        var phoneNumber = itemView.findViewById<TextView>(R.id.tvPhoneNumber)
        var edit = itemView.findViewById<Button>(R.id.btEditCustomer)
        var delete = itemView.findViewById<Button>(R.id.btDeleteCustomer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_customer, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listCustomer.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val db = Firebase.firestore
        val customer = listCustomer[position]
        holder.customerName.text = customer.tenKH
        holder.customerCode.text = customer.maKH
        holder.address.text = customer.diaChi
        holder.phoneNumber.text = customer.sDT

        holder.edit.setOnClickListener {
            val view = LayoutInflater.from(context).inflate(R.layout.edit_customer_dialog_layout, null)
            val customerNameEdit = view.findViewById<EditText>(R.id.etCustomerName)
            val customerAddressEdit = view.findViewById<EditText>(R.id.etAddress)
            val customerCodeEdit = view.findViewById<EditText>(R.id.etCustomerCode)
            val customerPhoneNumber = view.findViewById<EditText>(R.id.etPhoneNumber)
            customerNameEdit.setText(customer.tenKH)
            customerAddressEdit.setText(customer.diaChi)
            customerCodeEdit.setText(customer.maKH)
            customerPhoneNumber.setText(customer.sDT)
            MaterialAlertDialogBuilder(context)
                .setView(view)
                .setTitle("Chỉnh sửa khách hàng")
                .setPositiveButton("Lưu") { dialog, which ->
                    // Respond to positive button press
                    db.collection("Kho").document(customer.ID).update(
                        mapOf(
                            "TenKH" to customerNameEdit.text.toString(),
                            "DiaChi" to customerAddressEdit.text.toString(),
                            "MaKH" to customerCodeEdit.text.toString(),
                            "SDT" to customerPhoneNumber.text.toString()
                        )
                    ).addOnSuccessListener {
                        Toast.makeText(context, "Chỉnh sửa thành công", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(context, "Chỉnh sửa thất bại", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Huỷ") { dialog, which ->
                    // Respond to negative button press
                }
                .show()
        }
        holder.delete.setOnClickListener {
            db.collection("KhachHang").document(customer.ID).delete()
        }
    }
}