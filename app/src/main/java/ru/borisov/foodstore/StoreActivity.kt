package ru.borisov.foodstore

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import java.io.IOException

class StoreActivity : AppCompatActivity() {

    private val GALLERY_REQUEST_CODE = 302
    lateinit var toolbar: Toolbar
    lateinit var foodImageIV: ImageView
    lateinit var foodNameET: EditText
    lateinit var foodPriceET: EditText
    lateinit var addFoodItemBTN: Button
    lateinit var foodItemListLV: ListView
    private val foodItemList: MutableList<FoodItem> = mutableListOf()
    lateinit var storeViewModel: StoreViewModel
    private var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)
        storeViewModel = ViewModelProvider(this)[StoreViewModel::class.java]
        initVariables()
        setSupportActionBar(toolbar)
        val adapter = FoodListAdapter(this, foodItemList)
        foodItemListLV.adapter = adapter
        storeViewModel.foodItemList.observe(this) { list ->
            list?.let {
                foodItemList.clear()
                foodItemList.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }
        foodImageIV.setOnClickListener { startGetImageIntent() }
        addFoodItemBTN.setOnClickListener { onAddClick() }
    }

    private fun onAddClick() {
        if (foodNameET.text.isNotEmpty() && foodPriceET.text.isNotEmpty()) {
            val foodItem = FoodItem(
                foodNameET.text.toString(),
                foodPriceET.text.toString().toInt(),
                bitmap
            )
            val list = storeViewModel.foodItemList.value?.toMutableList() ?: mutableListOf()
            list.add(foodItem)
            storeViewModel.foodItemList.value = list
            foodNameET.text.clear()
            foodPriceET.text.clear()
            bitmap = null
            foodImageIV.setImageResource(R.drawable.ic_add_image_24)
        }
    }

    private fun startGetImageIntent() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST_CODE)
    }

    private fun initVariables() {
        toolbar = findViewById(R.id.toolbar)
        foodImageIV = findViewById(R.id.foodImageIV)
        foodNameET = findViewById(R.id.foodNameET)
        foodPriceET = findViewById(R.id.foodPriceET)
        addFoodItemBTN = findViewById(R.id.addFoodItemBTN)
        foodItemListLV = findViewById(R.id.foodItemListLV)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GALLERY_REQUEST_CODE -> if (resultCode == RESULT_OK) {
                val selectedImage: Uri? = data?.data
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                foodImageIV.setImageBitmap(bitmap)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.exit) finish()
        return super.onOptionsItemSelected(item)
    }
}