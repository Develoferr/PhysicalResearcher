package com.tutorial.earthquakemonitor.detailactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tutorial.earthquakemonitor.R
import com.tutorial.earthquakemonitor.databinding.ActivityDetailBinding
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        binding.texMag.text = bundle?.get("eq_mag").toString()
        binding.textPlace.text = bundle?.get("eq_place").toString()
        binding.textLong.text = bundle?.get("eq_long").toString()
        binding.textLat.text = bundle?.get("eq_lat").toString()
        val time = bundle?.get("eq_time").toString().toLong()
        val simpleDateFormat = SimpleDateFormat("dd/MMM/yyyy hh:mm", Locale.getDefault())
        val date = Date(time)
        val formattedString = simpleDateFormat.format(date)
        binding.textTime.text = formattedString
    }
}