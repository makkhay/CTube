package makkhay.ctube.Fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import makkhay.ctube.Model.MyVideo;
import makkhay.ctube.adapter.VideoAdapter;
import makkhay.ctube.R;
import makkhay.ctube.util.YoutubeConnector;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFrag extends Fragment implements AdapterView.OnItemSelectedListener {
    private RecyclerView mRecyclerView;
    private Handler mHandler;
    private List<MyVideo> myVideoList;
    private VideoAdapter mVideoAdapter;
    private View v;
    private Spinner mSpinner;
    private static final String[] paths = {"Latest", "Funny"};

    public VideoFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.video_fragment, container, false);
        mRecyclerView = v.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        // Inflate the layout for this fragment

        mSpinner = (Spinner) v.findViewById(R.id.filterSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
            android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);



        searchOnYoutube("fox news");
        myVideoList = new ArrayList<MyVideo>();
        mHandler = new Handler();
        return v;
    }
    /**
     * This function searches videos thru the youtube API when a keyword is passed.
     *
     * @param
     */

    private void searchOnYoutube(final String filter) {
        new Thread(){
            @Override
            public void run() {
                YoutubeConnector yc = new YoutubeConnector(v.getContext());
                myVideoList = yc.search(filter);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        updateVideosFound();
                    }
                });
            }
        }.start();
    }

    /**
     * This function updates the view when videos are found
     */
    private void updateVideosFound(){
        mVideoAdapter = new VideoAdapter(v.getContext(), myVideoList);
        mRecyclerView.setAdapter(mVideoAdapter);
    }


    public String toString(){
        return "Videos";
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected
                searchOnYoutube("fox news");
                Toast.makeText(getContext(),"Loading, please wait :)", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                searchOnYoutube("fox news funny");
                Toast.makeText(getContext(),"Loading, please wait :)", Toast.LENGTH_SHORT).show();

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
