package pl.sebcel.bpg.services.sharing

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.core.content.FileProvider
import pl.sebcel.bpg.BpgApplication
import pl.sebcel.bpg.R
import java.io.File

object SharingService {

    fun share(exportedFileName: String?, activity: Activity) {

        if (exportedFileName != null) {
            val requestFile = File(exportedFileName)
            val uri = FileProvider.getUriForFile(activity, "pl.sebcel.bpg.fileprovider", requestFile)

            Log.d("BPG", "Exported file name: $exportedFileName")
            Log.d("BPG", "Exported file URI: $uri")

            Log.d("BPG", "Creating intent")
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, BpgApplication.instance.getString(R.string.data_export_introductory_text))
                putExtra(Intent.EXTRA_STREAM, uri)
                type = "text/csv"
            }

            Log.d("BPG", "Granting permission to read the shared file")
            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            Log.d("BPG", "Launching sharing activity")
            activity.startActivity(sendIntent)
            Log.d("BPG", "Sharing completed")
        }
    }

}