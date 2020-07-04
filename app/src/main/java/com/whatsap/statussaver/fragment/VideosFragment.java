package com.whatsap.statussaver.fragment;


import android.os.Bundle;
import android.os.Environment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.whatsap.statussaver.R;
import com.whatsap.statussaver.adapter.VideosRecyclerAdapter;
import com.whatsap.statussaver.utils.AppConstants;
import com.whatsap.statussaver.utils.ItemDecorationAlbumColumns;
import java.io.File;
import java.util.ArrayList;


public class VideosFragment extends Fragment {

    private ArrayList<File> allVideoFiles = new ArrayList<>();
    private ArrayList<File> fileList = new ArrayList<>();
    private RecyclerView rvVideos;
    private ImageView imgInfoIcon;
    private TextView txtInfo;


    public VideosFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_videos, container, false);
        initView(view);
        getAllStatusVideos();
        setAdapter();
        return view;
    }


    private void initView(View view) {
        rvVideos = view.findViewById(R.id.rvVideos);
        imgInfoIcon = view.findViewById(R.id.img_info_icon);
        txtInfo = view.findViewById(R.id.txt_info_msg);
    }

    private void setAdapter() {
        if (allVideoFiles != null && allVideoFiles.size() > 0) {
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
            rvVideos.setLayoutManager(layoutManager);
            rvVideos.addItemDecoration(new ItemDecorationAlbumColumns(getResources().getDimensionPixelSize(R.dimen.dp_5),2));
            VideosRecyclerAdapter recyclerAdapter = new VideosRecyclerAdapter(allVideoFiles, getActivity());
            rvVideos.setAdapter(recyclerAdapter);
        } else {
            rvVideos.setVisibility(View.GONE);
            imgInfoIcon.setVisibility(View.VISIBLE);
            txtInfo.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Get all the whatsapp status videos
     **/
    private void getAllStatusVideos() {
        try {
            File file = new File(Environment.getExternalStorageDirectory().getPath()
                    + AppConstants.PATH_TO_STATUSES_DIR);

            allVideoFiles = getFile(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    /**
     * Get all the video files within specified directory, store into an arraylist
     * and return that arraylist.
     **/
    private ArrayList<File> getFile(File dir) {
        File[] listOfFiles = dir.listFiles();
        if (listOfFiles != null && listOfFiles.length > 0) {
            if (fileList != null) {
                fileList.clear();
            }

            for (File listOfFile : listOfFiles) {
                if (listOfFile.isDirectory()) {
                    fileList.add(listOfFile);
                    getFile(listOfFile);
                } else {
                    if (listOfFile.getName().endsWith(".mp4")
                            || listOfFile.getName().endsWith(".gif")) {

                        fileList.add(listOfFile);
                    }
                }
            }
        }
        return fileList;
    }

}
