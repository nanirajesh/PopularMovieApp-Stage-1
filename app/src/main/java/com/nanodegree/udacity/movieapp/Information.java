package com.nanodegree.udacity.movieapp;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by nanirajesh on 28-03-2016.
 */
public class Information implements Serializable {
    @Override
    public String toString() {
        return getTitle()+" "+getDescription()+" "+getVotes();
    }

    private String title;
    private String votes;
    private String description;
    private String releasedate;
    private String posterpath;

    public String getPosterpath() {
        return posterpath;
    }

    public void setPosterpath(String posterpath) {
        this.posterpath = "http://image.tmdb.org/t/p/w154"+posterpath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVotes() {
        return votes+"";
    }

    public String getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(String releasedate) {
        this.releasedate = releasedate;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
