package com.maxrave.simcardstoremanagement.model.invoice.shipinvoice

import android.annotation.SuppressLint
import android.content.ClipData.Item
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maxrave.simcardstoremanagement.R
import com.maxrave.simcardstoremanagement.databinding.ItemShipInvoiceBinding
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class ShipInvoiceAdapter(private var listShipInvoice: ArrayList<ShipInvoice>, var context: Context): RecyclerView.Adapter<ShipInvoiceAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemShipInvoiceBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemShipInvoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listShipInvoice.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val formatter = NumberFormat.getCurrencyInstance()
        formatter.currency = Currency.getInstance("VND")
        formatter.minimumFractionDigits = 0 // set decimal places to 0
        val db = Firebase.firestore
        val shipInvoice = listShipInvoice[position]

        var isExpanded = shipInvoice.isExpanded
        holder.binding.expandableLayout.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.binding.viewExpandable.visibility = if (isExpanded) View.VISIBLE else View.GONE


        holder.binding.tvAddress.text = context.getString(R.string.address_, shipInvoice.diaChi)
        holder.binding.tvShipDate.text = context.getString(R.string.ship_date_, shipInvoice.ngayGiao)
        holder.binding.tvCustomerName.text = context.getString(R.string.customer_name_, shipInvoice.tenKH)
        holder.binding.tvPhoneNumber.text = context.getString(R.string.phone_number_, shipInvoice.sDT)
        holder.binding.tvShipService.text = context.getString(R.string.ship_service_, shipInvoice.dVVC)
        holder.binding.tvValue.text = context.getString(R.string.total_value_, formatter.format(shipInvoice.giaTienCOD))
        holder.binding.tvReceiptCode.text = context.getString(R.string.invoice_code1_, shipInvoice.maBL)

        holder.binding.ivExpandDown.setOnClickListener {
            if (!isExpanded) {
                holder.binding.expandableLayout.visibility = View.VISIBLE
                holder.binding.viewExpandable.visibility = View.VISIBLE
                holder.binding.ivExpandDown.visibility = View.GONE
                holder.binding.ivExpandUp.visibility = View.VISIBLE
                isExpanded = true
            }
        }
        holder.binding.ivExpandUp.setOnClickListener {
            if (isExpanded) {
                holder.binding.expandableLayout.visibility = View.GONE
                holder.binding.viewExpandable.visibility = View.GONE
                holder.binding.ivExpandDown.visibility = View.VISIBLE
                holder.binding.ivExpandUp.visibility = View.GONE
                isExpanded = false
            }
        }

        holder.binding.btDelete.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                .setTitle("Xoá hóa đơn giao hàng")
                .setPositiveButton("Xoá"){dialog, which ->
                    db.collection("HDGiaoHang").document(shipInvoice.ID).delete()
                    notifyDataSetChanged()
                    Toast.makeText(context, "Xoá thành công", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Huỷ"){dialog, which ->
                }
                .show()
        }
        holder.binding.btEdit.setOnClickListener {
            val alertDialogView = LayoutInflater.from(context).inflate(R.layout.edit_ship_invoice_dialog_layout, null)
            val maBL = alertDialogView.findViewById<EditText>(R.id.etReceiptCode)
            val tenKH = alertDialogView.findViewById<EditText>(R.id.etCustomerName)
            val diaChi = alertDialogView.findViewById<EditText>(R.id.etAddress)
            val sDT = alertDialogView.findViewById<EditText>(R.id.etPhoneNumber)
            val cOD = alertDialogView.findViewById<EditText>(R.id.etCOD)
            val ngayGiao = alertDialogView.findViewById<EditText>(R.id.etDate)
            val dVVC = alertDialogView.findViewById<EditText>(R.id.etShipService)

            maBL.setText(shipInvoice.maBL)
            tenKH.setText(shipInvoice.tenKH)
            diaChi.setText(shipInvoice.diaChi)
            sDT.setText(shipInvoice.sDT)
            cOD.setText(shipInvoice.giaTienCOD.toString())
            ngayGiao.setText(shipInvoice.ngayGiao)
            dVVC.setText(shipInvoice.dVVC)

            MaterialAlertDialogBuilder(context)
                .setView(alertDialogView)
                .setTitle("Sửa hóa đơn giao hàng")
                .setPositiveButton("Lưu"){ dialog, which ->
                    db.collection("HDGiaoHang").document(shipInvoice.ID).update(
                        mapOf(
                            "MaBL" to maBL.text.toString(),
                            "TenKH" to tenKH.text.toString(),
                            "DiaChi" to diaChi.text.toString(),
                            "SDT" to sDT.text.toString(),
                            "GiaTienCOD" to cOD.text.toString().toInt(),
                            "NgayGiao" to ngayGiao.text.toString(),
                            "DVVC" to dVVC.text.toString()
                        )
                    ).addOnSuccessListener {
                        Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Sửa thất bại", Toast.LENGTH_SHORT).show()
                    }
                }.setNegativeButton("Huỷ"){
                        dialog, which ->
                }
                .show()
        }

    }
}