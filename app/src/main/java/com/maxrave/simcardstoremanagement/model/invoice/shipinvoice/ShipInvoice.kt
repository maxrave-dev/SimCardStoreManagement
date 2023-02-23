package com.maxrave.simcardstoremanagement.model.invoice.shipinvoice


import com.google.gson.annotations.SerializedName

data class ShipInvoice(
    @SerializedName("DVVC")
    val dVVC: String,
    @SerializedName("DiaChi")
    val diaChi: String,
    @SerializedName("GiaTienCOD")
    val giaTienCOD: Int,
    @SerializedName("MaBL")
    val maBL: String,
    @SerializedName("NgayGiao")
    val ngayGiao: String,
    @SerializedName("SDT")
    val sDT: String,
    @SerializedName("TenKH")
    val tenKH: String,
    val ID: String,
    var isExpanded: Boolean = false
)