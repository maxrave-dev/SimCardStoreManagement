package com.maxrave.simcardstoremanagement.model.user


import com.google.gson.annotations.SerializedName

data class user(
    @SerializedName("Avatar")
    val avatar: String,
    @SerializedName("ChucVu")
    val chucVu: String,
    @SerializedName("DiaChi")
    val diaChi: String,
    @SerializedName("Email")
    val email: String,
    @SerializedName("HoNV")
    val hoNV: String,
    @SerializedName("Luong")
    val luong: Int,
    @SerializedName("MaNQL")
    val maNQL: String,
    @SerializedName("MaNV")
    val maNV: String,
    @SerializedName("MatKhau")
    val matKhau: String,
    @SerializedName("NgaySinh")
    val ngaySinh: String,
    @SerializedName("Phai")
    val phai: String,
    @SerializedName("SDT")
    val sDT: String,
    @SerializedName("TenLot")
    val tenLot: String,
    @SerializedName("TenNV")
    val tenNV: String,
    @SerializedName("UID")
    val uID: String,
    var isExpanded: Boolean = false
)