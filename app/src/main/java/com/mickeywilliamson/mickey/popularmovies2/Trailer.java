package com.mickeywilliamson.mickey.popularmovies2;

public class Trailer {

    private String title;
    private String video_id;

    public Trailer() {}

    public Trailer(String title, String video_id) {
        this.title = title;
        this.video_id = video_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setVideoID(String video_id) {
        this.video_id = video_id;
    }

    public String getVideoId() {
        return video_id;
    }
}
