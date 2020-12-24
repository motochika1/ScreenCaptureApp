package com.example.screencaptureapp

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.view.MotionEventCompat
import com.example.screencaptureapp.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isPushed = false


    companion object {

        private fun takeScreenShot(view: View): Bitmap =
            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
                .apply { view.draw(Canvas(this)) }

        private fun takeScreenShotOfRootView(v: View): Bitmap = takeScreenShot(v.rootView)

        private fun saveScreenShot(context: Context, bitmap: Bitmap) {

            val values = ContentValues().apply {
                val name = "${System.currentTimeMillis()}.jpg"
                put(MediaStore.Images.Media.DISPLAY_NAME, name)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Screenshots")
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }

            val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            Log.d("mainActivity.kt", Environment.getExternalStorageState())

            context.contentResolver.insert(collection, values)?.let { imageUri ->
                context.contentResolver.openOutputStream(imageUri).use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                }
                values.clear()
                values.put(MediaStore.Images.Media.IS_PENDING, 0)
                context.contentResolver.update(imageUri, values, null, null)

                Toast.makeText(context, "保存しました", Toast.LENGTH_LONG).show()
            }

        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.screenShotButton.setOnClickListener {

            val bitmap = takeScreenShotOfRootView(binding.imageView2)
            binding.imageView3.apply {
                setImageBitmap(bitmap)
                visibility = View.VISIBLE
            }

            view.setBackgroundColor(Color.parseColor("#999999"))

            saveScreenShot(this, bitmap)

            isPushed = true
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event != null) {
            if (event.action == MotionEvent.ACTION_DOWN) {

                if (isPushed) {
                    binding.root.setBackgroundColor(Color.parseColor("#ffffff"))
                    binding.imageView3.visibility = View.INVISIBLE
                    isPushed = false
                }

            }

        }

        return false
    }


}