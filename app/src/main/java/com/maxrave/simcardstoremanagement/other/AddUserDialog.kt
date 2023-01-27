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
import com.google.android.material.snackbar.Snackbar
import com.maxrave.simcardstoremanagement.model.user.user
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maxrave.simcardstoremanagement.R

public class AddUserDialog: DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        return inflater.inflate(R.layout.users_add_dialog_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var lastName = view?.findViewById<EditText>(R.id.etLastName)
        var firstName = view?.findViewById<EditText>(R.id.etFirstName)
        var middleName = view?.findViewById<EditText>(R.id.etMiddleName)
        var email = view?.findViewById<EditText>(R.id.etEmail)
        var role = view?.findViewById<EditText>(R.id.etRole)
        var address = view?.findViewById<EditText>(R.id.etAddress)
        var password = view?.findViewById<EditText>(R.id.etPassword)
        var salary = view?.findViewById<EditText>(R.id.etSalary)
        var phone = view?.findViewById<EditText>(R.id.etPhoneNumber)
        var userCode = view?.findViewById<EditText>(R.id.etUserCode)
        var managerCode = view?.findViewById<EditText>(R.id.etManagerCode)
        var sex = view?.findViewById<EditText>(R.id.etSex)
        var birthday = view?.findViewById<EditText>(R.id.etBirthday)


        var topAppBar = view?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.topAppBar)
        topAppBar?.setNavigationOnClickListener {
            dismiss()
            Log.d("EditDialog", "Dismiss")
        }
        topAppBar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.itSave -> {
                    Log.d("EditDialog", "Save")
                    if (lastName?.text.toString().isNotEmpty()
                        && firstName?.text.toString().isNotEmpty()
                        && middleName?.text.toString().isNotEmpty()
                        && email?.text.toString().isNotEmpty()
                        && role?.text.toString().isNotEmpty()
                        && address?.text.toString().isNotEmpty()
                        && password?.text.toString().isNotEmpty()
                        && salary?.text.toString().isNotEmpty()
                        && phone?.text.toString().isNotEmpty()
                        && userCode?.text.toString().isNotEmpty()
                        && managerCode?.text.toString().isNotEmpty()
                        && sex?.text.toString().isNotEmpty()
                        && birthday?.text.toString().isNotEmpty()){
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Cảnh báo")
                            .setMessage("Bạn có chắc chắn muốn lưu thay đổi?")
                            .setNegativeButton("Hủy") { dialog, which ->
                                dismiss()
                                // Respond to negative button press
                            }
                            .setPositiveButton("Lưu") { dialog, which ->
                                val db = Firebase.firestore
                                var user = hashMapOf(
                                    "Avatar" to "",
                                    "ChucVu" to role?.text.toString(),
                                    "DiaChi" to address?.text.toString(),
                                    "Email" to email?.text.toString(),
                                    "HoNV" to lastName?.text.toString(),
                                    "Luong" to salary?.text.toString().toInt(),
                                    "MaNQL" to managerCode?.text.toString(),
                                    "MaNV" to userCode?.text.toString(),
                                    "MatKhau" to password?.text.toString(),
                                    "NgaySinh" to birthday?.text.toString(),
                                    "Phai" to sex?.text.toString(),
                                    "SDT" to phone?.text.toString(),
                                    "TenLot" to middleName?.text.toString(),
                                    "TenNV" to firstName?.text.toString(),
                                )
                                db.collection("NhanVien")
                                    .add(user)
                                    .addOnSuccessListener { documentReference ->
                                        Log.d("AddUserDialog", "DocumentSnapshot added with ID: ${documentReference.id}")
                                        Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show()
                                        val frgTransaction = parentFragmentManager
                                        val frg = parentFragmentManager.findFragmentByTag("ManageFragment")
                                        frgTransaction.beginTransaction().detach(frg!!).commit()
                                        frgTransaction.beginTransaction().attach(frg).commit()
                                        dismiss()
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w("AddUserDialog", "Error adding document", e)
                                        Toast.makeText(context, "Thêm thất bại", Toast.LENGTH_SHORT).show()
                                        dismiss()
                                    }
                                // Respond to positive button press
                            }
                            // Add customization options here
                            .show()
                    }
                    else{
                        Snackbar.make(view, "Vui lòng nhập đầy đủ thông tin", Snackbar.LENGTH_SHORT).show()
                    }
                    true
                }
                else -> false
            }
        }
    }
}