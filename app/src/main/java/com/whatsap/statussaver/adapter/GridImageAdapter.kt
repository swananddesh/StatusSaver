package com.whatsap.statussaver.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.whatsap.statussaver.R
import java.io.File
import kotlin.collections.ArrayList

/**
 * Created by Swanand Deshpande on 25/2/18.
 */
class GridImageAdapter(activity: Activity?, fileList: ArrayList<File>?) : BaseAdapter() {

    private var activity:Activity? = null
    private var fileList: ArrayList<File>? = null
    private var mLayoutInflater: LayoutInflater? = null

    init {
        this.activity = activity
        this.mLayoutInflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.fileList = fileList
    }

    override fun getCount(): Int {
        return fileList?.size!!
    }

    override fun getItem(position: Int): File? {
        return fileList?.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup): View? {
        val view: View?
        val holder: ViewHolder

        if (convertView == null){
            view = this.mLayoutInflater?.inflate(R.layout.photo_grid_item_layout, null)
            holder = ViewHolder(view)
            holder.image = view?.findViewById(R.id.img_photo)
            holder.position = position
            view?.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        holder.image?.scaleType = ImageView.ScaleType.CENTER_CROP
        setBitmap(holder, position)
        return view
    }

    /**
     * ViewHolder
     */
    private class ViewHolder(row: View?) {
        var image: ImageView? = null
        var position = 0
        init {
            this.image = row?.findViewById(R.id.img_photo) as ImageView
        }
    }

    private fun setBitmap(holder: ViewHolder, position: Int) {
        activity?.let {
            holder.image?.let { it1 ->
                Glide.with(it)
                    .load(fileList?.get(position)?.absolutePath)
                    .into(it1)
            }
        }
    }

}