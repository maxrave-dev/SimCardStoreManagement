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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maxrave.simcardstoremanagement.R
import com.maxrave.simcardstoremanagement.databinding.FragmentNotificationBinding
import com.maxrave.simcardstoremanagement.model.notification.Notification
import com.maxrave.simcardstoremanagement.model.notification.NotificationAdapter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.time.seconds

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

        var db = Firebase.firestore
        db.collection("ThongBao").get().addOnSuccessListener { result ->
            for (document in result)
            {
                var maNV = document.get("MaNV")
                var content = document.get("NoiDung")
                var time = document.get("ThoiDiemDang")
                var listLike = document.get("DSNVLike")
                var maTB = document.get("MaTB")
                var notification = Notification(listLike as List<String>, maNV.toString(), content.toString(), time.toString().toInt(), maTB.toString())
                listNotification.add(notification)
            }
        }
        var user = Firebase.auth.currentUser
        var email = user?.email
        val recyclerView: RecyclerView = binding.rvNotification
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = NotificationAdapter(listNotification)
        recyclerView.adapter = adapter
        var decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(decoration)
        recyclerView.setHasFixedSize(true)
        binding.btPost.setOnClickListener(View.OnClickListener {
            if (binding.etNotification.text.toString() == "")
            {
                Toast.makeText(requireContext(), "Vui lòng nhập nội dung", Toast.LENGTH_SHORT).show()
            }
            else {
                var emailRef = db.collection("NhanVien").whereEqualTo("Email", email)
                emailRef.get().addOnSuccessListener { result ->
                    for (document in result)
                    {
                        var maNV = document.get("MaNV")
                        var data = hashMapOf(
                            "MaNV" to maNV,
                            "NoiDung" to binding.etNotification.text.toString(),
                            "ThoiDiemDang" to (System.currentTimeMillis()/1000).toInt(),
                            "DSNVLike" to listOf<String>(),
                            "MaTB" to ""
                        )
                        db.collection("ThongBao").add(data).addOnSuccessListener { documentReference ->
                            db.collection("ThongBao").document(documentReference.id).update("MaTB", documentReference.id)
                            var notification = Notification(listOf<String>(), maNV.toString(), binding.etNotification.text.toString(), (System.currentTimeMillis()/1000).toInt(), documentReference.id)
                            listNotification.add(notification)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
                Toast.makeText(requireContext(), "Đăng thành công", Toast.LENGTH_SHORT).show()
            }
        },)


        return view
    }
}