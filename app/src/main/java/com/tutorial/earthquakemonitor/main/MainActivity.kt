package com.tutorial.earthquakemonitor.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tutorial.earthquakemonitor.Earthquake
import com.tutorial.earthquakemonitor.R
import com.tutorial.earthquakemonitor.api.ApiResponseStatus
import com.tutorial.earthquakemonitor.databinding.ActivityMainBinding
import com.tutorial.earthquakemonitor.databinding.ActivityMainBinding.inflate
import com.tutorial.earthquakemonitor.detailactivity.DetailActivity

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = inflate(layoutInflater)
        setContentView(binding.root)

        binding.eqRecycler.layoutManager = LinearLayoutManager(this)
        viewModel = ViewModelProvider(this, MainViewModelFactory(application)).get(MainViewModel::class.java)

        val adapter = EqAdapter()
        binding.eqRecycler.adapter = adapter

        viewModel.eqList.observe(this, Observer {
            eqList ->
            adapter.submitList(eqList)

            handleEmptyView(eqList, binding)
        })

        viewModel.status.observe(this, Observer {
            apiResponseStatus ->
            if (apiResponseStatus == ApiResponseStatus.LOADING) {
                binding.loadingWheel.visibility = View.VISIBLE
            } else if (apiResponseStatus == ApiResponseStatus.DONE) {
                binding.loadingWheel.visibility = View.GONE
            } else if (apiResponseStatus == ApiResponseStatus.ERROR) {
                binding.loadingWheel.visibility = View.GONE

            }
        })

        adapter.onItemClickListener = {
            openDetailActivity(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if(itemId == R.id.main_menu_sort_magnitude) {
            viewModel.reloadEarthquakesFromDb(true)
        } else if (itemId == R.id.main_menu_sort_time)
            viewModel.reloadEarthquakesFromDb(false)
        return super.onOptionsItemSelected(item)
    }

    private fun openDetailActivity(it: Earthquake) {
        val intent = Intent(this, DetailActivity::class.java)
        val id: String = it.id
        val place: String = it.place
        val magnitude: Double = it.magnitude
        val time: Long = it.time
        val longitude: Double = it.longitude
        val latitude: Double = it.latitude
        intent.putExtra("eq_id", id)
        intent.putExtra("eq_place", place)
        intent.putExtra("eq_mag", magnitude)
        intent.putExtra("eq_time", time)
        intent.putExtra("eq_long", longitude)
        intent.putExtra("eq_lat", latitude)
        startActivity(intent)
    }

    private fun handleEmptyView(
            eqList: MutableList<Earthquake>,
            binding: ActivityMainBinding
    ) {
        if (eqList.isEmpty()) {
            binding.emptyView.visibility = View.VISIBLE
        } else {
            binding.emptyView.visibility = View.GONE
        }
    }
}