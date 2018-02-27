package com.coursion.mediapickerlib

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import com.coursion.freakycoder.mediapicker.galleries.Gallery
import kotlinx.android.synthetic.main.content_main.*
import java.io.File
import java.io.FileNotFoundException


class MainActivity : AppCompatActivity() {

    val OPEN_MEDIA_PICKER = 1  // Request code
    val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100 // Request code for read external storage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            if (!permissionIfNeeded()) {
                val intent = Intent(this, Gallery::class.java)
                // Set the title
                intent.putExtra("title", "Select media")
                // Mode 1 for both images and videos selection, 2 for images only and 3 for videos!
                intent.putExtra("mode", 1)
                intent.putExtra("maxSelection", 3) // Optional
                startActivityForResult(intent, OPEN_MEDIA_PICKER)
            }
        }
    }

    private fun permissionIfNeeded(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (shouldShowRequestPermissionRationale(
                                Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to read the contacts
                }

                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
                return true
            }
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Check which request we're responding to
        if (requestCode == OPEN_MEDIA_PICKER) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK && data != null) {
                val selectionResult = data.getStringArrayListExtra("result")
                selectionResult.forEach {
                    try {
                        Log.d("MyApp", "Image Path : " + it)
                        val uriFromPath = Uri.fromFile(File(it))
                        Log.d("MyApp", "Image URI : " + uriFromPath)
                        // Convert URI to Bitmap
                        val bm = BitmapFactory.decodeStream(
                                contentResolver.openInputStream(uriFromPath))
                        image.setImageBitmap(bm)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}
