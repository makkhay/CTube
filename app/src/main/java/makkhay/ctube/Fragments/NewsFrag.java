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
import makkhay.ctube.adapter.NewsAdapter;
import makkhay.ctube.R;
import makkhay.ctube.util.YoutubeConnector;

public class NewsFrag extends Fragment implements AdapterView.OnItemSelectedListener {

  private RecyclerView mRecyclerView;
  private Handler mHandler;
  private List<MyVideo> myVideoList;
  private NewsAdapter mNewsAdapter;
  private View v;
  private Spinner mSpinner;
  private static final String[] paths = {"Latest", "Funny"};

  public NewsFrag() {
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

    v = inflater.inflate(R.layout.fragment_news, container, false);
    mRecyclerView = v.findViewById(R.id.recyclerViewNews);
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

  public String toString(){
    return "News";
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
        myVideoList = yc.getNews(filter);
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
    mNewsAdapter = new NewsAdapter(v.getContext(), myVideoList);
    mRecyclerView.setAdapter(mNewsAdapter);
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
