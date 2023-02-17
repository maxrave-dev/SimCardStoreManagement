package com.maxrave.simcardstoremanagement.model.promotion

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
import com.maxrave.simcardstoremanagement.databinding.ItemPromotionBinding

class PromotionAdapter(private var listPromotion: ArrayList<Promotion>, var context: Context): RecyclerView.Adapter<PromotionAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemPromotionBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPromotionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listPromotion.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(listPromotion[position]){
                binding.tvPromotionName.text = this.tenKM
                binding.tvPromotionCode.text = this.maKM
                binding.tvDescription.text = this.dKApDung
                binding.tvTime.text= context.getString(R.string.time_application, this.tGApDung)
                binding.tvPriority.text = context.getString(R.string.priority, this.tTUuTien)
                binding.tvValue.text = this.giaTri
                if (isExpanded){
                    binding.viewExpandable.visibility = View.VISIBLE
                    binding.ivExpandDown.visibility = View.GONE
                    binding.ivExpandUp.visibility = View.VISIBLE
                } else {
                    binding.viewExpandable.visibility = View.GONE
                    binding.ivExpandDown.visibility = View.VISIBLE
                    binding.ivExpandUp.visibility = View.GONE
                }
                binding.ivExpandDown.setOnClickListener {
                    if (!isExpanded){
                        binding.viewExpandable.visibility = View.VISIBLE
                        binding.ivExpandDown.visibility = View.GONE
                        binding.ivExpandUp.visibility = View.VISIBLE
                        isExpanded = true
                    }
                }
                binding.ivExpandUp.setOnClickListener {
                    if (isExpanded){
                        binding.viewExpandable.visibility = View.GONE
                        binding.ivExpandDown.visibility = View.VISIBLE
                        binding.ivExpandUp.visibility = View.GONE
                        isExpanded = false
                    }
                }
                val db = Firebase.firestore
                binding.btDeletePromotion.setOnClickListener {
                    MaterialAlertDialogBuilder(context)
                        .setTitle("Xóa CTKM")
                        .setMessage("Bạn có chắc chắn muốn xoá CTKM này?")
                        .setPositiveButton("Xóa") { dialog, which ->
                            // Respond to positive button press
                            db.collection("CTKhuyenMai").document(this.id).delete().addOnSuccessListener {
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
                binding.btEditPromotion.setOnClickListener {
                    val alertDialogView = LayoutInflater.from(context).inflate(R.layout.edit_promotion_dialog_layout, null)
                    val promotionName = alertDialogView.findViewById<EditText>(R.id.etPromotionName)
                    val promotionCode = alertDialogView.findViewById<EditText>(R.id.etPromotionCode)
                    val promotionValue = alertDialogView.findViewById<EditText>(R.id.etValue)
                    val description = alertDialogView.findViewById<EditText>(R.id.etDescription)
                    val time = alertDialogView.findViewById<EditText>(R.id.etTime)
                    val priority = alertDialogView.findViewById<EditText>(R.id.etPriority)
                    promotionName.setText(this.tenKM)
                    promotionCode.setText(this.maKM)
                    promotionValue.setText(this.giaTri)
                    description.setText(this.dKApDung)
                    time.setText(this.tGApDung)
                    priority.setText(this.tTUuTien)
                    MaterialAlertDialogBuilder(context)
                        .setView(alertDialogView)
                        .setTitle("Thêm CTKM")
                        .setPositiveButton("Lưu") { dialog, which ->
                            // Respond to positive button press


                            if (promotionName.text.toString() != "" && promotionCode.text.toString() != "" && promotionValue.text.toString() != "" && description.text.toString() != "" && time.text.toString() != "" && priority.text.toString() != "") {
                                db.collection("CTKhuyenMai").document(this.id).update(
                                    mapOf(
                                        "TenKM" to promotionName.text.toString(),
                                        "MaKM" to promotionCode.text.toString(),
                                        "GiaTri" to promotionValue.text.toString(),
                                        "DKApDung" to description.text.toString(),
                                        "TGApDung" to time.text.toString(),
                                        "TTUuTien" to priority.text.toString()
                                    )
                                ).addOnSuccessListener {
                                    Toast.makeText(
                                        context,
                                        "Chỉnh sửa thành công",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(
                                        context,
                                        "Chỉnh sửa thất bại",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                        .setNegativeButton("Huỷ") { dialog, which ->
                            // Respond to negative button press
                        }
                        .show()
                }
            }
        }

    }
}