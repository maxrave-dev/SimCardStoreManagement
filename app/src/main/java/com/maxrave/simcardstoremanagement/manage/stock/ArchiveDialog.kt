package com.maxrave.simcardstoremanagement.manage.stock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.maxrave.simcardstoremanagement.R
import com.maxrave.simcardstoremanagement.databinding.ArchiveDialogBinding
import com.maxrave.simcardstoremanagement.model.archive.Archive
import com.maxrave.simcardstoremanagement.model.archive.ArchiveAdapter

class ArchiveDialog: DialogFragment() {
    private var _binding: ArchiveDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var listArchive: ArrayList<Archive>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ArchiveDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listArchive = ArrayList()
        val db = Firebase.firestore

        binding.topAppBar.setNavigationOnClickListener {
            dismiss()
        }

        db.collection("DSLuuTru").get().addOnSuccessListener { result ->
            for (document in result) {
                val maKho = document.get("MaKho")
                val maSP = document.get("MaSP")
                val ngayNhap = document.get("NgayNhap")
                val soLuong = document.get("SoLuong")
                val tonKhoT7 = document.get("TonKhoT7")
                val ID = document.id
                val archive = Archive(maKho.toString(), maSP.toString(), ngayNhap.toString(), soLuong.toString().toInt(), tonKhoT7.toString().toInt(), ID)
                listArchive.add(archive)
            }
            binding.rvListArchive.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = ArchiveAdapter(listArchive, context)
                addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            }
        }.addOnFailureListener {

        }


        binding.refreshLayout.setOnRefreshListener {
            reloadPage()
            binding.refreshLayout.isRefreshing = false
        }


    }
    private fun reloadPage(){
        val frgTransaction = parentFragmentManager
        val frg = parentFragmentManager.findFragmentByTag("ArchiveDialog")
        frgTransaction.beginTransaction().detach(frg!!).commit()
        frgTransaction.beginTransaction().attach(frg).commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}