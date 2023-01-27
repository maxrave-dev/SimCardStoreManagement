package com.maxrave.simcardstoremanagement.model.stock

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maxrave.simcardstoremanagement.R

class StockAdapter(private var listStock: ArrayList<Stock>, var context: Context): RecyclerView.Adapter<StockAdapter.ViewHolder>(){
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var stockName = itemView.findViewById<TextView>(R.id.tvStockName)
        var stockAddress = itemView.findViewById<TextView>(R.id.tvAddress)
        var stockCode = itemView.findViewById<TextView>(R.id.tvStockCode)
        var stockEdit = itemView.findViewById<Button>(R.id.btEditStock)
        var stockDelete = itemView.findViewById<Button>(R.id.btDeleteStock)

        var expandableLayout = itemView.findViewById<LinearLayout>(R.id.viewExpandable)

        var expandDown = itemView.findViewById<ImageView>(R.id.ivExpandDown)
        var expandUp = itemView.findViewById<ImageView>(R.id.ivExpandUp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_stock, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listStock.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stock = listStock[position]
        var isExpanded = stock.isExpanded
        holder.expandableLayout.visibility = if (isExpanded) View.VISIBLE else View.GONE

        val db = Firebase.firestore

        holder.stockName.text = stock.tenKho
        holder.stockAddress.text = stock.diaChi
        holder.stockCode.text = stock.maKho

        holder.expandDown.setOnClickListener {
            if (!isExpanded) {
                holder.expandableLayout.visibility = View.VISIBLE
                holder.expandDown.visibility = View.GONE
                holder.expandUp.visibility = View.VISIBLE
                isExpanded = true
            }
        }
        holder.expandUp.setOnClickListener {
            if (isExpanded) {
                holder.expandableLayout.visibility = View.GONE
                holder.expandDown.visibility = View.VISIBLE
                holder.expandUp.visibility = View.GONE
                isExpanded = false
            }
        }

        holder.stockEdit.setOnClickListener {
            Log.d("Stock", stock.toString())
            val view = LayoutInflater.from(context).inflate(R.layout.edit_stock_dialog_layout, null)
            val stockNameEdit = view.findViewById<EditText>(R.id.etStockName)
            val stockAddressEdit = view.findViewById<EditText>(R.id.etAddress)
            val stockCodeEdit = view.findViewById<EditText>(R.id.etStockCode)
            Log.d("Stock", stock.toString())
            stockNameEdit.setText(stock.tenKho)
            stockCodeEdit.setText(stock.maKho)
            stockAddressEdit.setText(stock.diaChi)
            MaterialAlertDialogBuilder(context)
                .setView(view)
                .setTitle("Chỉnh sửa kho")
                .setPositiveButton("Lưu") { dialog, which ->
                    // Respond to positive button press
                    db.collection("Kho").document(stock.ID).update(
                        mapOf(
                            "TenKho" to stockNameEdit.text.toString(),
                            "DiaChi" to stockAddressEdit.text.toString(),
                            "MaKho" to stockCodeEdit.text.toString()
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

        holder.stockDelete.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                .setTitle("Xóa kho")
                .setMessage("Bạn có chắc chắn muốn xóa kho này?")
                .setPositiveButton("Xóa") { dialog, which ->
                    // Respond to positive button press
                    db.collection("Kho").document(stock.ID).delete().addOnSuccessListener {
                        Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Huỷ") { dialog, which ->
                    // Respond to negative button press
                }
                .show()
        }
    }


}