package makkhay.ctube.Model;

import io.realm.RealmObject;

/**
 * Created by Prakash Gurung on 12/7/18.
 */
public class Comment extends RealmObject {

  private String author;
  private String comment;

  public Comment(){
    //default
  }

  public Comment(String author, String comment) {
    this.author = author;
    this.comment = comment;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }
}
