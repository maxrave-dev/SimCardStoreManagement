package com.maxrave.simcardstoremanagement.other

import android.app.Activity
import android.app.Dialog
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import coil.load
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.maxrave.simcardstoremanagement.R
import com.maxrave.simcardstoremanagement.databinding.UserAccountDialogLayoutBinding

public class AccountDialog: DialogFragment() {
    var _binding: UserAccountDialogLayoutBinding? = null
    val binding get() = _binding!!
    private lateinit var sendBundle: Bundle
    private lateinit var avatarUri: Uri
    var ID: String? = null


    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK)
        {
            Log.d("ID", ID.toString())
            var intentRef = activityResult.data
            var data = intentRef?.data
            if (data != null)
            {
                binding.progressBar.visibility = View.VISIBLE
                Log.d("Avatar","Có upload")
                avatarUri = data
                var st = Firebase.storage
                var avatarRef = st.reference.child("users/image/${ID}")
                var uploadTask = avatarRef.putFile(avatarUri)
                var notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                uploadTask.addOnProgressListener {
                    var progress = (100.0 * it.bytesTransferred / it.totalByteCount).toInt()
                    binding.progressBar.setProgressCompat(progress, true)
                }
                    .addOnSuccessListener {taskSnapshot->
                        binding.progressBar.visibility = View.GONE
                            taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                                downloadUrl->
                                Log.d("DownloadURL", downloadUrl.toString())
                                Toast.makeText(context,"Thay đổi ảnh đại diện thành công",Toast.LENGTH_SHORT).show()
                                var db = Firebase.firestore
                                var userRef = db.collection("NhanVien").document(ID!!)
                                Log.d("it", downloadUrl.toString())
                                userRef.update("Avatar",downloadUrl.toString())
                                binding.ivAvatar.load(downloadUrl)
                                dismiss()
                            }
                    }
                    .addOnFailureListener{
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(context,"Thay đổi ảnh đại diện thất bại",Toast.LENGTH_SHORT).show()
                    }
            }
        }
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
        if (mArgs?.getString("Avatar") != "") {
            binding.ivAvatar.load(mArgs?.getString("Avatar"))
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
            var intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT

            resultLauncher.launch(intent)



            Log.d("CHẠY", "Đã chạy intent")

        }

        binding.topAppBar.setNavigationOnClickListener {
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
        savedInstanceState: Bundle?
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