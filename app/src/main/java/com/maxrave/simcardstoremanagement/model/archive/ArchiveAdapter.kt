package com.maxrave.simcardstoremanagement.model.archive

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.maxrave.simcardstoremanagement.R

class ArchiveAdapter(private var listArchive: ArrayList<Archive>, var context: Context): RecyclerView.Adapter<ArchiveAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val stockCode = itemView.findViewById<TextView>(R.id.tvStockCode)
        val productCode = itemView.findViewById<TextView>(R.id.tvProductCode)
        val date = itemView.findViewById<TextView>(R.id.tvDate)
        val unit = itemView.findViewById<TextView>(R.id.tvUnit)
        val inventory = itemView.findViewById<TextView>(R.id.tvInventory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = View.inflate(context, R.layout.item_archive, null)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listArchive.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val archive = listArchive[position]
        holder.stockCode.text = context.getString(R.string.stock_code_, archive.maKho)
        holder.productCode.text = context.getString(R.string.product_code, archive.maSP)
        holder.date.text = context.getString(R.string.import_date, archive.ngayNhap)
        holder.unit.text = context.getString(R.string.unit, archive.soLuong.toString())
        holder.inventory.text = context.getString(R.string.inventory, archive.tonKhoT7.toString())
    }
}