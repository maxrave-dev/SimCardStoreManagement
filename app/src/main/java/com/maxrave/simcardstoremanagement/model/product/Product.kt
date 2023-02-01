package com.maxrave.simcardstoremanagement.model.product


import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("GhiChu")
    val ghiChu: String,
    @SerializedName("GiaBan")
    val giaBan: Int,
    @SerializedName("GiaNhap")
    val giaNhap: Int,
    @SerializedName("MaNhaCC")
    val maNhaCC: String,
    @SerializedName("MaSP")
    val maSP: String,
    @SerializedName("TenSP")
    val tenSP: String,
    val ID: String
)