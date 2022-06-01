package com.example.snackbar

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class MainActivity : AppCompatActivity() {
    private val longClick: (Weather) -> Unit = { item -> onLongClick(item) }
    private val adapter = Adapter(longClick)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView: RecyclerView = findViewById(R.id.rView)

        val viewModel: WeatherViewModel by viewModels()
        viewModel.getRequestResult().observe(this) {
            val snackBar = Snackbar.make(recyclerView, it.toString(), Snackbar.LENGTH_LONG)
            snackBar.show()
        }
        viewModel.getWeatherData().observe(this) {
            adapter.submitList(it)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

    }

    private fun onLongClick(listItem: Weather) {
        val dataString: String =
            "City: ${getString(R.string.City)} \n" +
                    "Date: ${listItem.dt_txt} \n" +
                    "Temperature: ${listItem.temp}"

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, dataString)
            type = "text/*"
        }
        startActivity(Intent.createChooser(sendIntent, null))
    }
}