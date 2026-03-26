package com.connecto.app.report

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * Exports a report string to a PDF file using the native Android PDF API.
 * Fully offline, no third-party library required.
 */
object PdfExporter {

    /**
     * Saves the report as a PDF in the Downloads folder.
     * @return the path of the saved file, or null if failed.
     */
    fun exportToPdf(context: Context, reportText: String, patientName: String): String? {
        return try {
            val document = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4
            val page = document.startPage(pageInfo)

            val canvas = page.canvas
            val paint = android.graphics.Paint().apply {
                color = android.graphics.Color.BLACK
                textSize = 11f
                typeface = android.graphics.Typeface.MONOSPACE
            }

            val margin = 40f
            val lineHeight = 16f
            var yPos = 60f
            val maxWidth = pageInfo.pageWidth - margin * 2
            var pageIndex = 1

            val lines = reportText.split("\n")

            for (line in lines) {
                if (yPos > pageInfo.pageHeight - margin) {
                    document.finishPage(page)
                    pageIndex++
                    val nextPageInfo = PdfDocument.PageInfo.Builder(595, 842, pageIndex).create()
                    val nextPage = document.startPage(nextPageInfo)
                    canvas.apply { }
                    yPos = 60f
                }
                canvas.drawText(line.take(80), margin, yPos, paint)
                yPos += lineHeight
            }

            document.finishPage(page)

            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName  = "CONNECTO_${patientName.replace(" ", "_")}_$timestamp.pdf"

            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, fileName)

            FileOutputStream(file).use { document.writeTo(it) }
            document.close()

            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
