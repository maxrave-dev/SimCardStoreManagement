package com.maxrave.simcardstoremanagement.manage.sell

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maxrave.simcardstoremanagement.R
import com.maxrave.simcardstoremanagement.databinding.PromotionDialogBinding
import com.maxrave.simcardstoremanagement.model.promotion.Promotion
import com.maxrave.simcardstoremanagement.model.promotion.PromotionAdapter

class PromotionDialog: DialogFragment(){
    private var _binding: PromotionDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var listPromotion: ArrayList<Promotion>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PromotionDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listPromotion = ArrayList()
        val db = Firebase.firestore

        binding.topAppBar.setNavigationOnClickListener {
            dismiss()
        }

        db.collection("CTKhuyenMai").get().addOnSuccessListener {
            for (document in it) {
                val maKM = document.get("MaKM")
                val tenKM = document.get("TenKM")
                val tGApDung = document.get("TGApDung")
                val dKApDung = document.get("DKApDung")
                val giaTri = document.get("GiaTri")
                val tTUuTien = document.get("TTUuTien")
                val id = document.id
                val promotion = Promotion(dKApDung.toString(), giaTri.toString(), maKM.toString(), tGApDung.toString(), tTUuTien.toString(), tenKM.toString(), id)
                listPromotion.add(promotion)
            }
            val layoutManager = LinearLayoutManager(context)
            val adapter = PromotionAdapter(listPromotion, requireContext())
            val itemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            val recyclerView = binding.rvListPromotion
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter
            recyclerView.setHasFixedSize(true)
            recyclerView.addItemDecoration(itemDecoration)
        }.addOnFailureListener {

        }
        binding.btAddPromotion.setOnClickListener {
            val alertDialogView = LayoutInflater.from(context).inflate(R.layout.edit_promotion_dialog_layout, null)
            MaterialAlertDialogBuilder(requireContext())
                .setView(alertDialogView)
                .setTitle("Thêm CTKM")
                .setPositiveButton("Lưu") { dialog, which ->
                    // Respond to positive button press
                    val promotionName = alertDialogView.findViewById<EditText>(R.id.etPromotionName)
                    val promotionCode = alertDialogView.findViewById<EditText>(R.id.etPromotionCode)
                    val promotionValue = alertDialogView.findViewById<EditText>(R.id.etValue)
                    val description = alertDialogView.findViewById<EditText>(R.id.etDescription)
                    val time = alertDialogView.findViewById<EditText>(R.id.etTime)
                    val priority = alertDialogView.findViewById<EditText>(R.id.etPriority)

                    if (promotionName.text.toString() != "" && promotionCode.text.toString() != "" && promotionValue.text.toString() != "" && description.text.toString() != "" && time.text.toString() != "" && priority.text.toString() != "") {
                        db.collection("CTKhuyenMai").add(
                            mapOf(
                                "TenKM" to promotionName.text.toString(),
                                "MaKM" to promotionCode.text.toString(),
                                "GiaTri" to promotionValue.text.toString(),
                                "DKApDung" to description.text.toString(),
                                "TGApDung" to time.text.toString(),
                                "TTUuTien" to priority.text.toString()
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    private fun reloadPage(){
        val frgTransaction = parentFragmentManager
        val frg = parentFragmentManager.findFragmentByTag("PromotionDialog")
        frgTransaction.beginTransaction().detach(frg!!).commit()
        frgTransaction.beginTransaction().attach(frg).commit()
    }
}