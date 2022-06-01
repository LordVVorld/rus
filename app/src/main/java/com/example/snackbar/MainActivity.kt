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
    private val longClickListener: (Weather) -> Unit = { item ->
        onLongClick(item)
    }
    private val weatherAdapter = Adapter(longClickListener)
    private val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel: MyViewModel by viewModels()
        viewModel.apply {
            getRequestResult().observe(context) {
                Snackbar.make(findViewById(R.id.rView), it.toString(), Snackbar.LENGTH_LONG)
                    .show()
            }
            getWeatherData().observe(context) {
                weatherAdapter.submitList(it)
            }
        }

        findViewById<RecyclerView>(R.id.rView).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = weatherAdapter
        }

    }

    private fun onLongClick(listItem: Weather) {
        val weather: String =
            "City: ${getString(R.string.City)} \n" +
                    "Date: ${listItem.dt_txt} \n" +
                    "Temperature: ${listItem.temp}"

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, weather)
            type = "text/*"
        }
        startActivity(Intent.createChooser(sendIntent, null))
    }
}