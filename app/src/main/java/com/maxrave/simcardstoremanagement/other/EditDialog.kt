package com.maxrave.simcardstoremanagement.other

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maxrave.simcardstoremanagement.R

public class EditDialog: DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        return inflater.inflate(R.layout.users_edit_dialog_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lastName = view.findViewById<EditText>(R.id.etLastName)
        val firstName = view.findViewById<EditText>(R.id.etFirstName)
        val middleName = view.findViewById<EditText>(R.id.etMiddleName)
        val email = view.findViewById<EditText>(R.id.etEmail)
        val role = view.findViewById<EditText>(R.id.etRole)
        val address = view.findViewById<EditText>(R.id.etAddress)
        val password = view.findViewById<EditText>(R.id.etPassword)
        val salary = view.findViewById<EditText>(R.id.etSalary)
        val phone = view.findViewById<EditText>(R.id.etPhoneNumber)
        val userCode = view.findViewById<EditText>(R.id.etUserCode)
        val managerCode = view.findViewById<EditText>(R.id.etManagerCode)
        val sex = view.findViewById<EditText>(R.id.etSex)
        val birthday = view.findViewById<EditText>(R.id.etBirthday)
        val mArgs = arguments
        Log.d("EditDialog", mArgs.toString())
        lastName?.setText(mArgs?.getString("LastName").toString())
        firstName?.setText(mArgs?.getString("FirstName").toString())
        middleName?.setText(mArgs?.getString("MiddleName").toString())
        email?.setText(mArgs?.getString("Email").toString())
        role?.setText(mArgs?.getString("Role").toString())
        address?.setText(mArgs?.getString("Address").toString())
        password?.setText(mArgs?.getString("Password").toString())
        salary?.setText(mArgs?.getString("Salary").toString())
        phone?.setText(mArgs?.getString("PhoneNumber").toString())
        userCode?.setText(mArgs?.getString("UserCode").toString())
        managerCode?.setText(mArgs?.getString("ManagerCode").toString())
        sex?.setText(mArgs?.getString("Sex").toString())
        birthday?.setText(mArgs?.getString("Birthday").toString())
        val userID = mArgs?.getString("ID")


        val topAppBar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.topAppBar)
        topAppBar?.setNavigationOnClickListener {
            dismiss()
            Log.d("EditDialog", "Dismiss")
        }
        topAppBar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.itSave -> {
                    Log.d("EditDialog", "Save")
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Cảnh báo")
                        .setMessage("Bạn có chắc chắn muốn lưu thay đổi?")
                        .setNegativeButton("Hủy") { dialog, which ->
                            dismiss()
                            // Respond to negative button press
                        }
                        .setPositiveButton("Lưu") { dialog, which ->
                            val db = Firebase.firestore
                            val user = db.collection("NhanVien").document(userID.toString())
                            Log.d("EditDialog", user.toString())
                            user.update(mapOf(
                                    "HoNV" to lastName?.text.toString(),
                                    "TenNV" to firstName?.text.toString(),
                                    "TenLot" to middleName?.text.toString(),
                                    "Email" to email?.text.toString(),
                                    "ChucVu" to role?.text.toString(),
                                    "DiaChi" to address?.text.toString(),
                                    "MatKhau" to password?.text.toString(),
                                    "Luong" to salary?.text.toString().toInt(),
                                    "SDT" to phone?.text.toString(),
                                    "MaNV" to userCode?.text.toString(),
                                    "MaNQL" to managerCode?.text.toString(),
                                    "Phai" to sex?.text.toString(),
                                    "NgaySinh" to birthday?.text.toString()
                            )).addOnSuccessListener { Log.d("EditDialog", "DocumentSnapshot successfully updated!")
                                Toast.makeText(requireContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                                reloadPage()
                                dismiss()
                            }
                                .addOnFailureListener { e -> Log.w("EditDialog", "Error updating document", e)
                                Toast.makeText(requireContext(),"Cập nhật thất bại", Toast.LENGTH_SHORT).show()
                                }
                            // Respond to positive button press
                        }
                        // Add customization options here
                        .show()
                    true
                }
                else -> false
            }
        }
    }

    private fun reloadPage() {
        val frgTransaction = parentFragmentManager
        val frg = parentFragmentManager.findFragmentByTag("ManageFragment")
        frgTransaction.beginTransaction().detach(frg!!).commit()
        frgTransaction.beginTransaction().attach(frg).commit()
    }
}