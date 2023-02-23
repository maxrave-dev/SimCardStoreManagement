package com.maxrave.simcardstoremanagement.model.invoice.receipt


import com.google.gson.annotations.SerializedName

data class Receipt(
    @SerializedName("MaBL")
    val maBL: String,
    @SerializedName("MaHDMH")
    val maHDMH: String,
    @SerializedName("MaKH")
    val maKH: String,
    @SerializedName("MaNV")
    val maNV: String,
    @SerializedName("NgayTao")
    val ngayTao: String,
    @SerializedName("SDT")
    val sDT: String,
    val ID: String,
    var isExpanded: Boolean = false
)