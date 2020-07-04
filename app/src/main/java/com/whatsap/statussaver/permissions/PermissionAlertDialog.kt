package com.whatsap.statussaver.permissions

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.TextView
import com.whatsap.statussaver.R
import com.whatsap.statussaver.utils.AppConstants

/**
 * A custom alert dialog have been created that will give information,
 * why we need that particular permission.
 */
class PermissionAlertDialog(activity: Activity, alertDialogProperties: AlertDialogProperties) : AlertDialog(activity) {
    private val typeFaceMyriad: Typeface
    private var okayMsg: String?
    private val cancelMsg: String?
    private val message: String?
    private val alertDialogProperties: AlertDialogProperties?
    private var activity: Activity?

    init {
        message = alertDialogProperties.message
        okayMsg = alertDialogProperties.okayButtonText
        cancelMsg = alertDialogProperties.cancelButtonText
        this.alertDialogProperties = alertDialogProperties
        this.activity = activity
        typeFaceMyriad = Typeface.create(AppConstants.FONT_TYPE_VARELA_ROUND, Typeface.NORMAL)
    }

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.permission_dialog)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        initUI()
    }

    private fun initUI() {
        val txtNotificationMsg = findViewById(R.id.txt_common_msg) as TextView

        if (alertDialogProperties?.messageTextColor == 0)
            activity?.resources?.getColor(R.color.colorBlack)?.let { txtNotificationMsg.setTextColor(it) }
        else
            alertDialogProperties?.messageTextColor?.let { txtNotificationMsg.setTextColor(it) }

        val btnCancel = findViewById(R.id.btn_cancel) as Button
        val btnOkay = findViewById(R.id.btn_okay) as Button

        btnOkay.typeface = typeFaceMyriad
        btnCancel.typeface = typeFaceMyriad
        txtNotificationMsg.typeface = typeFaceMyriad
        if (alertDialogProperties?.cancelButtonColor == 0)
            activity?.resources?.getColor(R.color.common_negative_action_button_text_color)?.let { btnCancel.setBackgroundColor(it) }
        else
            alertDialogProperties?.cancelButtonColor?.let { btnCancel.setBackgroundColor(it) }

        if (alertDialogProperties?.cancelButtonColor == 0)
            activity?.resources?.getColor(R.color.common_negative_action_button_text_color)?.let { btnOkay.setBackgroundColor(it) }
        else
            alertDialogProperties?.okayButtonColor?.let { btnOkay.setBackgroundColor(it) }

        if (alertDialogProperties?.cancelButtonColor == 0)
            activity?.resources?.getColor(R.color.colorBlack)?.let { btnCancel.setTextColor(it) }
        else
            alertDialogProperties?.buttontextColor?.let { btnCancel.setTextColor(it) }

        if (alertDialogProperties?.cancelButtonColor == 0)
            activity?.resources?.getColor(R.color.colorBlack)?.let { btnOkay.setTextColor(it) }
        else
            alertDialogProperties?.buttontextColor?.let { btnOkay.setTextColor(it) }

        btnOkay.setOnClickListener(alertDialogProperties?.okayButtonListener)
        btnCancel.setOnClickListener(alertDialogProperties?.cancelButtonListener)

        txtNotificationMsg.text = Html.fromHtml(message)
        if (okayMsg == null || okayMsg!!.isEmpty()) okayMsg = activity?.resources?.getString(R.string.ok)
        if (cancelMsg == null || cancelMsg.isEmpty()) okayMsg = activity?.resources?.getString(R.string.cancel)
        btnOkay.text = okayMsg
        btnCancel.text = cancelMsg
    }

}