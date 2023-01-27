package com.maxrave.simcardstoremanagement.model.provider


import com.google.gson.annotations.SerializedName

data class Provider(
    @SerializedName("DiaChi")
    val diaChi: String,
    @SerializedName("MaNhaCC")
    val maNhaCC: String,
    @SerializedName("SDT")
    val sDT: String,
    @SerializedName("TenNhaCC")
    val tenNhaCC: String,
    val ID: String,
    var isExpanded: Boolean = false
)