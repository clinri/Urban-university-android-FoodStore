package ru.borisov.foodstore

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var createStoreBTN: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createStoreBTN = findViewById(R.id.createStoreBTN)
        createStoreBTN.setOnClickListener {
            startActivity(Intent(this, StoreActivity::class.java))
            finish()
        }
    }
}