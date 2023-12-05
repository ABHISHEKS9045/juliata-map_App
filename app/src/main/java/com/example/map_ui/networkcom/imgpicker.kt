package com.example.map_ui.networkcom

import android.net.Uri
import androidx.core.net.toFile
import java.io.File

class Imgpicker {
fun convertFile(uri: Uri):File{
    return uri.toFile()
}
}