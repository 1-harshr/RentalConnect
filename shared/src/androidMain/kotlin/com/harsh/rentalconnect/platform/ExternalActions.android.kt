package com.harsh.rentalconnect.platform

import android.content.Context
import android.content.Intent
import android.net.Uri

actual fun openExternalUrl(url: String) {
    val context = AndroidContextHolder.context ?: return
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}

object AndroidContextHolder {
    var context: Context? = null
}
