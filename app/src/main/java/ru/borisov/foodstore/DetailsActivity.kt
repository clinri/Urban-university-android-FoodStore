package ru.borisov.foodstore

import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class DetailsActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var foodImageIV: ImageView
    lateinit var foodNameTV: TextView
    lateinit var foodPriceTV: TextView
    lateinit var foodDescriptionTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val foodItem = intent.parcelable<FoodItem>(FoodItem::class.java.simpleName)
        toolbar = findViewById(R.id.toolbar)
        foodImageIV = findViewById(R.id.foodImageIV)
        foodNameTV = findViewById(R.id.foodNameTV)
        foodPriceTV = findViewById(R.id.foodPriceTV)
        foodDescriptionTV = findViewById(R.id.foodDescriptionTV)
        setSupportActionBar(toolbar)
        foodItem?.apply {
            image?.let { foodImageIV.setImageURI(Uri.parse(it)) }
            foodNameTV.text = name
            foodDescriptionTV.text = description ?: ""
            foodPriceTV.text = price.toString()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.exit) finishAffinity()
        return super.onOptionsItemSelected(item)
    }

    inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
        SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    }
}