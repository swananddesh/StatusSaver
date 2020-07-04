package com.whatsap.statussaver.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.whatsap.statussaver.R
import com.whatsap.statussaver.activity.PhotoViewActivity
import com.whatsap.statussaver.adapter.GridImageAdapter
import com.whatsap.statussaver.utils.AppConstants
import java.io.File
import java.util.*

class PhotosFragment : Fragment() {
    private val fileList: ArrayList<File>? = ArrayList()
    private var allImageFiles: ArrayList<File>? = ArrayList()
    private var gvPhotos: GridView? = null
    private var imgInfoIcon: ImageView? = null
    private var txtInfo: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_photos, container, false)
        initView(view)
        allStatusPhotos
        setAdapter()
        setListener()
        return view
    }

    private fun initView(view: View) {
        gvPhotos = view.findViewById(R.id.gv_photos)
        imgInfoIcon = view.findViewById(R.id.img_info_icon)
        txtInfo = view.findViewById(R.id.txt_info_msg)
    }

    private fun setListener() {
        gvPhotos!!.onItemClickListener = ImageClickListener()
    }

    private fun setAdapter() {
        if (allImageFiles != null && allImageFiles!!.size > 0) {
            val gridImageAdapter = GridImageAdapter(activity!!, allImageFiles)
            gvPhotos!!.adapter = gridImageAdapter
        } else {
            gvPhotos!!.visibility = View.GONE
            imgInfoIcon!!.visibility = View.VISIBLE
            txtInfo!!.visibility = View.VISIBLE
        }
    }

    private inner class ImageClickListener : OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val file = parent.getItemAtPosition(position) as File
            //Sending image id to PhotoViewActivity
            val intent = Intent(activity, PhotoViewActivity::class.java)
            intent.putExtra(AppConstants.IMAGE_PATH, file.absolutePath)
            startActivity(intent)
        }
    }

    /**
     * Get all the whatsapp status photos
     */
    private val allStatusPhotos: Unit
        private get() {
            try {
                val file = File(Environment.getExternalStorageDirectory().path
                        + AppConstants.PATH_TO_STATUSES_DIR)
                allImageFiles = getFile(file)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

    /**
     * Get all the image files within specified directory, store into an arraylist
     * and return that arraylist.
     * @param dir
     */
    private fun getFile(dir: File): ArrayList<File>? {
        val listOfFiles = dir.listFiles()
        if (listOfFiles != null && listOfFiles.size > 0) {
            fileList?.clear()
            for (index in listOfFiles.indices) {
                if (listOfFiles[index].isDirectory) {
                    fileList!!.add(listOfFiles[index])
                    getFile(listOfFiles[index])
                } else {
                    if (listOfFiles[index].name.endsWith(".png")
                            || listOfFiles[index].name.endsWith(".jpeg")
                            || listOfFiles[index].name.endsWith(".jpg")
                            || listOfFiles[index].name.endsWith(".gif")) {
                        fileList!!.add(listOfFiles[index])
                    }
                }
            }
        }
        return fileList
    }
}