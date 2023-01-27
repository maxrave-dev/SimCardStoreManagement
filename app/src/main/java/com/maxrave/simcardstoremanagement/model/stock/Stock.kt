package com.maxrave.simcardstoremanagement.model.stock


import com.google.gson.annotations.SerializedName

data class Stock(
    @SerializedName("DiaChi")
    val diaChi: String,
    @SerializedName("MaKho")
    val maKho: String,
    @SerializedName("TenKho")
    val tenKho: String,
    val ID: String,
    var isExpanded: Boolean = false
)