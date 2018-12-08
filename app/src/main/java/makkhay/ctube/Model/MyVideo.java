package makkhay.ctube.Model;



public class MyVideo {
    private String title;
    private String numberOfViews;
    private String thumbnailURL;
    private String pubDate;
    private String videoID;
    private String description;



    public MyVideo(String title, String numberOfViews, String thumbnailURL, String pubDate, String description) {
        this.title = title;
        this.numberOfViews = numberOfViews;
        this.thumbnailURL = thumbnailURL;
        this.pubDate = pubDate;
        this.description = description;
    }
    public MyVideo(){
    }

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNumberOfViews() {
        return numberOfViews;
    }

    public void setNumberOfViews(String numberOfViews) {
        this.numberOfViews = numberOfViews;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}