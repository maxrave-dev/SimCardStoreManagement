package com.maxrave.simcardstoremanagement.model.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.imageview.ShapeableImageView
import com.maxrave.simcardstoremanagement.R
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class UserAdapter(private var listUsers: ArrayList<user>): RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var avatar = itemView.findViewById<ShapeableImageView>(R.id.ivAvatar)
        var userName = itemView.findViewById<TextView>(R.id.tvUserName)
        var userRole = itemView.findViewById<TextView>(R.id.tvUserRole)
        var userCode = itemView.findViewById<TextView>(R.id.tvUserCode)

        var email = itemView.findViewById<TextView>(R.id.tvEmail)
        var address = itemView.findViewById<TextView>(R.id.tvAddress)
        var salary = itemView.findViewById<TextView>(R.id.tvSalary)
        var managerCode = itemView.findViewById<TextView>(R.id.tvManager)
        var birthday = itemView.findViewById<TextView>(R.id.tvBirthday)
        var sex = itemView.findViewById<TextView>(R.id.tvSex)
        var phoneNumber = itemView.findViewById<TextView>(R.id.tvPhoneNumber)
        var password = itemView.findViewById<TextView>(R.id.tvPassword)

        var expandableLayout = itemView.findViewById<LinearLayout>(R.id.expandableLayout)


        var expandUp = itemView.findViewById<ImageView>(R.id.ivExpandUp)
        var expandDown = itemView.findViewById<ImageView>(R.id.ivExpandDown)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_users, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listUsers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val formatter = NumberFormat.getCurrencyInstance()
        formatter.currency = Currency.getInstance("VND")
        formatter.minimumFractionDigits = 0 // set decimal places to 0

        val user = listUsers[position]
        holder.userName.text = "${user.hoNV} ${user.tenLot} ${user.tenNV}"
        holder.userRole.text = user.chucVu
        holder.userCode.text = user.maNV
        holder.email.text = "Email: "+user.email
        holder.address.text = "Địa chỉ: "+user.diaChi
        holder.salary.text = "Lương: " +formatter.format(user.luong.toString().toInt())
        holder.managerCode.text = "Mã Quản Lý: " +user.maNQL
        holder.birthday.text = "Ngày sinh: " +user.ngaySinh
        holder.sex.text = "Giới tính: "+user.phai
        holder.phoneNumber.text = "SDT: "+user.sDT
        holder.password.text = "Mật khẩu: "+user.matKhau

        var isExpanded = listUsers[position].isExpanded
        holder.expandableLayout.visibility = if (isExpanded) View.VISIBLE else View.GONE

        if (user.avatar != "") {
            holder.avatar.load(user.avatar)
        }
        else {
            holder.avatar.load(R.drawable.manage_accounts_24px)
        }
        holder.expandDown.setOnClickListener {
            if (!isExpanded) {
                holder.expandableLayout.visibility = View.VISIBLE
                holder.expandDown.visibility = View.GONE
                holder.expandUp.visibility = View.VISIBLE
                isExpanded = true
            }
        }
        holder.expandUp.setOnClickListener {
            if (isExpanded) {
                holder.expandableLayout.visibility = View.GONE
                holder.expandDown.visibility = View.VISIBLE
                holder.expandUp.visibility = View.GONE
                isExpanded = false
            }
        }
    }
}