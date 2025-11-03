package dev.faizul726.newbshadercollection.utils

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri

// Thanks https://stackoverflow.com/a/64059674
internal fun downloadFile(title: String, url: String, context: Context) {
    val request = DownloadManager.Request(url.toUri()).apply {
        setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        setTitle(title)
        setDescription("Downloading...")
        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) // https://developer.android.com/reference/android/app/DownloadManager.Request
        setAllowedOverMetered(true)
        setAllowedOverRoaming(false)
        setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "nxl.mcpack")
    }

    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val downloadId = downloadManager.enqueue(request)
}