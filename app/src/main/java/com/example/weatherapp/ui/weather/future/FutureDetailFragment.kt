package com.example.weatherapp.ui.weather.future

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherapp.R

class FutureDetailFragment : Fragment() {

    companion object {
        fun newInstance() = FutureDetailFragment()
    }

    private lateinit var viewModel: FutureDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FutureDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}