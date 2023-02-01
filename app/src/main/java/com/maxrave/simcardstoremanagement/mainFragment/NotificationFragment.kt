package com.maxrave.simcardstoremanagement.mainFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maxrave.simcardstoremanagement.R
import com.maxrave.simcardstoremanagement.databinding.FragmentNotificationBinding
import com.maxrave.simcardstoremanagement.model.notification.Notification
import com.maxrave.simcardstoremanagement.model.notification.NotificationAdapter
import kotlin.collections.ArrayList

class NotificationFragment : Fragment() {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    lateinit var listNotification: ArrayList<Notification>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        val view = binding.root
        listNotification = ArrayList()
        binding.linearProgressIndicator.visibility = View.VISIBLE
        var db = Firebase.firestore
        val user = Firebase.auth.currentUser
        val email = user?.email
        var emailRef = db.collection("NhanVien").whereEqualTo("Email", email)
        emailRef.get().addOnSuccessListener { result ->
            for (document in result)
            {
                val avatar = document.get("Avatar").toString()
                if (avatar != "")
                {
                    binding.ivAvatar.load(avatar)
                }
                else{
                    binding.ivAvatar.load(R.drawable.manage_accounts_24px)
                }
                break
            }
        }

        db.collection("ThongBao").get().addOnSuccessListener { result ->
            for (document in result)
            {
                val maNV = document.get("MaNV")
                val content = document.get("NoiDung")
                val time = document.get("ThoiDiemDang")
                val listLike = document.get("DSNVLike")
                val maTB = document.get("MaTB")
                val notification = Notification(listLike as List<String>, maNV.toString(), content.toString(), time.toString().toInt(), maTB.toString())
                listNotification.add(notification)
            }
            listNotification.sortByDescending { it.thoiDiemDang }
            val recyclerView: RecyclerView = binding.rvNotification
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            val adapter = NotificationAdapter(listNotification)
            recyclerView.adapter = adapter
            val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            recyclerView.addItemDecoration(decoration)
            recyclerView.setHasFixedSize(true)
            binding.linearProgressIndicator.visibility = View.GONE
        }
            .addOnFailureListener { exception ->
            }

        binding.btPost.setOnClickListener {
            if (binding.etNotification.text.toString() == "") {
                Toast.makeText(requireContext(), "Vui lòng nhập nội dung", Toast.LENGTH_SHORT)
                    .show()
            } else {
                emailRef.get().addOnSuccessListener { result ->
                    for (document in result) {
                        val maNV = document.get("MaNV")
                        val data = hashMapOf(
                            "MaNV" to maNV,
                            "NoiDung" to binding.etNotification.text.toString(),
                            "ThoiDiemDang" to (System.currentTimeMillis() / 1000).toInt(),
                            "DSNVLike" to listOf<String>(),
                            "MaTB" to ""
                        )
                        db.collection("ThongBao").add(data)
                            .addOnSuccessListener { documentReference ->
                                db.collection("ThongBao").document(documentReference.id)
                                    .update("MaTB", documentReference.id)
                                val notification = Notification(
                                    listOf<String>(),
                                    maNV.toString(),
                                    binding.etNotification.text.toString(),
                                    (System.currentTimeMillis() / 1000).toInt(),
                                    documentReference.id
                                )
                                listNotification.add(notification)
                                reloadPage()
                            }
                    }
                }
                Toast.makeText(requireContext(), "Đăng thành công", Toast.LENGTH_SHORT).show()
            }
        }

        binding.notificationContainer.setOnRefreshListener {
            reloadPage()

            binding.notificationContainer.isRefreshing = false
        }


        return view
    }

    private fun reloadPage(){
        val frgTransaction = parentFragmentManager
        val frg = parentFragmentManager.findFragmentByTag("NotificationFragment")
        frgTransaction.beginTransaction().detach(frg!!).commit()
        frgTransaction.beginTransaction().attach(frg).commit()
    }
}