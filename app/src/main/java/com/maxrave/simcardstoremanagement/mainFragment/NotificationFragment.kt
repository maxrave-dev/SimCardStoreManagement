package com.maxrave.simcardstoremanagement.mainFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maxrave.simcardstoremanagement.R
import com.maxrave.simcardstoremanagement.databinding.FragmentNotificationBinding
import com.maxrave.simcardstoremanagement.model.notification.Notification
import com.maxrave.simcardstoremanagement.model.notification.NotificationAdapter

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
                var notification = Notification(listLike as List<String>, maNV.toString(), content.toString(), time.toString().toInt())
                listNotification.add(notification)
            }
        }
        val recyclerView: RecyclerView = binding.rvNotification
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = NotificationAdapter(listNotification)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)


        return view
    }
}