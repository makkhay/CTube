package makkhay.ctube.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import makkhay.ctube.Model.Comment;
import makkhay.ctube.R;

/**
 * Created by Prakash Gurung on 12/7/18.
 *
 *
 * This is an adapter class for the Comment class
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

  private List<Comment> mCommentList;
  Context c;

  public CommentAdapter(Context c, List<Comment> songList)
  {
    this.c = c;
    this.mCommentList = songList;
  }


  @Override
  public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list, parent, false);

    return new CommentViewHolder(v);
  }

  @Override
  public void onBindViewHolder(CommentViewHolder holder, int position) {

    holder.textName.setText(mCommentList.get(position).getAuthor());
    holder.textSong.setText(mCommentList.get(position).getComment());
  }

  @Override
  public int getItemCount() {
    return mCommentList.size();
  }

  public static class CommentViewHolder extends RecyclerView.ViewHolder {

    private TextView textName,textSong;

    public CommentViewHolder(View itemView) {
      super(itemView);

      textName = (TextView) itemView.findViewById(R.id.author);
      textSong = (TextView) itemView.findViewById(R.id.comment);
    }
  }
}
