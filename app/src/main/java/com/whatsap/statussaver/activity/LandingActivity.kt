package com.whatsap.statussaver.activity

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.whatsap.statussaver.R
import com.whatsap.statussaver.adapter.ViewPagerAdapter
import com.whatsap.statussaver.fragment.PhotosFragment
import com.whatsap.statussaver.fragment.VideosFragment

class LandingActivity : AppCompatActivity() {
    private var activity: Activity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        activity = this
        initUI()
    }

    private fun initUI() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val viewPager = findViewById<ViewPager>(R.id.viewpager)
        setUpViewPager(viewPager)
        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun setUpViewPager(viewPager: ViewPager) {
        val pagerAdapter = ViewPagerAdapter(supportFragmentManager)
        pagerAdapter.addFragment(PhotosFragment(), activity!!.resources.getString(R.string.photos))
        pagerAdapter.addFragment(VideosFragment(), activity!!.resources.getString(R.string.videos))
        viewPager.adapter = pagerAdapter
    }

    /**
     * Exit the app on back pressed
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitApp()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun exitApp() {
        val alertBox = AlertDialog.Builder(activity!!)
                .setTitle(activity!!.resources.getString(R.string.title_exit_app))
                .setMessage(activity!!.resources.getString(R.string.msg_exit_app))
                .setPositiveButton(activity!!.resources.getString(R.string.yes)
                ) { arg0, arg1 -> finish() }
                .setNegativeButton(activity!!.resources.getString(R.string.no)
                ) { arg0, arg1 -> }.create()
        alertBox.show()
        alertBox.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(activity!!, R.color.colorPrimaryDark))
        alertBox.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(activity!!, R.color.colorPrimaryDark))
        alertBox.setCancelable(false)
    }
}