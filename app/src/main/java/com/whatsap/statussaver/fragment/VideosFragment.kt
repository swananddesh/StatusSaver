package com.whatsap.statussaver.fragment

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.whatsap.statussaver.R
import com.whatsap.statussaver.adapter.VideosRecyclerAdapter
import com.whatsap.statussaver.utils.AppConstants
import com.whatsap.statussaver.utils.ItemDecorationAlbumColumns
import java.io.File
import java.util.*

class VideosFragment : Fragment() {
    private var allVideoFiles: ArrayList<File>? = ArrayList()
    private val fileList: ArrayList<File>? = ArrayList()
    private var rvVideos: RecyclerView? = null
    private var imgInfoIcon: ImageView? = null
    private var txtInfo: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_videos, container, false)
        initView(view)
        allStatusVideos
        setAdapter()
        return view
    }

    private fun initView(view: View) {
        rvVideos = view.findViewById(R.id.rvVideos)
        imgInfoIcon = view.findViewById(R.id.img_info_icon)
        txtInfo = view.findViewById(R.id.txt_info_msg)
    }

    private fun setAdapter() {
        if (allVideoFiles != null && allVideoFiles!!.size > 0) {
            val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 2)
            rvVideos!!.layoutManager = layoutManager
            rvVideos!!.addItemDecoration(ItemDecorationAlbumColumns(resources.getDimensionPixelSize(R.dimen.dp_5), 2))
            val recyclerAdapter = VideosRecyclerAdapter(allVideoFiles!!, activity!!)
            rvVideos!!.adapter = recyclerAdapter
        } else {
            rvVideos!!.visibility = View.GONE
            imgInfoIcon!!.visibility = View.VISIBLE
            txtInfo!!.visibility = View.VISIBLE
        }
    }

    /**
     * Get all the whatsapp status videos
     */
    private val allStatusVideos: Unit
        private get() {
            try {
                val file = File(Environment.getExternalStorageDirectory().path
                        + AppConstants.PATH_TO_STATUSES_DIR)
                allVideoFiles = getFile(file)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

    /**
     * Get all the video files within specified directory, store into an arraylist
     * and return that arraylist.
     */
    private fun getFile(dir: File): ArrayList<File>? {
        val listOfFiles = dir.listFiles()
        if (listOfFiles != null && listOfFiles.size > 0) {
            fileList?.clear()
            for (listOfFile in listOfFiles) {
                if (listOfFile.isDirectory) {
                    fileList!!.add(listOfFile)
                    getFile(listOfFile)
                } else {
                    if (listOfFile.name.endsWith(".mp4")
                            || listOfFile.name.endsWith(".gif")) {
                        fileList!!.add(listOfFile)
                    }
                }
            }
        }
        return fileList
    }
}