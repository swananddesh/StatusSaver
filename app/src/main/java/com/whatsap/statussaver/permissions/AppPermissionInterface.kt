package com.whatsap.statussaver.permissions

import android.app.Activity

/**
 * Created by Swanand Deshpande on 1/3/18.
 */
interface AppPermissionInterface {
    fun requestPermissionList(activity: Activity?, permissionCode: IntArray?, requestCode: Int): Boolean
    fun requestPermissionList(activity: Activity?, permissionCode: Array<String?>?, requestCode: Int)
    fun showPermissionDialog(activity: Activity?, alertDialogProperties: AlertDialogProperties?, permissionNames: Array<String?>?)
    fun closeDialog()
}