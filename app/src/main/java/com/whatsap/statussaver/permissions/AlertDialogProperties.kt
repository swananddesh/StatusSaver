package com.whatsap.statussaver.permissions

import android.view.View

/**
 * Created by Swanand Deshpande on 1/3/18.
 */
class AlertDialogProperties {
    var message: String? = null
    var okayButtonText: String? = null
    var cancelButtonText: String? = null
    var okayButtonColor = 0
    var cancelButtonColor = 0
    var okayButtonListener: View.OnClickListener? = null
    var cancelButtonListener: View.OnClickListener? = null
    var messageTextColor = 0
    var buttontextColor = 0
    var isCancelable = false
}