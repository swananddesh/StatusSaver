package com.whatsap.statussaver.adapter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.whatsap.statussaver.R
import com.whatsap.statussaver.adapter.VideosRecyclerAdapter.VideoViewHolder
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel
import java.util.*

/**
 * Created by Swanand Deshpande on 25/3/18.
 */
class VideosRecyclerAdapter(private val videosFilesList: ArrayList<File>, var activity: Activity) : RecyclerView.Adapter<VideoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val inflatedView = LayoutInflater.from(parent.context)
                .inflate(R.layout.videos_item_view, parent, false)
        return VideoViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val currentFile = videosFilesList[position]
        Glide.with(activity)
                .load(Uri.fromFile(File(currentFile.absolutePath)))
                .into(holder.imgVideoThumbnail)
        holder.txtMenuOptions.setOnClickListener(OptionMenuClickListener(holder, currentFile))
    }

    /**
     * Popup will appear when click on Menu options.
     */
    private inner class OptionMenuClickListener(private val holder: VideoViewHolder, private val currentFile: File) : View.OnClickListener {
        override fun onClick(view: View) {
            val popupMenu = PopupMenu(activity, holder.txtMenuOptions)
            popupMenu.inflate(R.menu.menu_options)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menuShare -> shareVideo(currentFile)
                    R.id.menuSave -> {
                        try {
                            saveVideo(currentFile, File(Environment.getExternalStorageDirectory(), "StatusSaver Videos/" + currentFile.name), activity)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        val snackbar: Snackbar
                        snackbar = Snackbar.make(view, activity.resources.getString(R.string.msg_video_saved), Snackbar.LENGTH_LONG)
                        val snackBarView = snackbar.view
                        snackBarView.setBackgroundColor(activity.resources.getColor(R.color.colorPrimaryDark))
                        val textView = snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                        textView.setTextColor(activity.resources.getColor(R.color.colorWhite))
                        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done_black_24dp, 0)
                        snackbar.show()
                    }
                }
                false
            }
            popupMenu.show()
        }

    }

    /**
     * Function to share video
     */
    private fun shareVideo(selectedVideoFileToShare: File) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "video/*"
        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(selectedVideoFileToShare))
        activity.startActivity(Intent.createChooser(sharingIntent, ""))
    }

    /**
     * Function to save video to device
     */
    @Throws(IOException::class)
    private fun saveVideo(sourceFile: File, destFile: File, activity: Activity) {
        if (!destFile.parentFile.exists()) {
            destFile.parentFile.mkdirs()
        }
        if (!destFile.exists()) {
            destFile.createNewFile()
        }
        var source: FileChannel? = null
        var destination: FileChannel? = null
        try {
            source = FileInputStream(sourceFile).channel
            destination = FileOutputStream(destFile).channel
            destination.transferFrom(source, 0, source.size())
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val contentUri = Uri.fromFile(destFile)
            mediaScanIntent.data = contentUri
            activity.sendBroadcast(mediaScanIntent)
        } finally {
            source?.close()
            destination?.close()
        }
    }

    override fun getItemCount(): Int {
        return videosFilesList.size
    }

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgVideoThumbnail: ImageView
        val txtMenuOptions: TextView

        init {
            imgVideoThumbnail = itemView.findViewById(R.id.imgVideoThumbnail)
            txtMenuOptions = itemView.findViewById(R.id.txtMenuOptions)
        }
    }

}