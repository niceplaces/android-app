package com.niceplaces.niceplaces.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.utils.AppRater

class RateDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            val prefs: SharedPreferences = it.getSharedPreferences("apprater", 0)
            val editor = prefs.edit()
            builder.setTitle(R.string.rate_app).setMessage(R.string.rate_dialog_msg)
                    .setPositiveButton(R.string.rate) { dialog, id ->
                        it.startActivity(Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=${AppRater.APP_PNAME}")))
                        dialog.dismiss()
                    }
                    .setNeutralButton(R.string.remind_me_later) { dialog, id ->
                        editor.putLong("launch_count", -1)
                        editor.apply()
                        dialog.dismiss()
                    }
                    .setNegativeButton(R.string.no_thanks) { dialog, id ->
                        editor.putBoolean("dontshowagain", true)
                        editor.apply()
                        dialog.dismiss()
                    }

            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}