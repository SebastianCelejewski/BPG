package pl.sebcel.bpg.export

import android.os.Environment
import android.util.Log
import jxl.DateCell
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
import pl.sebcel.bpg.BpgApplication
import pl.sebcel.bpg.data.local.database.model.Measurement
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ExcelExporter {

    private val fileNameDateFormatter = SimpleDateFormat("yyyy-MM-dd_HH-mm", Locale.UK)
    private val cellDateFormatter = DateFormat("yyyy-MM-dd HH:mm")

    fun export(measurements: List<Measurement>) : String {
        Log.d("BPG", "Export to Excel started")

        val directory = File("${BpgApplication.instance.filesDir}")
        val csvFile = "${fileNameDateFormatter.format(Date())} BPG export.xls"

        if (!directory.isDirectory) {
            Log.d("BPG", "Creating export directory")
            directory.mkdirs()
        }

        Log.d("BPG", "Directory path: $directory")
        Log.d("BPG", "File name: $csvFile")

        val file = File(directory, csvFile)
        val wbSettings: WorkbookSettings = WorkbookSettings()
        wbSettings.setLocale(Locale.UK)
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

        return "$directory/$file"
    }
}