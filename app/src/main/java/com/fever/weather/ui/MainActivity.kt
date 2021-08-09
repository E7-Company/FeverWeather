package com.fever.weather.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.fever.weather.R
import com.fever.weather.utils.Constants
import com.fever.weather.utils.putUnit
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val prefs = this.getPreferences(Context.MODE_PRIVATE)
        return when (item.itemId) {
            R.id.set_standard -> {
                prefs.putUnit(Constants.Units.Unit.STANDARD)
                true
            }
            R.id.set_metric -> {
                prefs.putUnit(Constants.Units.Unit.METRIC)
                true
            }
            R.id.set_imperial -> {
                prefs.putUnit(Constants.Units.Unit.IMPERIAL)
                true
            }
            else -> {
                prefs.putUnit(Constants.Units.Unit.STANDARD)
                super.onOptionsItemSelected(item)
            }
        }
    }
}