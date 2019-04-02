package com.example.mobileweek.controller
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mobileweek.R

class ParametersFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_parameters, container, false)

    companion object {
        fun newInstance(): ParametersFragment = ParametersFragment()
    }
}