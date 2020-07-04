package com.whatsap.statussaver.permissions

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.whatsap.statussaver.R
import java.util.*

/**
 * Created by Swanand Deshpande on 1/3/18.
 */
class AppPermission private constructor() : AppPermissionInterface {
    private lateinit var twoButtonDialog: PermissionAlertDialog
    private fun getPermissionName(permission: Int): String {
        var permissionName = ""
        if (permission == PERMISSION_REQUEST_STORAGE) {
            permissionName = Manifest.permission.WRITE_EXTERNAL_STORAGE
        }
        return permissionName
    }

    override fun requestPermissionList(activity: Activity?, permissionCode: IntArray?, requestCode: Int): Boolean {
        var permissionFlag = false
        val permissionArray = ArrayList<String>()
        if (permissionCode != null) {
            for (value in permissionCode) {
                val permissionName = getPermissionName(value)
                if (activity?.let { ContextCompat.checkSelfPermission(it, permissionName) } != PackageManager.PERMISSION_GRANTED) {
                    permissionArray.add(permissionName)
                    permissionFlag = true
                }
            }
        }
        if (!permissionArray.isEmpty()) {
            val tempArray = arrayOfNulls<String>(permissionArray.size)
            permissionArray.toArray(tempArray)
            permissionArray.clear()
            activity?.let {
                ActivityCompat.requestPermissions(it, tempArray,
                        requestCode)
            }
        }
        return permissionFlag

    }

    override fun requestPermissionList(activity: Activity?, permissionCode: Array<String?>?, requestCode: Int) {
        val permissionArray = ArrayList<String>()
        if (permissionCode != null) {
            for (permissionName in permissionCode) {
                if (ContextCompat.checkSelfPermission(activity!!, permissionName!!) != PackageManager.PERMISSION_GRANTED) {
                    permissionArray.add(permissionName)
                }
            }
        }
        if (permissionArray.isNotEmpty()) {
            val tempArray = arrayOfNulls<String>(permissionArray.size)
            permissionArray.toArray(tempArray)
            permissionArray.clear()
            activity?.let {
                ActivityCompat.requestPermissions(it, tempArray,
                        requestCode)
            }
        }

    }

    override fun showPermissionDialog(activity: Activity?, alertDialogProperties: AlertDialogProperties?, permissionNames: Array<String?>?) {
        if (activity != null) {
            if (alertDialogProperties != null) {
                showDialog(activity, alertDialogProperties, permissionNames)
            }
        }
    }


    override fun closeDialog() {
        if (twoButtonDialog != null && twoButtonDialog!!.isShowing) {
            twoButtonDialog!!.dismiss()
        }
    }

    private fun showDialog(activity: Activity, alertDialogProperties: AlertDialogProperties, permissionNames: Array<String?>?) {
        if (alertDialogProperties.message == null || alertDialogProperties.message!!.isEmpty()) {
            var message = ""
            if (permissionNames != null) {
                for (permissionName in permissionNames) {
                    message = "$message&#8226; "
                    if (permissionName?.contains("STORAGE")!!) {
                        message += activity.resources.getString(R.string.storage_permission)
                    }
                    message = "$message<br/>"
                }
            }
            alertDialogProperties.message = message
        }
        twoButtonDialog = PermissionAlertDialog(activity, alertDialogProperties)
        twoButtonDialog.show()
        twoButtonDialog.setCancelable(alertDialogProperties.isCancelable)
    }

    companion object {
        const val PERMISSION_REQUEST_STORAGE = 109
        private var AppPermissionObject: AppPermission? = AppPermission()
        val instance: AppPermission?
            get() {
                if (AppPermissionObject == null) {
                    AppPermissionObject = AppPermission()
                }
                return AppPermissionObject
            }
    }
}