package makkhay.ctube.util;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Prakash Gurung on 12/7/18.
 *
 * A simple utility class to use the intent sharing
 */
public class IntentShare {


  private Context mContext;
  public IntentShare(Context context) {
    mContext = context;
  }

  /**
   * this method will take the message as param and display the various options to share.
   * @param text, to be passed on so that I can use it from other class.
   */
  public void shareIntent(String text){
    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
    sharingIntent.setType("text/plain");
    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share the news story");
    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
    mContext.startActivity(Intent.createChooser(sharingIntent, "CTube"));
    }

}
