package com.maxrave.simcardstoremanagement.mainFragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maxrave.simcardstoremanagement.R
import com.maxrave.simcardstoremanagement.databinding.FragmentHomeBinding
import java.text.NumberFormat
import java.util.*

class HomeFragment : Fragment() {
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var doanhThu: Int = 0
    private var chiPhi: Int = 0
    private var countHoaDon: Int = 0

    lateinit var refresh: SwipeRefreshLayout


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        val formatter = NumberFormat.getCurrencyInstance()
        formatter.currency = Currency.getInstance("VND")
        formatter.minimumFractionDigits = 0 // set decimal places to 0

        binding.linearProgressIndicator.visibility = View.VISIBLE

        refresh = binding.mainContainer

        var db = Firebase.firestore
        db.collection("HDMuaHang").get().addOnSuccessListener { result ->
            doanhThu = 0
            countHoaDon = 0
            for (document in result) {

                countHoaDon++
                Log.d(TAG, "${document.id} => ${document.data}")
                var gia = document.get("DonGia")
                var soLuong = document.get("SoLuong")
                doanhThu += gia.toString().toInt() * soLuong.toString().toInt()
            }
            val sell = formatter.format(doanhThu)
            binding.tvAllTimeSell.text = sell + " VND"
            binding.tvAllTimeInvoice.text = countHoaDon.toString()
        }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents.", exception)
                }
        Log.d("DoanhThu", doanhThu.toString())

        db.collection("HDNhapHang").get().addOnSuccessListener { result ->
            chiPhi = 0
            for (document in result) {
                Log.d(TAG, "${document.id} => ${document.data}")
                var gia = document.get("DonGia")
                var soLuong = document.get("SoLuong")
                chiPhi += gia.toString().toInt() * soLuong.toString().toInt()
            }
            var loiNhuan = doanhThu - chiPhi
            val profit = formatter.format(loiNhuan)
            binding.tvAllTimeProfit.text = profit + " VND"
        }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        db.collection("KhachHang").get().addOnSuccessListener { result ->
            var countKhachHang = 0
            for (document in result) {
                countKhachHang++
            }
            binding.tvAllTimeCustomers.text = countKhachHang.toString()
        }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        db.collection("DSLuuTru").get().addOnSuccessListener { result ->
            var stock = 0
            for (document in result) {
                stock += document.get("TonKhoT7").toString().toInt()
            }
            binding.tvAllTimeStock.text = stock.toString()
            binding.linearProgressIndicator.visibility = View.GONE
        }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        refresh.setOnRefreshListener {
            val frgTransaction = parentFragmentManager
            var frg = parentFragmentManager.findFragmentByTag("HomeFragment")
            frgTransaction.beginTransaction().detach(frg!!).commit()
            frgTransaction.beginTransaction().attach(frg).commit()

            refresh.isRefreshing = false
        }


        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}