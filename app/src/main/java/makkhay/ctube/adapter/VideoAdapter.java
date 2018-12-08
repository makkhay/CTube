package makkhay.ctube.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import makkhay.ctube.Model.MyVideo;
import makkhay.ctube.ui.PlayerActivity;
import makkhay.ctube.R;
import makkhay.ctube.util.IntentShare;

/**
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private Context mCtx;
    private List<MyVideo> mMyVideoList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private HashMap<String, String> mChildKeyList = new HashMap<String, String>();

    public VideoAdapter(Context mCtx, List<MyVideo> myVideoList) {
        this.mCtx = mCtx;
        this.mMyVideoList = myVideoList;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.video_card_view, null);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        final MyVideo myVideo = mMyVideoList.get(position);
        String date = myVideo.getPubDate();

        String[] parts = date.split("-");
        String part1 = parts[0]; // 004
        String part2 = parts[1];
        String part3 = parts[2];

        holder.videoTitle.setText(myVideo.getTitle());
        holder.videoPubDate.setText("Uploaded: " + part1 + "/"+ part2 + "/"+ firstTwo(part3));
        holder.videoViewCount.setText("Views: " + myVideo.getNumberOfViews());
        Picasso.with(mCtx).load(myVideo.getThumbnailURL()).into(holder.videoThumbnail);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        holder.setVideoIDForPlayer(myVideo.getVideoID().toString());
        holder.setFavoriteListener(position); //pass position...

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
        return mMyVideoList.size();
    }

    public String firstTwo(String str) {
        return str.length() < 2 ? str : str.substring(0, 2);
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageButton videoThumbnail;
        TextView videoTitle;
        TextView videoPubDate;
        TextView videoViewCount;
        ImageButton favButton;
        ImageButton shareButton;
        Boolean favorited = false;

        /**
         * Contains one card view.
         * @param itemView
         */
        VideoViewHolder(View itemView) {
            super(itemView);
            videoThumbnail = itemView.findViewById(R.id.videoThumbnail);
            videoTitle = itemView.findViewById(R.id.videoTitle);
            videoPubDate = itemView.findViewById(R.id.videoPubDate);
            videoViewCount = itemView.findViewById(R.id.videoViewCount);
            shareButton = itemView.findViewById(R.id.shareButton);

        }


        private void setVideoIDForPlayer(final String vID){
            //To Play Video
            videoThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mCtx, PlayerActivity.class);
                    System.out.println(vID);
                    intent.putExtra("VIDEO_ID", vID );
                    mCtx.startActivity(intent);
                }
            });
        }

        /**
         * ADD VIDEO TO DATABASE...
         */
        private void setFavoriteListener(final int position) {
            favButton = itemView.findViewById(R.id.favoriteButton);
            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!favorited) {
                        favButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                        favorited = true;

                        MyVideo myVideoAdd = mMyVideoList.get(position); //now add this part into firebase database
                        mDatabase.child(mUser.getUid()).child("favorites").push().setValue(myVideoAdd);
                        Toast.makeText(mCtx, "Added to Favorite", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        favButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        favorited = false;
                        MyVideo myVideoDelete = mMyVideoList.get(position);
                        deleteVideoFromDB(mDatabase.child(mUser.getUid()).child("favorites"), myVideoDelete);
                        Toast.makeText(mCtx, "Removed from Favorite", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

//        private void setShareButtonListener(final int position){
//            shareButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(mCtx,"Coming soon :)",Toast.LENGTH_SHORT).show();
//
//                    String shareBody = ;
//                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//                    sharingIntent.setType("text/plain");
//                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
//                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
//                    mCtx.startActivity(Intent.createChooser(sharingIntent, "Camera Translate"));
//
//
//                }
//            });
//        }
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
                    mChildKeyList.put(video.getVideoID(), videoItem.getKey());
                }
                String vidToDelete = video.getVideoID();
                if (mChildKeyList.get(vidToDelete) != null) {
                    String childIDfound = mChildKeyList.get(vidToDelete);
                    mDatabase.child(mUser.getUid()).child("favorites").child(childIDfound).setValue(null);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}


