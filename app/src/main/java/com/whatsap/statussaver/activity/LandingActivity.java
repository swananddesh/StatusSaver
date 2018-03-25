package com.whatsap.statussaver.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import com.whatsap.statussaver.R;
import com.whatsap.statussaver.adapter.ViewPagerAdapter;
import com.whatsap.statussaver.fragment.PhotosFragment;
import com.whatsap.statussaver.fragment.VideosFragment;

public class LandingActivity extends AppCompatActivity {

    private Activity activity;

    private Toolbar toolbar;

    private TabLayout tabLayout;

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_landing);

        activity = this;

        initUI();

    }

    private void initUI() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewpager);
        setUpViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setUpViewPager(ViewPager viewPager) {

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        pagerAdapter.addFragment(new PhotosFragment(), activity.getResources().getString(R.string.photos));
        pagerAdapter.addFragment(new VideosFragment(), activity.getResources().getString(R.string.videos));

        viewPager.setAdapter(pagerAdapter);

    }

    /**
     * Exit the app on back pressed
     **/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            exitApp();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    private void exitApp() {

        AlertDialog alertBox = new AlertDialog.Builder(activity)
                .setTitle(activity.getResources().getString(R.string.title_exit_app))
                .setMessage(activity.getResources().getString(R.string.msg_exit_app))
                .setPositiveButton(activity.getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {

                        finish();

                    }
                })
                .setNegativeButton(activity.getResources().getString(R.string.no),
                        new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {

                    }

                }).create();

        alertBox.show();

        alertBox.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
        alertBox.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
        alertBox.setCancelable(false);
    }
}
