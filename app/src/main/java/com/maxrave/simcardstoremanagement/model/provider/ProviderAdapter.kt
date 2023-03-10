package com.maxrave.simcardstoremanagement.model.provider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maxrave.simcardstoremanagement.R
import com.maxrave.simcardstoremanagement.model.stock.StockAdapter

class ProviderAdapter(private var listProvider: ArrayList<Provider>, var context: Context): RecyclerView.Adapter<ProviderAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var providerName = itemView.findViewById<TextView>(R.id.tvProviderName)
        var providerCode = itemView.findViewById<TextView>(R.id.tvProviderCode)
        var providerAddress = itemView.findViewById<TextView>(R.id.tvAddress)
        var providerPhoneNumber = itemView.findViewById<TextView>(R.id.tvPhoneNumber)
        var expandableLayout = itemView.findViewById<LinearLayout>(R.id.viewExpandable)
        var providerEdit = itemView.findViewById<Button>(R.id.btEditProvider)
        var providerDelete = itemView.findViewById<Button>(R.id.btDeleteProvider)
        var expandUp = itemView.findViewById<ImageView>(R.id.ivExpandUp)
        var expandDown = itemView.findViewById<ImageView>(R.id.ivExpandDown)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_provider, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listProvider.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val provider = listProvider[position]
        var isExpanded = provider.isExpanded
        holder.expandableLayout.visibility = if (isExpanded) View.VISIBLE else View.GONE

        val db = Firebase.firestore

        holder.providerName.text = provider.tenNhaCC
        holder.providerCode.text = provider.maNhaCC
        holder.providerAddress.text = provider.diaChi
        holder.providerPhoneNumber.text = provider.sDT

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
        holder.providerEdit.setOnClickListener {
            val editProviderView = LayoutInflater.from(context).inflate(R.layout.edit_provider_dialog_layout, null)
            val providerNameEdit = editProviderView.findViewById<EditText>(R.id.etProviderName)
            val providerAddressEdit = editProviderView.findViewById<EditText>(R.id.etAddress)
            val providerCodeEdit = editProviderView.findViewById<EditText>(R.id.etProviderCode)
            val providerPhoneNumber = editProviderView.findViewById<EditText>(R.id.etProviderPhoneNumber)
            providerNameEdit.setText(provider.tenNhaCC)
            providerCodeEdit.setText(provider.maNhaCC)
            providerAddressEdit.setText(provider.diaChi)
            providerPhoneNumber.setText(provider.sDT)
            MaterialAlertDialogBuilder(context)
                .setView(editProviderView)
                .setTitle("Ch???nh s???a Nh?? cung c???p")
                .setPositiveButton("L??u") { dialog, which ->
                    // Respond to positive button press

                    db.collection("NhaCungCap").document(provider.ID).update(
                        mapOf(
                            "TenNhaCC" to providerNameEdit.text.toString(),
                            "DiaChi" to providerAddressEdit.text.toString(),
                            "MaNhaCC" to providerCodeEdit.text.toString(),
                            "SDT" to providerPhoneNumber.text.toString()
                        )
                    ).addOnSuccessListener {
                        Toast.makeText(context, "Ch???nh s???a th??nh c??ng", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(context, "Ch???nh s???a th???t b???i", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Hu???") { dialog, which ->
                    // Respond to negative button press
                }
                .show()

        }
        holder.providerDelete.setOnClickListener {
            MaterialAlertDialogBuilder(context)
                .setTitle("X??a Nh?? cung c???p")
                .setMessage("B???n c?? ch???c ch???n mu???n Nh?? CC n??y?")
                .setPositiveButton("X??a") { dialog, which ->
                    // Respond to positive button press
                    db.collection("NhaCungCap").document(provider.ID).delete().addOnSuccessListener {
                        Toast.makeText(context, "X??a th??nh c??ng", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(context, "X??a th???t b???i", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Hu???") { dialog, which ->
                    // Respond to negative button press
                }
                .show()
        }

    }
}
