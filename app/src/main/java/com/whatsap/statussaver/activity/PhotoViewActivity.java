package com.whatsap.statussaver.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.whatsap.statussaver.BuildConfig;
import com.whatsap.statussaver.R;
import com.whatsap.statussaver.utils.AppConstants;

import java.io.File;

public class PhotoViewActivity extends AppCompatActivity {

    private ImageView statusImage;
    private String selectedPath;
    private ImageButton shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo_view);

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

    }

    private void setListeners() {

        shareButton.setOnClickListener(new ShareClickListener());
    }

    private class ShareClickListener implements View.OnClickListener {


        @Override
        public void onClick(View view) {

            File shareFile = new File(selectedPath);

            Uri uriToImage = FileProvider.getUriForFile(
                    PhotoViewActivity.this, BuildConfig.APPLICATION_ID+".provider", shareFile);

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



}
