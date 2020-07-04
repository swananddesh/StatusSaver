package com.whatsap.statussaver.permissions;

import android.app.Activity;

/**
 * Created by Swanand Deshpande on 1/3/18.
 */

public interface AppPermissionInterface {
    boolean requestPermissionList(Activity activity, int[] permissionCode, int requestCode);
    void requestPermissionList(Activity activity, String[] permissionCode, int requestCode);
    void showPermissionDialog(Activity activity, AlertDialogProperties alertDialogProperties, String[] permissionNames);
    void closeDialog();
}
