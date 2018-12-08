package makkhay.ctube.ui;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import makkhay.ctube.Model.MyVideo;
import makkhay.ctube.R;
import makkhay.ctube.util.YoutubeConnector;
import makkhay.ctube.adapter.NewsDetailAdapter;

public class NewsActivity extends AppCompatActivity {
  private List<MyVideo> myVideoList;
  private Handler handler;
  private String videoId;
  private RecyclerView mRecyclerView;
  private NewsDetailAdapter mNewsDetailAdapter;





  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_news);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    setTitle("News");

    mRecyclerView = findViewById(R.id.recyclerViewNewsDetail);
    mRecyclerView.setHasFixedSize(true);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    // Inflate the layout for this fragment

    videoId = getIntent().getStringExtra("VIDEO_ID");
    searchOnYoutube();
    myVideoList = new ArrayList<MyVideo>();
    handler = new Handler();



  }


  private void searchOnYoutube() {
    new Thread(){
      @Override
      public void run() {
        YoutubeConnector yc = new YoutubeConnector(getApplicationContext());
        myVideoList = yc.getNewsByID(videoId);
        handler.post(new Runnable() {
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
    mNewsDetailAdapter = new NewsDetailAdapter(this, myVideoList);
    mRecyclerView.setAdapter(mNewsDetailAdapter);
  }
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        // for back button
        onBackPressed();
        return true;
    }


    return super.onOptionsItemSelected(item);
  }


}
