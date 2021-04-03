package com.example.weatherapp.ui.weather.future

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.data.db.models.future.FutureWeatherEntry
import com.example.weatherapp.data.repository.UnitSystem
import com.example.weatherapp.ui.LoadingFragment
import com.example.weatherapp.ui.setNavigartionTitle
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.future_list_fragment.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class FutureListFragment : Fragment(), KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: FutureListViewModelFactory by instance()
    private val viewModel: FutureListViewModel by viewModels {
        viewModelFactory
    }
    private lateinit var loadingFragment: LoadingFragment

    companion object {
        fun newInstance() = FutureListFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.future_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingFragment= LoadingFragment.newInstance()


        bindUI()
    }

    private fun bindUI(){
        lifecycleScope.launchWhenCreated {
            val futureWeather=viewModel.futureWeather.await()
            val weatherLocation=viewModel.weatherLocation.await()

            weatherLocation.observe(viewLifecycleOwner, {
                if(it != null){
                    updateActionBar(it.name, "Next Week")
                }
            })

            futureWeather.observe(viewLifecycleOwner, {
                setLoadingFragment()
                if(it != null){
                    removeLoadingFragment()
                    initRecyclerView(it.toFutureListItem())
                }
            })
        }
    }

    private fun List<FutureWeatherEntry>.toFutureListItem():List<FutureListItem>{
        return this.map {
            FutureListItem(it)
        }
    }

    private fun initRecyclerView(items: List<FutureListItem>){
        val groupAdapter=GroupAdapter<ViewHolder>().apply {
            addAll(items)
        }

        recyclerView.apply {
            layoutManager=LinearLayoutManager(this@FutureListFragment.context)
            adapter=groupAdapter
        }
    }

    private fun updateActionBar(location:String, subTitle:String){
        (activity as setNavigartionTitle).changeNavigationtitle(location, subTitle)
    }
    private fun setLoadingFragment(){
        childFragmentManager
                .beginTransaction()
                .replace(R.id.loadingFragment, loadingFragment)
                .commit()
    }
    private fun removeLoadingFragment(){
        childFragmentManager
                .beginTransaction()
                .remove(loadingFragment)
                .commit()
    }
}