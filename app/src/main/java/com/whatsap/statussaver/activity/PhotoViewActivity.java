package com.whatsap.statussaver.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.whatsap.statussaver.BuildConfig;
import com.whatsap.statussaver.R;
import com.whatsap.statussaver.utils.AppConstants;

import java.io.File;
import java.io.FileOutputStream;

public class PhotoViewActivity extends AppCompatActivity {

    private Activity activity;
    private ImageView statusImage;
    private String selectedPath;
    private ImageButton shareButton;
    private ImageButton downloadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo_view);

        activity = this;

        getIntentData();

        initUI();

        setStatusImage();

        setListeners();
    }

    /**
     * this method will fetch the data passed to activity
     */
    private void getIntentData() {

        Intent dataIntent = getIntent();

        if(dataIntent != null) {

            selectedPath = dataIntent.getExtras().getString(AppConstants.IMAGE_PATH);

        }
    }

    /**
     * This method will initializes the view components
     */
    private void initUI() {

        statusImage = findViewById(R.id.img_complete_photo_view);

        shareButton = findViewById(R.id.ibtn_share);

        downloadButton = findViewById(R.id.ibtn_download);
    }

    private void setListeners() {

        shareButton.setOnClickListener(new ShareClickListener());

        downloadButton.setOnClickListener(new DownloadImageClickListener());
    }

    private class ShareClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            File shareFile = new File(selectedPath);

            Uri uriToImage = FileProvider.getUriForFile(
                    PhotoViewActivity.this, BuildConfig.APPLICATION_ID + ".provider", shareFile);

            Intent shareIntent = ShareCompat.IntentBuilder.from(PhotoViewActivity.this)
                    .setStream(uriToImage)
                    .setType("image/*")
                    .getIntent();

            // Provide read access
            shareIntent.setData(uriToImage);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
            shareIntent.setType("image/jpeg");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(shareIntent);
        }
    }

    private void setStatusImage() {

        RequestOptions options = new RequestOptions();
        options.centerInside();

        Glide.with(PhotoViewActivity.this)
                .load(selectedPath)
                .apply(options)
                .into(statusImage);
    }


    private class DownloadImageClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            Bitmap imageToSave = BitmapFactory.decodeFile(selectedPath);

            // get file name from file path.
            String fileName = selectedPath.substring(selectedPath.lastIndexOf("/") + 1);

            if (imageToSave != null) {

                createDirectoryAndSaveImage(imageToSave, fileName, view);
            }
        }
    }

    private void createDirectoryAndSaveImage(Bitmap imageToSave, String fileName, View view) {

        boolean isImageSaved = false;

        File directory = new File(Environment.getExternalStorageDirectory(), "StatusSaver");

        if (!directory.exists()) {

            directory.mkdirs();
        }

        File file = new File(directory, fileName);

        if (file.exists()) {

            file.delete();
        }

        try{

            FileOutputStream fOut = new FileOutputStream(file);

            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, fOut);

            fOut.flush();

            fOut.close();

            isImageSaved = true;

            // MediaScanner is used to show respective image in media library as soon as
            // it is downloaded.

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

            intent.setData(Uri.fromFile(file));

            sendBroadcast(intent);

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        if (isImageSaved) {

            Snackbar snackbar;

            snackbar = Snackbar.make(view, activity.getResources().getString(R.string.msg_image_saved), Snackbar.LENGTH_LONG);

            View snackBarView = snackbar.getView();

            snackBarView.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimaryDark));

            TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);

            textView.setTextColor(activity.getResources().getColor(R.color.colorWhite));

            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_done_black_24dp, 0);

            snackbar.show();
        }

    }
}
