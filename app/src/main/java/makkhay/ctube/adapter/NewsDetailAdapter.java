package makkhay.ctube.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import makkhay.ctube.util.CustomDialogClass;

/**
 */

public class NewsDetailAdapter extends RecyclerView.Adapter<NewsDetailAdapter.VideoViewHolder> {

    private Context mContext;
    private List<MyVideo> myVideoList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private HashMap<String, String> mChildList = new HashMap<String, String>();

    public NewsDetailAdapter(Context mCtx, List<MyVideo> myVideoList) {
        this.mContext = mCtx;
        this.myVideoList = myVideoList;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.news_detail_card_view, null);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        MyVideo myVideo = myVideoList.get(position);
        holder.newsTitle.setText(myVideo.getTitle());
        holder.newsBody.setText(myVideo.getDescription());

        Log.d("TAg", "onBindViewHolder: title" + myVideo.getVideoID());

        mDatabase = FirebaseDatabase.getInstance().getReference();
        holder.setVideoIDForPlayer(myVideo.getVideoID());
        holder.setFavoriteListener(position); //pass position...
        holder.setCommentListner(position);
    }

    @Override
    public int getItemCount() {
        return myVideoList.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView newsTitle;
        TextView newsBody;
        ImageButton favButton;
        ImageButton commentButton;
        Boolean favorited = false;

        /**
         * Contains one card view.
         * @param itemView
         */
        public VideoViewHolder(View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.newsTitle);
            newsBody = itemView.findViewById(R.id.bodyText);
            commentButton = itemView.findViewById(R.id.commentButton);

        }
        public void setVideoIDForPlayer(final String vID){
            newsBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, NewsActivity.class);
                    System.out.println(vID);
                    intent.putExtra("VIDEO_ID", vID );
                    mContext.startActivity(intent);

                }
            });
        }

        public void setCommentListner(final int position){
            commentButton = itemView.findViewById(R.id.commentButton);
            commentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    CustomDialogClass cdd = new CustomDialogClass(mContext);
                    cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    cdd.show();


                  }
                });


        }

        /**
         *
         * ADD VIDEO TO DATABASE...
         */
        public void setFavoriteListener(final int position) {
            favButton = itemView.findViewById(R.id.favButton);
            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!favorited) {
                        favButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                        favorited = true;

                        MyVideo myVideoAdd = myVideoList.get(position); //now add this part into firebase database
                        mDatabase.child(mUser.getUid()).child("favorites").push().setValue(myVideoAdd);
                        Toast.makeText(mContext, "Added to Favorite", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        favButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        favorited = false;
                        MyVideo myVideoDelete = myVideoList.get(position);
                        deleteVideoFromDB(mDatabase.child(mUser.getUid()).child("favorites"), myVideoDelete);
                        Toast.makeText(mContext, "Removed from Favorite", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    /**
     * deletes video from database....
     * @param db
     */
    private synchronized void deleteVideoFromDB (DatabaseReference db, final MyVideo video) {
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                while(dataSnapshots.hasNext()) {
                    DataSnapshot videoItem = dataSnapshots.next();
                    MyVideo video = videoItem.getValue(MyVideo.class); // This is the request object.
                    mChildList.put(video.getVideoID(), videoItem.getKey());
                }
                String vidToDelete = video.getVideoID();
                if (mChildList.get(vidToDelete) != null) {
                    String childIDfound = mChildList.get(vidToDelete);
                    mDatabase.child(mUser.getUid()).child("favorites").child(childIDfound).setValue(null);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}


