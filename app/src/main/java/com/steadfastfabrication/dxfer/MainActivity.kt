package com.steadfastfabrication.dxfer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import com.caverock.androidsvg.SVG
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Handle incoming Intent
        val dxfFile = handleDxfIntent(intent)
        if (dxfFile != null) {
            // Pass to SVG conversion
            val svgString = convertDxfToSvg(dxfFile)

            android.util.Log.d("DXF", "SVG Output: $svgString")
            displayDxfInView(dxfFile)
        }
    }

    private fun displayDxfInView(dxfFile: File) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val svgString = convertDxfToSvg(dxfFile)
                if (svgString != null) {
                    withContext(Dispatchers.Main) {
                        try {
                            val svg = SVG.getFromString(svgString)
                            findViewById<com.caverock.androidsvg.SVGImageView>(R.id.dxfSvgView).setSVG(svg)
                        } catch (e: Exception) {
                            android.util.Log.e("DXF", "SVG Parse Error: ${e.message}")
                        }
                    }
                } else {
                    android.util.Log.e("DXF", "Failed to generate SVG")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                android.util.Log.e("DXF", "Error rendering DXF: ${e.message}")
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent) // Update intent for new shares
        val dxfFile = handleDxfIntent(intent)
        if (dxfFile != null) {
            val svgString = convertDxfToSvg(dxfFile)
            android.util.Log.d("DXF", "SVG Output: $svgString")
            displayDxfInView(dxfFile)
        }
    }

    private fun handleDxfIntent(intent: Intent?): File? {
        try {
            // Check if Intent is for viewing or sharing a file
            if (intent?.action in listOf(Intent.ACTION_VIEW, Intent.ACTION_SEND)) {
                val uri: Uri? = when (intent?.action) {
                    Intent.ACTION_VIEW -> intent.data
                    Intent.ACTION_SEND -> intent.getParcelableExtra(Intent.EXTRA_STREAM)
                    else -> null
                }

                if (uri != null) {
                    // Open InputStream from Uri
                    contentResolver.openInputStream(uri)?.use { inputStream ->
                        // Create a temp file in cache dir
                        val tempFile = File.createTempFile("dxf_", ".dxf", cacheDir)
                        FileOutputStream(tempFile).use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                        return tempFile
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            android.util.Log.e("DXF", "Failed to handle Intent: ${e.message}")
        }
        return null
    }
}