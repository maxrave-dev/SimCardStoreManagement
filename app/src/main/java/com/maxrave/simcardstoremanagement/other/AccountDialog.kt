package com.maxrave.simcardstoremanagement.other

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import coil.load
import coil.request.CachePolicy
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.maxrave.simcardstoremanagement.MainActivity
import com.maxrave.simcardstoremanagement.R
import com.maxrave.simcardstoremanagement.databinding.UserAccountDialogLayoutBinding
import com.maxrave.simcardstoremanagement.manage.user.EditDialog

public class AccountDialog: DialogFragment() {
    var _binding: UserAccountDialogLayoutBinding? = null
    val binding get() = _binding!!
    private lateinit var sendBundle: Bundle
    private lateinit var avatarUri: Uri
    var ID: String? = null


    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK)
        {
            Log.d("ID", ID.toString())
            val intentRef = activityResult.data
            val data = intentRef?.data
            if (data != null)
            {
                binding.progressBar.visibility = View.VISIBLE
                Log.d("Avatar","Có upload")
                avatarUri = data
                val st = Firebase.storage
                val avatarRef = st.reference.child("users/image/${ID}")
                val uploadTask = avatarRef.putFile(avatarUri)
                uploadTask.addOnProgressListener {
                    val progress = (100.0 * it.bytesTransferred / it.totalByteCount).toInt()
                    binding.progressBar.setProgressCompat(progress, true)
                }
                    .addOnSuccessListener {taskSnapshot->
                            taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                                downloadUrl->
                                binding.progressBar.visibility = View.GONE
                                Log.d("DownloadURL", downloadUrl.toString())
                                Toast.makeText(context,"Thay đổi ảnh đại diện thành công",Toast.LENGTH_SHORT).show()
                                val db = Firebase.firestore
                                val userRef = db.collection("NhanVien").document(ID!!)
                                Log.d("it", downloadUrl.toString())
                                userRef.update("Avatar",downloadUrl.toString())
                                binding.ivAvatar.load(downloadUrl)
                            }
                    }
                    .addOnFailureListener{
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(context,"Thay đổi ảnh đại diện thất bại",Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun reloadPage() {
        val frg = parentFragmentManager.findFragmentByTag("AccountDialog")
        Log.d("frg", frg.toString())
        val frgTransaction = parentFragmentManager
        frgTransaction.beginTransaction().detach(frg!!).commit()
        frgTransaction.beginTransaction().attach(frg).commit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mArgs = arguments
        ID = mArgs?.getString("ID")
        Log.d("args", mArgs.toString())
        binding.tvUserName.text = "${mArgs?.getString("HoNV")} ${mArgs?.getString("TenLot")} ${mArgs?.getString("TenNV")}"
        binding.tvUserCode.text = mArgs?.getString("MaNV")
        binding.tvUserRole.text = mArgs?.getString("ChucVu")
        if (mArgs?.getString("ChucVu") == "Kế toán, Quản lý")
        {
            binding.tvAccountPermission.text = "Tài khoản Admin"
        }
        else{
            binding.tvAccountPermission.text = "Tài khoản Nhân viên"
        }
        if (mArgs?.getString("Avatar") != "") {
            binding.ivAvatar.load(mArgs?.getString("Avatar")){
                    memoryCachePolicy(CachePolicy.DISABLED)
            }
        }
        else
        {
            binding.ivAvatar.load(R.drawable.outline_account_circle_24)
        }
        binding.btEdit.setOnClickListener{
            var dialog = EditDialog()
            sendBundle = Bundle()
            sendBundle.putString("Avatar",mArgs?.getString("Avatar"))
            sendBundle.putString("Role",mArgs?.getString("ChucVu"))
            sendBundle.putString("Address",mArgs?.getString("DiaChi"))
            sendBundle.putString("Email",mArgs?.getString("Email"))
            sendBundle.putString("LastName",mArgs?.getString("HoNV"))
            sendBundle.putString("Salary",mArgs?.getString("Luong"))
            sendBundle.putString("ManagerCode",mArgs?.getString("MaNQL"))
            sendBundle.putString("UserCode",mArgs?.getString("MaNV"))
            sendBundle.putString("Password",mArgs?.getString("MatKhau"))
            sendBundle.putString("Birthday",mArgs?.getString("NgaySinh"))
            sendBundle.putString("Sex",mArgs?.getString("Phai"))
            sendBundle.putString("PhoneNumber",mArgs?.getString("SDT"))
            sendBundle.putString("MiddleName",mArgs?.getString("TenLot"))
            sendBundle.putString("FirstName",mArgs?.getString("TenNV"))
            sendBundle.putString("ID",mArgs?.getString("ID"))
            dialog.arguments = sendBundle
            dialog.show(parentFragmentManager,"EditDialog")

        }
        binding.btAddAvatar.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT

            resultLauncher.launch(intent)



            Log.d("CHẠY", "Đã chạy intent")

        }
        binding.btChangeEmailPassword.setOnClickListener {
            var dialog = ChangeEmailPasswordDialog()
            sendBundle = Bundle()
            sendBundle.putString("Email",mArgs?.getString("Email"))
            sendBundle.putString("ID",mArgs?.getString("ID"))
            dialog.arguments = sendBundle
            dialog.show(parentFragmentManager,"ChangeEmailPasswordDialog")
        }
        binding.btLogOut.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Cảnh báo!!!")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setNegativeButton("Huỷ") { dialog, which ->
                }
                .setPositiveButton("Đăng xuất") { dialog, which ->
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
                .show()

        }
        binding.btFeedback.setOnClickListener{
            val intent = Intent( Intent.ACTION_SENDTO )
            intent.data = Uri.fromParts("mailto", "ndtminh2608@gmail.com", null)
            intent.putExtra(Intent.EXTRA_SUBJECT, "Báo lỗi về ứng dụng Quản lý Cửa hàng bán sim")
            intent.putExtra(Intent.EXTRA_TEXT, "Nội dung báo lỗi")
            startActivity(intent)
        }

        binding.topAppBar.setNavigationOnClickListener {
            reloadPage()
            dismiss()
        }



    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = UserAccountDialogLayoutBinding.inflate(inflater, container, false)
        var wpr = dialog?.window?.attributes
        wpr?.x = 0
        wpr?.y = -400
        dialog?.window?.attributes = wpr

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}