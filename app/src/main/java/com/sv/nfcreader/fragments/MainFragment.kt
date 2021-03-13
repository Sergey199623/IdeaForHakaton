package com.sv.nfcreader.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.sv.nfcreader.R

class MainFragment :  Fragment(){


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_main, container, false)

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}
