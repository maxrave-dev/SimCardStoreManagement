package com.maxrave.simcardstoremanagement.model.product

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

class ProductAdapter(private var listProduct: ArrayList<Product>, var context: Context): RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val productName = itemView.findViewById<TextView>(R.id.tvProductName)
        val productCode = itemView.findViewById<TextView>(R.id.tvProductCode)
        val providerCode = itemView.findViewById<TextView>(R.id.tvProviderCode)
        val sellPrice = itemView.findViewById<TextView>(R.id.tvSellPrice)
        val importPrice = itemView.findViewById<TextView>(R.id.tvImportPrice)
        val note = itemView.findViewById<TextView>(R.id.tvNote)

        val editProduct = itemView.findViewById<Button>(R.id.btEditProduct)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = View.inflate(context, R.layout.item_product, null)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = listProduct[position]
        val db = Firebase.firestore
        holder.productName.text = context.getString(R.string.product_name, product.tenSP)
        holder.productCode.text = context.getString(R.string.product_code_, product.maSP)
        holder.providerCode.text = context.getString(R.string.provider_code, product.maNhaCC)
        holder.sellPrice.text = context.getString(R.string.sell_price, product.giaBan.toString())
        holder.importPrice.text = context.getString(R.string.import_price, product.giaNhap.toString())
        holder.note.text = context.getString(R.string.note, product.ghiChu)

        holder.editProduct.setOnClickListener {
            val alertDialogView = LayoutInflater.from(context).inflate(R.layout.product_add_dialog_layout, null)
            val productNameEdit = alertDialogView.findViewById<EditText>(R.id.etProductName)
            val productCodeEdit = alertDialogView.findViewById<EditText>(R.id.etProductCode)
            val providerCodeEdit = alertDialogView.findViewById<EditText>(R.id.etProviderCode)
            val sellPriceEdit = alertDialogView.findViewById<EditText>(R.id.etSellPrice)
            val importPriceEdit = alertDialogView.findViewById<EditText>(R.id.etImportPrice)
            val noteEdit = alertDialogView.findViewById<EditText>(R.id.etNote)

            productNameEdit.setText(product.tenSP)
            productCodeEdit.setText(product.maSP)
            providerCodeEdit.setText(product.maNhaCC)
            sellPriceEdit.setText(product.giaBan.toString())
            importPriceEdit.setText(product.giaNhap.toString())
            noteEdit.setText(product.ghiChu)
            MaterialAlertDialogBuilder(context)
                .setView(alertDialogView)
                .setTitle("Sửa sản phẩm")
                .setPositiveButton("Lưu") { dialog, which ->
                    // Respond to positive button press
                    if (productNameEdit.text.toString() != "" && productCodeEdit.text.toString() != "" && providerCodeEdit.text.toString() != "" && sellPriceEdit.text.toString() != "" && importPriceEdit.text.toString() != "") {
                        db.collection("SanPham").document(product.ID).update(
                            mapOf(
                                "GhiChu" to noteEdit.text.toString(),
                                "GiaBan" to sellPriceEdit.text.toString(),
                                "GiaNhap" to importPriceEdit.text.toString(),
                                "MaNhaCC" to providerCodeEdit.text.toString(),
                                "MaSP" to productCodeEdit.text.toString(),
                                "TenSP" to productNameEdit.text.toString()
                            )
                        ).addOnSuccessListener {
                            Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(context, "Sửa thất bại", Toast.LENGTH_SHORT).show()
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
}