package com.maxrave.simcardstoremanagement.model.notification

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maxrave.simcardstoremanagement.R
import java.util.*
import kotlin.collections.ArrayList

class NotificationAdapter(private var listNotification: ArrayList<Notification>): RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var avatar = itemView.findViewById<ImageView>(R.id.ivAvatar)
        var userName = itemView.findViewById<TextView>(R.id.tvUserName)
        var userCode = itemView.findViewById<TextView>(R.id.tvUserCode)
        var userRole = itemView.findViewById<TextView>(R.id.tvUserRole)
        var time = itemView.findViewById<TextView>(R.id.tvTime)
        var content = itemView.findViewById<TextView>(R.id.tvContent)
        var love = itemView.findViewById<CheckBox>(R.id.cbLove)
        var loveCount = itemView.findViewById<TextView>(R.id.tvLoveCount)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = listNotification[position]
        var timeStamp = notification.thoiDiemDang
        var date = Date(timeStamp.toLong() * 1000)
        holder.time.text = date.toString()
        holder.content.text = notification.noiDung
        holder.loveCount.text = notification.dSNVLike.size.toString()
        var db = Firebase.firestore
        val userRef = db.collection("NhanVien").whereEqualTo("MaNV", notification.maNV)
        userRef.get().addOnSuccessListener { documents ->
            for (document in documents) {
                holder.userName.text = document.data["HoNV"].toString() + " " + document.data["TenLot"].toString() + " " + document.data["TenNV"].toString()
                Log.d("Tên", document.data["HoNV"].toString() + " " + document.data["TenLot"].toString() + " " + document.data["TenNV"].toString())
                holder.userCode.text = document.data["MaNV"].toString()
                holder.userRole.text = document.data["ChucVu"].toString()
                var avatar = document.data["Avatar"].toString()
                if (avatar != "")
                {
                    holder.avatar.load(avatar)
                }
                else
                {
                    holder.avatar.load(R.drawable.manage_accounts_24px)
                }
            }
        }


//        var authData = Firebase.auth.currentUser
//        var email = authData?.email
//        var db = Firebase.firestore


    }

    override fun getItemCount(): Int {
        return listNotification.size
    }

}