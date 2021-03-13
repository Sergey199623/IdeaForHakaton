package com.sv.nfcreader.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.sv.nfcreader.R
import com.sv.nfcreader.adapters.AdapterData
import com.sv.nfcreader.data.repo.Repository

class FragmentDataDetails : Fragment() {

    private lateinit var adapter: AdapterData

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_data_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recycler: RecyclerView = view.findViewById(R.id.rv_data)
        adapter = AdapterData()
        recycler.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        updateData()
    }

    private fun updateData() {
        adapter.bindData(Repository().getAccounts())
        adapter.notifyDataSetChanged()
    }
}