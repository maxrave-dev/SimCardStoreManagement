package com.maxrave.simcardstoremanagement.model.customer


import com.google.gson.annotations.SerializedName

data class Customer(
    @SerializedName("DiaChi")
    val diaChi: String,
    @SerializedName("MaKH")
    val maKH: String,
    @SerializedName("SDT")
    val sDT: String,
    @SerializedName("TenKH")
    val tenKH: String,
    val ID: String
)