package com.whatsap.statussaver.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.multidex.BuildConfig
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.whatsap.statussaver.R
import com.whatsap.statussaver.utils.AppConstants
import java.io.File
import java.io.FileOutputStream

class PhotoViewActivity : AppCompatActivity() {
    private var activity: Activity? = null
    private var statusImage: ImageView? = null
    private var selectedPath: String? = null
    private var shareButton: ImageButton? = null
    private var downloadButton: ImageButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_view)
        activity = this
        intentData
        initUI()
        setStatusImage()
        setListeners()
    }

    /**
     * this method will fetch the data passed to activity
     */
    private val intentData: Unit
        private get() {
            val dataIntent = intent
            if (dataIntent != null) {
                selectedPath = dataIntent.extras!!.getString(AppConstants.IMAGE_PATH)
            }
        }

    /**
     * This method will initializes the view components
     */
    private fun initUI() {
        statusImage = findViewById(R.id.img_complete_photo_view)
        shareButton = findViewById(R.id.ibtn_share)
        downloadButton = findViewById(R.id.ibtn_download)
    }

    private fun setListeners() {
        shareButton!!.setOnClickListener(ShareClickListener())
        downloadButton!!.setOnClickListener(DownloadImageClickListener())
    }

    private inner class ShareClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            val shareFile = File(selectedPath)
            val uriToImage = FileProvider.getUriForFile(
                    this@PhotoViewActivity, BuildConfig.APPLICATION_ID + ".provider", shareFile)
            val shareIntent = ShareCompat.IntentBuilder.from(this@PhotoViewActivity)
                    .setStream(uriToImage)
                    .setType("image/*")
                    .intent

            // Provide read access
            shareIntent.data = uriToImage
            shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage)
            shareIntent.type = "image/jpeg"
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(shareIntent)
        }
    }

    private fun setStatusImage() {
        val options = RequestOptions()
        options.centerInside()
        Glide.with(this@PhotoViewActivity)
                .load(selectedPath)
                .apply(options)
                .into(statusImage!!)
    }

    private inner class DownloadImageClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            val imageToSave = BitmapFactory.decodeFile(selectedPath)

            // get file name from file path.
            val fileName = selectedPath!!.substring(selectedPath!!.lastIndexOf("/") + 1)
            imageToSave?.let { createDirectoryAndSaveImage(it, fileName, view) }
        }
    }

    private fun createDirectoryAndSaveImage(imageToSave: Bitmap, fileName: String, view: View) {
        var isImageSaved = false
        val directory = File(Environment.getExternalStorageDirectory(), "StatusSaver")
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val file = File(directory, fileName)
        if (file.exists()) {
            file.delete()
        }
        try {
            val fOut = FileOutputStream(file)
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
            fOut.flush()
            fOut.close()
            isImageSaved = true

            // MediaScanner is used to show respective image in media library as soon as
            // it is downloaded.
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            intent.data = Uri.fromFile(file)
            sendBroadcast(intent)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        if (isImageSaved) {
            val snackbar = Snackbar.make(view, activity!!.resources.getString(R.string.msg_image_saved), Snackbar.LENGTH_LONG)
            val snackBarView = snackbar.view
            snackBarView.setBackgroundColor(activity!!.resources.getColor(R.color.colorPrimaryDark))
            val textView = snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            textView.setTextColor(activity!!.resources.getColor(R.color.colorWhite))
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done_black_24dp, 0)
            snackbar.show()
        }
    }
}