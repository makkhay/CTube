package makkhay.ctube.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import makkhay.ctube.Model.MyVideo;
import makkhay.ctube.ui.NewsActivity;
import makkhay.ctube.R;
import makkhay.ctube.util.IntentShare;

/**
 * This is adapter class to bridge between the UI components and the data source, and finally populate the Recyclerview
 *
 *
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.VideoViewHolder> {

    private Context mCtx;
    private List<MyVideo> myVideoList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private HashMap<String, String> childKey_list = new HashMap<String, String>();

    public NewsAdapter(Context mCtx, List<MyVideo> myVideoList) {
        this.mCtx = mCtx;
        this.myVideoList = myVideoList;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.news_card_view, null);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        final MyVideo myVideo = myVideoList.get(position);
        holder.newsTitle.setText(myVideo.getTitle());
        holder.newsBody.setText(myVideo.getDescription());

        Log.d("TAg", "onBindViewHolder: title" + myVideo.getVideoID());

        mDatabase = FirebaseDatabase.getInstance().getReference();
        holder.setVideoIDForPlayer(myVideo.getVideoID());
        holder.setFavoriteListener(position); //pass position...

        // onClick listener for sharebutton
        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentShare share = new IntentShare(mCtx);
                share.shareIntent(myVideo.getTitle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return myVideoList.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView newsTitle;
        TextView newsBody;
        ImageButton favButton;
        ImageButton shareButton;
        Boolean favorited = false;

        /**
         * Contains one card view.
         * @param itemView
         */
         VideoViewHolder(View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.newsTitle);
            newsBody = itemView.findViewById(R.id.bodyText);
            shareButton = itemView.findViewById(R.id.shareButton);

        }

        /**
         * It will set the id for the particular item from the list. It will be send to another acitivty using intent
         * @param vID video id
         */
        private void setVideoIDForPlayer(final String vID){
            newsBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mCtx, NewsActivity.class);
                    System.out.println(vID);
                    intent.putExtra("VIDEO_ID", vID );
                    mCtx.startActivity(intent);

                }
            });
        }

        /**
         * This method helps to save an item to favorite
         *
         * @param position is used to get the particular item from the list
         */
        private void setFavoriteListener(final int position) {
            favButton = itemView.findViewById(R.id.favButton);
            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!favorited) {
                        favButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                        favorited = true;

                        MyVideo myVideoAdd = myVideoList.get(position); //now add this part into firebase database
                        mDatabase.child(user.getUid()).child("favorites").push().setValue(myVideoAdd);
                        Toast.makeText(mCtx, "Added to Favorite", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        favButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        favorited = false;
                        MyVideo myVideoDelete = myVideoList.get(position);
                        deleteVideoFromDB(mDatabase.child(user.getUid()).child("favorites"), myVideoDelete);
                        Toast.makeText(mCtx, "Removed from Favorite", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

    /**
     * Given a video this method will delete video from the database
     * @param db is the reference to the firebase where it will connect and do the deletion
     */
    private synchronized void deleteVideoFromDB (DatabaseReference db, final MyVideo video) {
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                while(dataSnapshots.hasNext()) {
                    DataSnapshot videoItem = dataSnapshots.next();
                    MyVideo video = videoItem.getValue(MyVideo.class); // This is the request object.
                    childKey_list.put(video.getVideoID(), videoItem.getKey());
                }
                String vidToDelete = video.getVideoID();
                if (childKey_list.get(vidToDelete) != null) {
                    String childIDfound = childKey_list.get(vidToDelete);
                    mDatabase.child(user.getUid()).child("favorites").child(childIDfound).setValue(null);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}


