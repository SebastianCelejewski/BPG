package pl.sebcel.bpg.export

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.DateFormat
import jxl.write.DateTime
import jxl.write.Label;
import jxl.write.Number
import jxl.write.WritableCellFormat
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pl.sebcel.bpg.BpgApplication
import pl.sebcel.bpg.R
import pl.sebcel.bpg.data.local.database.model.Measurement
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ExcelExporter {

    private val fileNameDateFormatter = SimpleDateFormat("yyyy-MM-dd_HH-mm", Locale.UK)
    private val cellDateFormatter = DateFormat("yyyy-MM-dd HH:mm")

    fun exportToExcel(measurements: List<Measurement>, snackbarHostState: SnackbarHostState, scope: CoroutineScope, activity: Activity) : String? {
        val requestId = 1

        try {
            askForPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), requestId, activity)
            val fileName = saveToExcelFile(measurements)
            scope.launch {
                snackbarHostState.showSnackbar(message = activity.getString(R.string.export_completed_toast_text) + fileName, duration = SnackbarDuration.Long)
            }
            Log.d("BPG", "Export successful to file $fileName")
            return fileName
        } catch (ex: Exception) {
            scope.launch {
                snackbarHostState.showSnackbar(message = activity.getString(R.string.export_failed_toast_text) + ex.message, duration = SnackbarDuration.Long)
            }
            Log.e("BPG", "Export failed: ${ex.message}", ex)
            return null
        }
    }

    private fun askForPermissions(permissions: Array<String>, requestCode: Int, activity: Activity) {
        Log.d("BPG", "Checking permissions")
        val permissionsToRequest : MutableList<String> = mutableListOf<String>()
        permissions.forEach {
            if (ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED) {
                Log.d("BPG", "Permission $it is granted")
            } else {
                Log.d("BPG", "Permission $it is not granted")
                permissionsToRequest.add(it)
            }
        }

        Log.d("BPG", "Requesting permissions")
        ActivityCompat.requestPermissions(activity, permissions, requestCode)

        Log.d("BPG", "Checking if permissions were granted")
        val permissionsNotGranted : MutableList<String> = mutableListOf()
        permissions.forEach {
            if (ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED) {
                Log.d("BPG", "Permission $it is granted")
            } else {
                Log.d("BPG", "Permission $it is not granted")
                permissionsNotGranted.add(it)
            }
        }

        if (permissionsNotGranted.isNotEmpty()) {
            throw Exception("Failed to get permissions necessary to perform the export: ${permissionsNotGranted.joinToString(", ")}")
        }
    }

    private fun saveToExcelFile(measurements: List<Measurement>) : String {
        Log.d("BPG", "Export to Excel started")

        val directory = File("${BpgApplication.instance.filesDir}")
        val csvFile = "${fileNameDateFormatter.format(Date())} BPG export.xls"
        val fullPath = "${directory}/{$csvFile}"

        if (!directory.isDirectory) {
            Log.d("BPG", "Creating export directory")
            directory.mkdirs()
        }

        Log.d("BPG", "Directory path: $directory")
        Log.d("BPG", "File name: $csvFile")
        Log.d("BPG", "Full path: $fullPath")

        val file = File(directory, csvFile)
        val wbSettings: WorkbookSettings = WorkbookSettings()
        wbSettings.locale = Locale.UK
        wbSettings.setRationalization(false) // workaround for java.lang.ArrayIndexOutOfBoundsException in jxl.biff.IndexMapping.getNewIndex
        val workbook: WritableWorkbook = Workbook.createWorkbook(file, wbSettings)

        val sheetA: WritableSheet = workbook.createSheet("Measurements", 0)

        sheetA.addCell(Label(0, 0, "Data"))
        sheetA.addCell(Label(1, 0, "Poziom bólu"))
        sheetA.addCell(Label(2, 0, "Uwagi"))

        var rowIdx = 1
        measurements.forEach{measurement -> run {
            Log.d("BPG", "Exporting row $rowIdx: ${measurement.date}, ${measurement.pain}, ${measurement.comment}")
            sheetA.addCell(DateTime(0, rowIdx, measurement.date, WritableCellFormat(cellDateFormatter)))
            sheetA.addCell(Number(1, rowIdx, measurement.pain.toDouble()))
            sheetA.addCell(Label(2, rowIdx, measurement.comment))
            rowIdx++
        }}

        Log.d("BPG", "Rows added")

        Log.d("BPG", "Writing spreadsheet to output file")
        workbook.write()

        Log.d("BPG", "Closing the output file")
        workbook.close()

        Log.d("BPG", "Export completed")

        return "${BpgApplication.instance.filesDir.path}/${csvFile}"
    }
}