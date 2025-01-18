package ru.borisov.foodstore

import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Parcelable
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

open class CustomActivity : AppCompatActivity() {
    protected var photoUri: Uri? = null
    protected lateinit var toolbar: Toolbar
    protected lateinit var foodImageIV: ImageView
    protected val foodItemList = ArrayList<FoodItem>()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GALLERY_REQUEST_CODE -> if (resultCode == RESULT_OK) {
                photoUri = data?.data
                foodImageIV.setImageURI(photoUri)
            }
        }
    }

    protected fun startGetImageIntent() {
        val photoPickerIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST_CODE)
    }

    protected fun showToastFinishProgram() {
        Toast.makeText(applicationContext, getString(R.string.finish_program), Toast.LENGTH_SHORT)
            .show()
    }

    inline fun <reified T : Parcelable> Intent.parcelableArrayList(key: String): ArrayList<T>? =
        when {
            SDK_INT >= 33 -> getParcelableArrayListExtra(key, T::class.java)
            else -> @Suppress("DEPRECATION") getParcelableArrayListExtra(key)
        }

    companion object {
        const val GALLERY_REQUEST_CODE = 302
        const val FOOD_ITEM_LIST_KEY = "foodItemList"
        const val HAS_INTENT_FROM_DETAILS_ACTIVITY_KEY = "hasIntentFromDetailsActivity"
        const val POSITION_KEY = "position"
    }
}