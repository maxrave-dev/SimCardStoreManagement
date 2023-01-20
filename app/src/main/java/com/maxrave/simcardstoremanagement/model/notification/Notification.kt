package com.maxrave.simcardstoremanagement.model.notification


import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("DSNVLike")
    val dSNVLike: List<String>,
    @SerializedName("MaNV")
    val maNV: String,
    @SerializedName("NoiDung")
    val noiDung: String,
    @SerializedName("ThoiDiemDang")
    val thoiDiemDang: Int
)