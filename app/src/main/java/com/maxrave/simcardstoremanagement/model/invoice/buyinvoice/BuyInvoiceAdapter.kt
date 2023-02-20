package com.maxrave.simcardstoremanagement.model.invoice.buyinvoice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maxrave.simcardstoremanagement.R
import com.maxrave.simcardstoremanagement.model.customer.CustomerAdapter
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class BuyInvoiceAdapter(private var listBuyInvoice: ArrayList<BuyInvoice>, val context: Context): RecyclerView.Adapter<BuyInvoiceAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val invoiceCode = itemView.findViewById<TextView>(R.id.tvInvoiceCode)
        val customerCode = itemView.findViewById<TextView>(R.id.tvCustomerCode)
        val productCode = itemView.findViewById<TextView>(R.id.tvProductCode)
        val price = itemView.findViewById<TextView>(R.id.tvPrice)
        val date = itemView.findViewById<TextView>(R.id.tvDate)
        val unit = itemView.findViewById<TextView>(R.id.tvUnit)
        val promotionCode = itemView.findViewById<TextView>(R.id.tvPromotionCode)
        val promotionValue = itemView.findViewById<TextView>(R.id.tvPromotionValue)
        val form = itemView.findViewById<TextView>(R.id.tvForm)
        val value = itemView.findViewById<TextView>(R.id.tvValue)
        val userCode = itemView.findViewById<TextView>(R.id.tvUserCode)
        val expandableLayout = itemView.findViewById<View>(R.id.expandableLayout)
        val viewExpandable = itemView.findViewById<View>(R.id.viewExpandable)

        var expandUp = itemView.findViewById<ImageView>(R.id.ivExpandUp)
        var expandDown = itemView.findViewById<ImageView>(R.id.ivExpandDown)

        val delete = itemView.findViewById<Button>(R.id.btDelete)
        val edit = itemView.findViewById<Button>(R.id.btEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_buy_invoice, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listBuyInvoice.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val formatter = NumberFormat.getCurrencyInstance()
        formatter.currency = Currency.getInstance("VND")
        formatter.minimumFractionDigits = 0 // set decimal places to 0
        val buyInvoice = listBuyInvoice[position]

        var isExpanded = buyInvoice.isExpanded
        holder.expandableLayout.visibility = if (isExpanded) View.VISIBLE else View.GONE
        holder.viewExpandable.visibility = if (isExpanded) View.VISIBLE else View.GONE

        val db = Firebase.firestore

        holder.invoiceCode.text = context.getString(R.string.invoice_code_, buyInvoice.maHDMH)
        holder.customerCode.text = context.getString(R.string.customer_code_, buyInvoice.maKH)
        holder.productCode.text = context.getString(R.string.product_code_, buyInvoice.maSP)
        holder.price.text = context.getString(R.string.price_, formatter.format(buyInvoice.donGia))
        holder.date.text = context.getString(R.string.date_create_, buyInvoice.ngayTao)
        holder.unit.text = context.getString(R.string.unit, buyInvoice.soLuong.toString())
        holder.promotionCode.text = context.getString(R.string.promotion_code_, buyInvoice.maKM)
        holder.promotionValue.text = context.getString(R.string.promotion_value_, buyInvoice.giamGia.toString())
        holder.form.text = context.getString(R.string.shopping_form_, buyInvoice.hinhThucMH)
        holder.value.text = context.getString(R.string.total_value_, buyInvoice.thanhTien.toString())
        holder.userCode.text = context.getString(R.string.create_invoice_user_code_, buyInvoice.maNV)

        holder.expandDown.setOnClickListener {
            if (!isExpanded) {
                holder.expandableLayout.visibility = View.VISIBLE
                holder.viewExpandable.visibility = View.VISIBLE
                holder.expandDown.visibility = View.GONE
                holder.expandUp.visibility = View.VISIBLE
                isExpanded = true
            }
        }
        holder.expandUp.setOnClickListener {
            if (isExpanded) {
                holder.expandableLayout.visibility = View.GONE
                holder.viewExpandable.visibility = View.GONE
                holder.expandDown.visibility = View.VISIBLE
                holder.expandUp.visibility = View.GONE
                isExpanded = false
            }
        }
    }
}