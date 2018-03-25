package com.whatsap.statussaver.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.whatsap.statussaver.R;
import com.whatsap.statussaver.activity.PhotoViewActivity;
import com.whatsap.statussaver.adapter.GridImageAdapter;
import com.whatsap.statussaver.utils.AppConstants;
import java.io.File;
import java.util.ArrayList;


public class PhotosFragment extends Fragment {

    private ArrayList<File> fileList = new ArrayList<>();

    private ArrayList<File> allImageFiles = new ArrayList<>();

    private File file;

    private GridView gvPhotos;

    private GridImageAdapter gridImageAdapter;

    private ImageView imgInfoIcon;

    private TextView txtInfo;


    public PhotosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_photos, container, false);

        initView(view);

        getAllStatusPhotos();

        setAdapter();

        setListener();

        return view;
    }

    private void initView(View view) {

        gvPhotos = (GridView) view.findViewById(R.id.gv_photos);

        imgInfoIcon = (ImageView) view.findViewById(R.id.img_info_icon);

        txtInfo = (TextView) view.findViewById(R.id.txt_info_msg);

    }

    private void setListener() {

        gvPhotos.setOnItemClickListener(new ImageClickListener());
    }

    private void setAdapter() {

        if (allImageFiles != null && allImageFiles.size() > 0) {

            gridImageAdapter = new GridImageAdapter(getActivity(), allImageFiles);

            gvPhotos.setAdapter(gridImageAdapter);

        } else {

            gvPhotos.setVisibility(View.GONE);

            imgInfoIcon.setVisibility(View.VISIBLE);

            txtInfo.setVisibility(View.VISIBLE);
        }
    }

    private class ImageClickListener implements AdapterView.OnItemClickListener {


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            File file = (File) parent.getItemAtPosition(position);

            //Sending image id to PhotoViewActivity

            Intent intent = new Intent(getActivity(), PhotoViewActivity.class);

            intent.putExtra(AppConstants.IMAGE_PATH, file.getAbsolutePath());

            startActivity(intent);

        }
    }

    /**
     * Get all the whatsapp status photos
     **/
    private void getAllStatusPhotos() {

        try {

            file = new File(Environment.getExternalStorageDirectory().getPath()
            + AppConstants.PATH_TO_STATUSES_DIR);

            allImageFiles = getFile(file);

        } catch (Exception ex) {

            ex.printStackTrace();
        }

    }


    /**
     * Get all the image files within specified directory, store into an arraylist
     * and return that arraylist.
     * @param dir
     **/
    private ArrayList<File> getFile(File dir) {

        File listOfFiles[] = dir.listFiles();

        if (listOfFiles != null && listOfFiles.length > 0) {

            if (fileList != null) {

                fileList.clear();
            }

            for (int index = 0; index < listOfFiles.length; index++) {

                if (listOfFiles[index].isDirectory()) {

                    fileList.add(listOfFiles[index]);

                    getFile(listOfFiles[index]);

                } else {

                    if (listOfFiles[index].getName().endsWith(".png")
                            || listOfFiles[index].getName().endsWith(".jpeg")
                            || listOfFiles[index].getName().endsWith(".jpg")
                            || listOfFiles[index].getName().endsWith(".gif")) {

                        fileList.add(listOfFiles[index]);
                    }
                }
            }
        }

        return fileList;
    }

}
