package makkhay.ctube.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import makkhay.ctube.Model.MyVideo;
import makkhay.ctube.R;
import makkhay.ctube.adapter.VideoAdapterFavorites;

public class FavoriteActivity extends AppCompatActivity {

  private RecyclerView mRecyclerView;
  private List<MyVideo> myVideoList;
  private VideoAdapterFavorites mVideoAdapterFavorites;

  DatabaseReference mDatabase;
  private FirebaseAuth mAuth;
  private FirebaseUser mUser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_favorite);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    setTitle("Favorites");

    mAuth = FirebaseAuth.getInstance();
    mUser = mAuth.getCurrentUser();
    mDatabase = FirebaseDatabase.getInstance().getReference().child(mUser.getUid()).child("favorites"); // my refernce to all faovrite videos

    mRecyclerView = findViewById(R.id.recyclerView);
    mRecyclerView.setHasFixedSize(true);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    myVideoList = new ArrayList<MyVideo>();

    /**
     * For  DB loading
     */
    loadFavsFromDatabase(mDatabase);

  }

  /**
   *  load favorites then launch this
   * function to put the data loaded into the mVideoAdapterFavorites...
   */
  private synchronized void updateVideosFromDB(){
    mVideoAdapterFavorites = new VideoAdapterFavorites(this, myVideoList);
    mRecyclerView.setAdapter(mVideoAdapterFavorites);
  }

  /**
   * Load favorites from database then calls the mVideoAdapterFavorites with updateVideosFromDB();
   * @param db
   */
  private synchronized void loadFavsFromDatabase (DatabaseReference db) {
    db.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
        while(dataSnapshots.hasNext()) {
          DataSnapshot videoItem = dataSnapshots.next();
          MyVideo video = videoItem.getValue(MyVideo.class); // This is the request object.
          myVideoList.add(video);
        }
        System.out.println("Video list is: " + myVideoList);
        updateVideosFromDB();
      }
      @Override
      public void onCancelled(DatabaseError databaseError) {
      }
    });
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
