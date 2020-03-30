package com.book.chore.utils

import android.content.Context
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ImageManager {
    // generating a temporary image file
    fun createImageFile(context: Context): File? {
        var img: File?

        val timeStamp: String = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(Date())
        val imageFileName = "IMG_" + timeStamp + "_"
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        img = File.createTempFile(
            imageFileName,
            ".JPEG",
            storageDir
        )
        return img
    }
}
