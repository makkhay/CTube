package makkhay.ctube.util;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Prakash Gurung on 12/7/18.
 */
public class IntentShare {


  private Context mContext;
  public IntentShare(Context context) {
    mContext = context;
  }

  public void shareIntent(String message){
    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
    sharingIntent.setType("text/plain");
    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share the news story");
    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
    mContext.startActivity(Intent.createChooser(sharingIntent, "CTube"));
    }

}
