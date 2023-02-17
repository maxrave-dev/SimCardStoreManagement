package com.maxrave.simcardstoremanagement.model.promotion


import com.google.gson.annotations.SerializedName

data class Promotion(
    @SerializedName("DKApDung")
    val dKApDung: String,
    @SerializedName("GiaTri")
    val giaTri: String,
    @SerializedName("MaKM")
    val maKM: String,
    @SerializedName("TGApDung")
    val tGApDung: String,
    @SerializedName("TTUuTien")
    val tTUuTien: String,
    @SerializedName("TenKM")
    val tenKM: String,
    val id: String,
    var isExpanded: Boolean = false
)