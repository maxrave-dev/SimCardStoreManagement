package com.maxrave.simcardstoremanagement.model.invoice.receipt

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maxrave.simcardstoremanagement.R
import com.maxrave.simcardstoremanagement.databinding.ItemReceiptBinding

class ReceiptAdapter(private var listReceipt: ArrayList<Receipt>, var context: Context): RecyclerView.Adapter<ReceiptAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemReceiptBinding): RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = ItemReceiptBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listReceipt.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var receipt = listReceipt[position]
        val db = Firebase.firestore

        var isExpanded = receipt.isExpanded
        holder.binding.expandableLayout.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.binding.viewExpandable.visibility = if (isExpanded) View.VISIBLE else View.GONE

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

        holder.binding.tvReceiptCode.text = context.getString(R.string.invoice_code1_, receipt.maBL)
        holder.binding.tvInvoiceCode.text = context.getString(R.string.invoice_code_, receipt.maHDMH)
        holder.binding.tvCustomerCode.text = context.getString(R.string.customer_code_, receipt.maKH)
        holder.binding.tvDate.text = context.getString(R.string.date_create_, receipt.ngayTao)
        holder.binding.tvPhoneNumber.text = context.getString(R.string.phone_number_, receipt.sDT)
        holder.binding.tvUserCode.text = context.getString(R.string.create_receipt_user_code_, receipt.maNV)

        holder.binding.btDelete.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                .setTitle("Xoá biên lai này?")
                .setPositiveButton("Xoá"){dialog, which ->
                    db.collection("BienLai").document(receipt.ID).delete()
                    notifyDataSetChanged()
                    Toast.makeText(context, "Xoá thành công", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Huỷ"){dialog, which ->
                }
                .show()
        }

        holder.binding.btEdit.setOnClickListener {
            val alertDialogView = LayoutInflater.from(context).inflate(R.layout.edit_receipt_dialog_layout, null)

            val maBL =  alertDialogView.findViewById<TextInputEditText>(R.id.etReceiptCode)
            val maHDMH =  alertDialogView.findViewById<TextInputEditText>(R.id.etInvoiceCode)
            val maKH =  alertDialogView.findViewById<TextInputEditText>(R.id.etCustomerCode)
            val sDT = alertDialogView.findViewById<TextInputEditText>(R.id.etPhoneNumber)
            val maNV = alertDialogView.findViewById<TextInputEditText>(R.id.etUserCode)
            val ngayTao = alertDialogView.findViewById<TextInputEditText>(R.id.etDate)

            maBL.setText(receipt.maBL)
            maHDMH.setText(receipt.maHDMH)
            maKH.setText(receipt.maKH)
            sDT.setText(receipt.sDT)
            maNV.setText(receipt.maNV)
            ngayTao.setText(receipt.ngayTao)

            MaterialAlertDialogBuilder(context)
                .setView(alertDialogView)
                .setTitle("Sửa thông tin biên lai")
                .setPositiveButton("Lưu"){dialog, which ->
                    db.collection("BienLai").document(receipt.ID).update(
                        mapOf(
                            "MaBL" to maBL.text.toString(),
                            "MaHDMH" to maHDMH.text.toString(),
                            "MaKH" to maKH.text.toString(),
                            "MaNV" to maNV.text.toString(),
                            "NgayTao" to ngayTao.text.toString(),
                            "SDT" to sDT.text.toString()
                        )
                    ).addOnSuccessListener {
                        Toast.makeText(context, "Sửa thông tin biên lai thành công", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(context, "Sửa thông tin biên lai thất bại", Toast.LENGTH_SHORT).show()
                    }
                }.setNegativeButton("Huỷ"){
                        dialog, which ->
                }.show()
        }
    }
}