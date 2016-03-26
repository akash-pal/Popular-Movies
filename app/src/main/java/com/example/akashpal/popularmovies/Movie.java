package com.example.akashpal.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by akash pal on 3/16/2016.
 */
public class Movie implements Parcelable {

    private String id;
    private String title;
    private String release_date;
    private String vote_average;
    private String overview;
    private String poster_path;

    Movie() {

    }

    Movie(JSONObject movie) throws JSONException {
        id = movie.getString(Constants.TAG_TITLE);
        title = movie.getString(Constants.TAG_TITLE);
        poster_path = movie.getString(Constants.TAG_POSTER_PATH);
        release_date = movie.getString(Constants.TAG_RELEASE_DATE);
        vote_average = movie.getString(Constants.TAG_VOTE_AVERAGE);
        overview = movie.getString(Constants.TAG_OVERVIEW);
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(poster_path);
        dest.writeString(release_date);
        dest.writeString(vote_average);
        dest.writeString(overview);
    }

    private Movie(Parcel in) {
        id = in.readString();
        title = in.readString();
        poster_path = in.readString();
        release_date = in.readString();
        vote_average = in.readString();
        overview = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
