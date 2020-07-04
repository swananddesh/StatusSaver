package com.whatsap.statussaver.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.whatsap.statussaver.R;
import java.util.ArrayList;

/**
 * Created by Swanand Deshpande on 1/3/18.
 */

public class AppPermission implements AppPermissionInterface {

    public static final int PERMISSION_REQUEST_STORAGE = 109;
    private PermissionAlertDialog twoButtonDialog;
    private static AppPermission AppPermissionObject = new AppPermission();
    private AppPermission() {

    }

    public static AppPermission getInstance() {
        if (AppPermissionObject == null) {
            AppPermissionObject = new AppPermission();
        }
        return AppPermissionObject;
    }

    private String getPermissionName(int permission) {
        String permissionName = "";
        if (permission == PERMISSION_REQUEST_STORAGE) {
            permissionName = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        }
        return permissionName;
    }

    @Override
    public boolean requestPermissionList(Activity activity, int[] permissionCode, int requestCode) {
        boolean permissionFlag = false;
        ArrayList<String> permissionArray = new ArrayList<>();
        for (int value : permissionCode) {
            String permissionName = getPermissionName(value);
            if (ContextCompat.checkSelfPermission(activity, permissionName) != PackageManager.PERMISSION_GRANTED) {
                permissionArray.add(permissionName);
                permissionFlag = true;
            }
        }

        if (!permissionArray.isEmpty()) {
            String[] tempArray = new String[permissionArray.size()];
            permissionArray.toArray(tempArray);
            permissionArray.clear();
            ActivityCompat.requestPermissions(activity, tempArray,
                    requestCode);
        }
        return permissionFlag;
    }

    @Override
    public void requestPermissionList(Activity activity, @NonNull String[] permissionCode, int requestCode) {
        ArrayList<String> permissionArray = new ArrayList<>();

        for (String permissionName : permissionCode) {
            if (ContextCompat.checkSelfPermission(activity, permissionName) != PackageManager.PERMISSION_GRANTED) {
                permissionArray.add(permissionName);
            }
        }
        if (!permissionArray.isEmpty()) {
            String[] tempArray = new String[permissionArray.size()];
            permissionArray.toArray(tempArray);
            permissionArray.clear();
            ActivityCompat.requestPermissions(activity, tempArray,
                    requestCode);
        }
    }



    @Override
    public void showPermissionDialog(Activity activity, AlertDialogProperties alertDialogProperties, String[] permissionNames) {

        showDialog(activity, alertDialogProperties, permissionNames);
    }

    @Override
    public void closeDialog() {

        if (twoButtonDialog != null && twoButtonDialog.isShowing()) {
            twoButtonDialog.dismiss();
        }

    }

    private void showDialog(Activity activity, AlertDialogProperties alertDialogProperties, String[] permissionNames) {

        if (alertDialogProperties.getMessage() == null || alertDialogProperties.getMessage().length() == 0) {
            String message = "";

            for (String permissionName : permissionNames) {
                message = message + "&#8226;" + " ";
                if (permissionName.contains("STORAGE")) {

                    message = message + activity.getResources().getString(R.string.storage_permission);
                }
                message = message + "<br/>";
            }
            alertDialogProperties.setMessage(message);
        }

        twoButtonDialog = new PermissionAlertDialog(activity, alertDialogProperties);
        twoButtonDialog.show();
        twoButtonDialog.setCancelable(alertDialogProperties.isCancelable());
    }

}
