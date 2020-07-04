package com.whatsap.statussaver.permissions;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.TextView;
import com.whatsap.statussaver.R;
import com.whatsap.statussaver.utils.AppConstants;


/**
 * A custom alert dialog have been created that will give information,
 * why we need that particular permission.
 **/
public class PermissionAlertDialog extends AlertDialog {

    private Typeface typeFaceMyriad;
    private Activity activity;
    private String okayMsg;
    private String cancelMsg;
    private String message;
    private AlertDialogProperties alertDialogProperties;

    public PermissionAlertDialog(Activity activity, AlertDialogProperties alertDialogProperties) {

        super(activity);
        this.activity = activity;
        this.message = alertDialogProperties.getMessage();
        this.okayMsg = alertDialogProperties.getOkayButtonText();
        this.cancelMsg = alertDialogProperties.getCancelButtonText();
        this.alertDialogProperties = alertDialogProperties;
        typeFaceMyriad = Typeface.create(AppConstants.FONT_TYPE_VARELA_ROUND, Typeface.NORMAL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.permission_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        initUI();
    }


    private void initUI() {
        TextView txtNotificationMsg = findViewById(R.id.txt_common_msg);
        if (alertDialogProperties.getMessageTextColor() == 0)
            txtNotificationMsg.setTextColor(activity.getResources().getColor(R.color.colorBlack));
        else
            txtNotificationMsg.setTextColor(alertDialogProperties.getMessageTextColor());

        Button btnCancel = findViewById(R.id.btn_cancel);
        Button btnOkay = findViewById(R.id.btn_okay);

        btnOkay.setTypeface(typeFaceMyriad);
        btnCancel.setTypeface(typeFaceMyriad);
        txtNotificationMsg.setTypeface(typeFaceMyriad);

        if (alertDialogProperties.getCancelButtonColor() == 0)
            btnCancel.setBackgroundColor(activity.getResources().getColor(R.color.common_negative_action_button_text_color));
        else
            btnCancel.setBackgroundColor(alertDialogProperties.getCancelButtonColor());

        if (alertDialogProperties.getCancelButtonColor() == 0)
            btnOkay.setBackgroundColor(activity.getResources().getColor(R.color.common_negative_action_button_text_color));
        else
            btnOkay.setBackgroundColor(alertDialogProperties.getOkayButtonColor());

        if (alertDialogProperties.getCancelButtonColor() == 0)
            btnCancel.setTextColor(activity.getResources().getColor(R.color.colorBlack));
        else
            btnCancel.setTextColor(alertDialogProperties.getButtontextColor());

        if (alertDialogProperties.getCancelButtonColor() == 0)
            btnOkay.setTextColor(activity.getResources().getColor(R.color.colorBlack));
        else
            btnOkay.setTextColor(alertDialogProperties.getButtontextColor());

        btnOkay.setOnClickListener(alertDialogProperties.getOkayButtonListener());
        btnCancel.setOnClickListener(alertDialogProperties.getCancelButtonListener());

        txtNotificationMsg.setText(Html.fromHtml(message));
        if (okayMsg == null || okayMsg.length() == 0)
            okayMsg = activity.getResources().getString(R.string.ok);
        if (cancelMsg == null || cancelMsg.length() == 0)
            okayMsg = activity.getResources().getString(R.string.cancel);
        btnOkay.setText(okayMsg);
        btnCancel.setText(cancelMsg);
    }
}
