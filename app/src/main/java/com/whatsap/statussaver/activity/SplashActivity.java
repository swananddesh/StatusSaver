package com.whatsap.statussaver.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.whatsap.statussaver.R;
import com.whatsap.statussaver.permissions.AlertDialogProperties;
import com.whatsap.statussaver.permissions.AppPermission;
import java.util.ArrayList;
import static com.whatsap.statussaver.permissions.AppPermission.PERMISSION_REQUEST_STORAGE;

public class SplashActivity extends AppCompatActivity {

    private Activity activity;

    private boolean isDialogShowing = false;
    private AppPermission appPermission;
    private String[] permissionArray;
    private ArrayList<String> listRational;

    public static final int MULTIPLE_PERMISSION_REQUEST = 130;
    private AlertDialogProperties alertDialogProperties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        activity = this;

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isDialogShowing) {

            methodRequiresPermission();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        isDialogShowing = false;
    }

    /**
     * Ask for permission. If permission is allowed,
     * then & then only proceed.
     **/
    private void methodRequiresPermission() {

        appPermission = AppPermission.getInstance();

        int permission[] = {PERMISSION_REQUEST_STORAGE};

        if (!appPermission.requestPermissionList(activity, permission, MULTIPLE_PERMISSION_REQUEST)) {

            startAppProcess();
        }

    }

    /**
     * Method to go to LandingActivity*/
    private void startAppProcess() {

//        startActivity(new Intent(activity, LandingActivity.class));
//
//        finish();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LandingActivity.class));
                SplashActivity.this.finish();
            }
        }, 3000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        ArrayList<String> tempPermissionList;
        listRational = new ArrayList<>();

        switch (requestCode) {

            case MULTIPLE_PERMISSION_REQUEST:
                // If request is cancelled, the result arrays are empty.

                tempPermissionList = new ArrayList<>();

                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {

                        if (Build.VERSION.SDK_INT >= 23) {
                            boolean showRationale = shouldShowRequestPermissionRationale(permissions[i]);

                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {

                                if (!showRationale)
                                    listRational.add(permissions[i]);
                                else
                                    tempPermissionList.add(permissions[i]);

                            }
                        }
                    }

                    if (!tempPermissionList.isEmpty()) {
                        isDialogShowing = true;
                        String[] tempArray = new String[tempPermissionList.size()];
                        tempPermissionList.toArray(tempArray);
                        tempPermissionList.clear();
                        permissionArray = tempArray;
                        alertDialogProperties = new AlertDialogProperties();
                        alertDialogProperties.setButtontextColor(activity.getResources().getColor(R.color.colorWhite));
                        alertDialogProperties.setCancelable(false);
                        alertDialogProperties.setCancelButtonColor(activity.getResources().getColor(R.color.colorPrimary));
                        alertDialogProperties.setOkayButtonColor(activity.getResources().getColor(R.color.colorPrimary));
                        alertDialogProperties.setCancelButtonText(activity.getResources().getString(R.string.exit));
                        alertDialogProperties.setOkayButtonText(activity.getResources().getString(R.string.ok));
                        alertDialogProperties.setMessageTextColor(activity.getResources().getColor(R.color.colorBlack));
                        alertDialogProperties.setOkayButtonListener(new OKButtonListener());
                        alertDialogProperties.setCancelButtonListener(new CancelButtonListener());
                        appPermission.showPermissionDialog(activity, alertDialogProperties, permissionArray);

                    } else if (!listRational.isEmpty()) {
                        isDialogShowing = true;

                        String[] tempArray = new String[listRational.size()];
                        listRational.toArray(tempArray);
                        tempPermissionList.clear();
                        permissionArray = tempArray;
                        alertDialogProperties = new AlertDialogProperties();
                        alertDialogProperties.setButtontextColor(activity.getResources().getColor(R.color.colorWhite));
                        alertDialogProperties.setCancelable(false);
                        alertDialogProperties.setCancelButtonColor(activity.getResources().getColor(R.color.colorPrimary));
                        alertDialogProperties.setOkayButtonColor(activity.getResources().getColor(R.color.colorPrimary));
                        alertDialogProperties.setCancelButtonText(activity.getResources().getString(R.string.exit));
                        alertDialogProperties.setOkayButtonText(activity.getResources().getString(R.string.app_settings));
                        alertDialogProperties.setMessageTextColor(activity.getResources().getColor(R.color.colorBlack));
                        alertDialogProperties.setOkayButtonListener(new GoToSettings());
                        alertDialogProperties.setCancelButtonListener(new CancelButtonListener());
                        appPermission.showPermissionDialog(activity, alertDialogProperties, permissionArray);

                    } else {

                        startAppProcess();
                    }

                }
        }

    }


    /**
     * Ask for permission again click on OK
     **/
    class OKButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            appPermission.requestPermissionList(activity, permissionArray, MULTIPLE_PERMISSION_REQUEST);
            appPermission.closeDialog();
        }
    }


    class CancelButtonListener implements View.OnClickListener {


        @Override
        public void onClick(View v) {

            appPermission.closeDialog();
            onBackPressed();
        }
    }

    private class GoToSettings implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            Intent i = new Intent();
            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            i.addCategory(Intent.CATEGORY_DEFAULT);
            i.setData(Uri.parse("package:" + activity.getPackageName()));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            startActivity(i);
            appPermission.closeDialog();
            onBackPressed();

        }
    }
}
