package com.whatsap.statussaver.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.whatsap.statussaver.R
import com.whatsap.statussaver.permissions.AlertDialogProperties
import com.whatsap.statussaver.permissions.AppPermission
import java.util.*

class SplashActivity : AppCompatActivity() {
    private var activity: Activity? = null
    private var isDialogShowing = false
    private var appPermission: AppPermission? = null
    private lateinit var permissionArray: Array<String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        activity = this
    }

    override fun onResume() {
        super.onResume()
        if (!isDialogShowing) {
            methodRequiresPermission()
        }
    }

    override fun onPause() {
        super.onPause()
        isDialogShowing = false
    }

    /**
     * Ask for permission. If permission is allowed,
     * then & then only proceed.
     */
    private fun methodRequiresPermission() {
        appPermission = AppPermission.instance
        val permission = intArrayOf(AppPermission.PERMISSION_REQUEST_STORAGE)
        if (!activity?.let { appPermission?.requestPermissionList(it, permission, MULTIPLE_PERMISSION_REQUEST) }!!) {
            startAppProcess()
        }
    }

    /**
     * Method to go to LandingActivity */
    private fun startAppProcess() {
        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, LandingActivity::class.java))
            finish()
        }, 3000)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        val tempPermissionList: ArrayList<String>
        val listRational = ArrayList<String>()
        if (requestCode == MULTIPLE_PERMISSION_REQUEST) {
            // If request is cancelled, the result arrays are empty.
            tempPermissionList = ArrayList()
            if (grantResults.isNotEmpty()) {
                for (i in grantResults.indices) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        val showRationale = shouldShowRequestPermissionRationale(permissions[i])
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            if (!showRationale) listRational.add(permissions[i]) else tempPermissionList.add(permissions[i])
                        }
                    }
                }
                val alertDialogProperties: AlertDialogProperties
                if (tempPermissionList.isNotEmpty()) {
                    isDialogShowing = true
                    val tempArray = arrayOfNulls<String>(tempPermissionList.size)
                    tempPermissionList.toArray(tempArray)
                    tempPermissionList.clear()
                    permissionArray = tempArray
                    alertDialogProperties = AlertDialogProperties()
                    alertDialogProperties.buttontextColor = activity!!.resources.getColor(R.color.colorWhite)
                    alertDialogProperties.isCancelable = false
                    alertDialogProperties.cancelButtonColor = activity!!.resources.getColor(R.color.colorPrimary)
                    alertDialogProperties.okayButtonColor = activity!!.resources.getColor(R.color.colorPrimary)
                    alertDialogProperties.cancelButtonText = activity!!.resources.getString(R.string.exit)
                    alertDialogProperties.okayButtonText = activity!!.resources.getString(R.string.ok)
                    alertDialogProperties.messageTextColor = activity!!.resources.getColor(R.color.colorBlack)
                    alertDialogProperties.okayButtonListener = OKButtonListener()
                    alertDialogProperties.cancelButtonListener = CancelButtonListener()
                    appPermission!!.showPermissionDialog(activity!!, alertDialogProperties, permissionArray)
                } else if (!listRational.isEmpty()) {
                    isDialogShowing = true
                    val tempArray = arrayOfNulls<String>(listRational.size)
                    listRational.toArray(tempArray)
                    tempPermissionList.clear()
                    permissionArray = tempArray
                    alertDialogProperties = AlertDialogProperties()
                    alertDialogProperties.buttontextColor = activity!!.resources.getColor(R.color.colorWhite)
                    alertDialogProperties.isCancelable = false
                    alertDialogProperties.cancelButtonColor = activity!!.resources.getColor(R.color.colorPrimary)
                    alertDialogProperties.okayButtonColor = activity!!.resources.getColor(R.color.colorPrimary)
                    alertDialogProperties.cancelButtonText = activity!!.resources.getString(R.string.exit)
                    alertDialogProperties.okayButtonText = activity!!.resources.getString(R.string.app_settings)
                    alertDialogProperties.messageTextColor = activity!!.resources.getColor(R.color.colorBlack)
                    alertDialogProperties.okayButtonListener = GoToSettings()
                    alertDialogProperties.cancelButtonListener = CancelButtonListener()
                    appPermission!!.showPermissionDialog(activity!!, alertDialogProperties, permissionArray)
                } else {
                    startAppProcess()
                }
            }
        }
    }

    /**
     * Ask for permission again click on OK
     */
    internal inner class OKButtonListener : View.OnClickListener {
        override fun onClick(v: View) {
            appPermission!!.requestPermissionList(activity, permissionArray, MULTIPLE_PERMISSION_REQUEST)
            appPermission!!.closeDialog()
        }
    }

    internal inner class CancelButtonListener : View.OnClickListener {
        override fun onClick(v: View) {
            appPermission!!.closeDialog()
            onBackPressed()
        }
    }

    private inner class GoToSettings : View.OnClickListener {
        override fun onClick(v: View) {
            val i = Intent()
            i.apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                addCategory(Intent.CATEGORY_DEFAULT)
                data = Uri.parse("package:" + activity!!.packageName)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            }
            startActivity(i)
            appPermission!!.closeDialog()
            onBackPressed()
        }
    }

    companion object {
        const val MULTIPLE_PERMISSION_REQUEST = 130
    }
}