package makkhay.ctube.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import makkhay.ctube.adapter.VideoAdapterFavorites;
import makkhay.ctube.R;

/**
 * A simple {@link Fragment} class to handle the favorite videos and news.
 */
public class FavoritesFrag extends Fragment {
    private View v;
    private RecyclerView mRecyclerView;
    private List<MyVideo> myVideoList;
    private VideoAdapterFavorites mAdapter;

    DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    /**
     * Display all videos in a mRecyclerView that are in the mUser's favorite list
     *
     */
    public FavoritesFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(mUser.getUid()).child("favorites"); // my refernce to all faovrite videos

        v = inflater.inflate(R.layout.fragment_favorites, container, false);
        mRecyclerView = v.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));

        myVideoList = new ArrayList<MyVideo>();

        // for  DB loading
        loadFavsFromDatabase(mDatabase);

        return v;
    }

    /**
     * load favorites directly
     */
    private synchronized void updateVideosFromDB(){
        mAdapter = new VideoAdapterFavorites(v.getContext(), myVideoList);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Load favorites from database
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


    public String toString(){
        return "Favorite";
    }

}
