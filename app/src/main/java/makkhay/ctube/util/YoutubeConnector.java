package makkhay.ctube.util;

import android.content.Context;
import android.util.Log;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import makkhay.ctube.Model.MyVideo;
import makkhay.ctube.R;

/**
 * This class is responsible for fetching the youtube data via their api. The data will be retrieved, fetched and parsed in this class
 *
 */
public class YoutubeConnector {

    private YouTube mYouTube;
    private YouTube.Search.List query;
    public static final String KEY = "Your_api_key";

    /**
     * This constructor first creates an instance of youtube, which we will use to make a Http request to the server with the specific query.
     * @param content
     */
    public YoutubeConnector(Context content) {
        mYouTube = new YouTube.Builder(new NetHttpTransport(),
                new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(com.google.api.client.http.HttpRequest request) throws IOException {}
        }).setApplicationName(content.getString(R.string.app_name)).build();

        try{
            query = mYouTube.search().list("id,snippet");
            query.setKey(KEY);
            query.setType("video");
            query.setFields("items(id/videoId,snippet/title,snippet/publishedAt,snippet/description,snippet/thumbnails/default/url)");
            query.setMaxResults((long)10);
        }catch(IOException e){
            Log.d("YC", "Could not initialize: "+e);
        }
    }

    /**
     * This method will make query to the youtube backend, and grab the needed information  such as title, description etc..
     * @param keywords, to be searched on the youtube. This is provided by the programmer for now.
     * @return
     */
    public List<MyVideo> search(String keywords){
        query.setQ(keywords);
        try{
            SearchListResponse response = query.execute();
            List<SearchResult> results = response.getItems();

            List<MyVideo> items = new ArrayList<MyVideo>();
            for(SearchResult result:results){
                MyVideo myVideo = new MyVideo();
                myVideo.setTitle(result.getSnippet().getTitle());
                myVideo.setVideoID(result.getId().getVideoId().toString());

                /**
                 * This function is needed to get ViewCount and Other Statistics
                 */
                YouTube.Videos.List list = mYouTube.videos().list("statistics");
                list.setId(result.getId().getVideoId());
                list.setKey(KEY);
                Video v = list.execute().getItems().get(0);

                myVideo.setThumbnailURL(result.getSnippet().getThumbnails().getDefault().getUrl());
                myVideo.setNumberOfViews(v.getStatistics().getViewCount().toString());
                myVideo.setPubDate(result.getSnippet().getPublishedAt().toString());
                items.add(myVideo);

            }
            return items;
        }catch(IOException e){
            Log.d("YC", "Could not search: "+e);
            return null;
        }
    }
    /**
     * This method will make query to the youtube backend, and grab the needed information  such as title, description etc..
     * @param keywords, to be searched on the youtube. This is provided by the programmer for now.
     * @return
     */
    public List<MyVideo> getNews(String keywords){
        query.setQ(keywords);
        try{
            SearchListResponse response = query.execute();
            List<SearchResult> results = response.getItems();

            List<MyVideo> items = new ArrayList<MyVideo>();
            for(SearchResult result:results){
                MyVideo myVideo = new MyVideo();
                myVideo.setTitle(result.getSnippet().getTitle());
                myVideo.setVideoID(result.getId().getVideoId().toString());

                myVideo.setThumbnailURL(result.getSnippet().getTitle());
                myVideo.setDescription(result.getSnippet().getDescription());
                myVideo.setPubDate(result.getSnippet().getPublishedAt().toString());
                items.add(myVideo);

            }
            return items;
        }catch(IOException e){
            Log.d("YC", "Could not search: "+e);
            return null;
        }
    }
    /**
     * This method will make query to the youtube backend, and grab the needed information such as title, description etc..
     * @param videoID, to be searched on the youtube. This is provided by the programmer for now.
     * @return
     */
    public List<MyVideo> getNewsByID(String videoID){
        query.setQ(videoID);
        try{
            SearchListResponse response = query.execute();
            List<SearchResult> results = response.getItems();

            List<MyVideo> items = new ArrayList<MyVideo>();
            for(SearchResult result:results){
                MyVideo myVideo = new MyVideo();
                myVideo.setTitle(result.getSnippet().getTitle());
                myVideo.setVideoID(result.getId().getVideoId().toString());

                /**
                 * This function is needed to get the detail info about the selected video
                 */
                YouTube.Videos.List list = mYouTube.videos().list("snippet");
                list.setId(result.getId().getVideoId());
                list.setKey(KEY);
                Video v = list.execute().getItems().get(0);

                myVideo.setThumbnailURL(result.getSnippet().getTitle());
                myVideo.setDescription(v.getSnippet().getDescription());
                myVideo.setPubDate(result.getSnippet().getPublishedAt().toString());
                items.add(myVideo);

            }
            return items;
        }catch(IOException e){
            Log.d("YC", "Could not search: "+e);
            return null;
        }
    }


}
