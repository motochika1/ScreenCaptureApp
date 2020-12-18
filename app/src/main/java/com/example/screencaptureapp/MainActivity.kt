package com.example.screencaptureapp

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    companion object {
        private fun takeScreenShot(view: View): Bitmap =
                Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888).apply { view.draw(Canvas(this)) }

        private fun takeScreenShotOfRootView(v: View): Bitmap = takeScreenShot(v.rootView)

        private fun saveScreenShot(context: Context, bitmap: Bitmap) {

            val values = ContentValues().apply {
                val name = "${System.currentTimeMillis()}.jpg"
                put(MediaStore.Images.Media.DISPLAY_NAME, name)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Screenshots")
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }

            val collection =
                    MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

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
        setContentView(R.layout.activity_main)


    }





}