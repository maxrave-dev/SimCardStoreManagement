package com.maxrave.simcardstoremanagement.model.archive


import com.google.gson.annotations.SerializedName

data class Archive(
    @SerializedName("MaKho")
    val maKho: String,
    @SerializedName("MaSP")
    val maSP: String,
    @SerializedName("NgayNhap")
    val ngayNhap: String,
    @SerializedName("SoLuong")
    val soLuong: Int,
    @SerializedName("TonKhoT7")
    val tonKhoT7: Int,
    val ID: String
)