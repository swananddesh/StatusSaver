package com.whatsap.statussaver.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.whatsap.statussaver.R;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 * Created by Swanand Deshpande on 25/3/18.
 */

public class VideosRecyclerAdapter extends RecyclerView.Adapter<VideosRecyclerAdapter.VideoViewHolder> {

    private ArrayList<File> videosFilesList;
    Activity activity;

    public VideosRecyclerAdapter(ArrayList<File> videosFilesList, Activity activity) {

        this.videosFilesList = videosFilesList;

        this.activity = activity;

    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.videos_item_view, parent, false);

        return new VideoViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(final VideoViewHolder holder, int position) {

        final File currentFile = videosFilesList.get(position);
        Glide.with(activity)
                .load(Uri.fromFile(new File(currentFile.getAbsolutePath())))
                .into(holder.imgVideoThumbnail);

        holder.txtMenuOptions.setOnClickListener(new OptionMenuClickListener(holder, currentFile));
    }

    /**
     * Popup will appear when click on Menu options.
     **/
    private class OptionMenuClickListener implements View.OnClickListener {
        private VideoViewHolder holder;
        private File currentFile;

        public OptionMenuClickListener(VideoViewHolder holder, File currentFile) {
            this.holder = holder;
            this.currentFile = currentFile;
        }

        @Override
        public void onClick(final View view) {
            PopupMenu popupMenu = new PopupMenu(activity, holder.txtMenuOptions);
            popupMenu.inflate(R.menu.menu_options);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    switch (menuItem.getItemId()) {
                        case R.id.menuShare:
                            shareVideo(currentFile);
                            break;

                        case R.id.menuSave:
                            try {
                                saveVideo(currentFile, new File(Environment.getExternalStorageDirectory() ,"StatusSaver Videos/" + currentFile.getName()), activity);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Snackbar snackbar;
                            snackbar = Snackbar.make(view, activity.getResources().getString(R.string.msg_video_saved), Snackbar.LENGTH_LONG);
                            View snackBarView = snackbar.getView();
                            snackBarView.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimaryDark));
                            TextView textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
                            textView.setTextColor(activity.getResources().getColor(R.color.colorWhite));
                            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_done_black_24dp, 0);
                            snackbar.show();
                            break;
                    }
                    return false;
                }
            });

            popupMenu.show();

        }
    }

    /**
     * Function to share video
     **/
    private void shareVideo(File selectedVideoFileToShare) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("video/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(selectedVideoFileToShare));
        activity.startActivity(Intent.createChooser(sharingIntent, ""));
    }

    /**
     * Function to save video to device
     **/

    private void saveVideo(File sourceFile, File destFile, Activity activity) throws IOException {

        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(destFile);
            mediaScanIntent.setData(contentUri);
            activity.sendBroadcast(mediaScanIntent);

        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    @Override
    public int getItemCount() {
        return videosFilesList.size();
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgVideoThumbnail;
        private TextView txtMenuOptions;

        public VideoViewHolder(View itemView) {
            super(itemView);

            imgVideoThumbnail = itemView.findViewById(R.id.imgVideoThumbnail);
            txtMenuOptions = itemView.findViewById(R.id.txtMenuOptions);
        }
    }
}
