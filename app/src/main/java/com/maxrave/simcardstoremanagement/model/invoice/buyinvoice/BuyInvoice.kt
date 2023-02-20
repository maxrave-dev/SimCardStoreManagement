package com.maxrave.simcardstoremanagement.model.invoice.buyinvoice


import com.google.gson.annotations.SerializedName

data class BuyInvoice(
    @SerializedName("DonGia")
    val donGia: Int,
    @SerializedName("GiamGia")
    val giamGia: Int,
    @SerializedName("HinhThucMH")
    val hinhThucMH: String,
    @SerializedName("MaHDMH")
    val maHDMH: String,
    @SerializedName("MaKH")
    val maKH: String,
    @SerializedName("MaKM")
    val maKM: String,
    @SerializedName("MaNV")
    val maNV: String,
    @SerializedName("MaSP")
    val maSP: String,
    @SerializedName("NgayTao")
    val ngayTao: String,
    @SerializedName("SoLuong")
    val soLuong: Int,
    @SerializedName("ThanhTien")
    val thanhTien: Int,
    val ID: String,
    var isExpanded: Boolean = false
)