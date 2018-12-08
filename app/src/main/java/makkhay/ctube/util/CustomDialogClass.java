
package makkhay.ctube.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import makkhay.ctube.Model.Comment;
import makkhay.ctube.R;
import makkhay.ctube.adapter.CommentAdapter;

/**
 * This is a custom dialog box which extends Dialog. This class takes input from the user and displays it
 */
public class CustomDialogClass extends Dialog {

  private Context c;
  private ImageButton submitButton;
  private AppCompatEditText editText;
  private RecyclerView mRecyclerView;
  private List<Comment> mCommentList;
  private CommentAdapter mCommentAdapter;
  private Realm realm;

  public CustomDialogClass(Context a) {
    super(a);
    // TODO Auto-generated constructor stub
    this.c = a;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_comment);

    mCommentList = new ArrayList<>();
    mCommentAdapter = new CommentAdapter(c,mCommentList);
    realm = Realm.getInstance(c);

    final RealmResults<Comment> results = realm.where(Comment.class).findAll();
    if(results.size() > 0)
    {
      realm.beginTransaction();
      for (int i = 0; i < results.size(); i++) {
        mCommentList.add(results.get(i));

      }
      realm.commitTransaction();
    }

    mRecyclerView = (RecyclerView) findViewById(R.id.messageRV);
    mRecyclerView.setHasFixedSize(true);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(c));
    mRecyclerView.setAdapter(mCommentAdapter);

    submitButton = (ImageButton) findViewById(R.id.submit_btn);
    editText = (AppCompatEditText) findViewById(R.id.edit_feedback);

    // submit button onClick, it will get the user input and populate the realm database notify the adapter
    submitButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        realm.beginTransaction();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name;
        // if user is null then display it.
        if (user != null) {
          // Name, email address etc
         name = user.getDisplayName();
         Comment comment = realm.createObject(Comment.class);
         comment.setAuthor(name);
         comment.setComment(editText.getText().toString());
         realm.commitTransaction();
         mCommentList.add(comment);
         mCommentAdapter.notifyDataSetChanged();

        } else {
          Comment comment = new Comment("User: " ,editText.getText().toString());
          mCommentList.add(comment);
          mCommentAdapter.notifyDataSetChanged();

        }
        Toast.makeText(c,"Posted",Toast.LENGTH_SHORT).show();

      }
    });

  }





}