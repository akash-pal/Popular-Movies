package com.example.akashpal.popularmovies;

import com.example.akashpal.popularmovies.data.MovieContract;

/**
 * Created by akash pal on 3/17/2016.
 */
public class Constants {
    public static final String TAG_ID = "id";
    public static final String TAG_TITLE = "title";
    public static final String TAG_POSTER_PATH = "poster_path";
    public static final String TAG_RELEASE_DATE = "release_date";
    public static final String TAG_VOTE_AVERAGE = "vote_average";
    public static final String TAG_OVERVIEW = "overview";

    public static final String SORT_BY_POPULAR = "popular";
    public static final String SORT_BY_TOP_RATED = "top_rated";
    public static final String SORT_KEY = "sortkey";
    public static final String SORT_TITLE = "sortTitle";

    public static final String POPULAR = "Popular Movies";
    public static final String TOP_RATED = "Top Rated Movies";

    public static final String BASE_URL = "http://image.tmdb.org/t/p/w185";

    public static final String DETAIL_MOVIE = "DETAIL_MOVIE";

    public static final String TAG_REVIEW_RESULTS = "results";
    public static final String TAG_REVIEW_CONTENT = "content";

    public static final String TAG_TRAILER_RESULTS = "results";
    public static final String TAG_TRAILER_ID = "id";
    public static final String TAG_TRAILER_KEY = "key";
    public static final String TAG_TRAILER_NAME = "name";
    public static final String TAG_TRAILER_SITE = "site";
    public static final String TAG_TRAILER_TYPE = "type";

    public static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_IMAGE,
            MovieContract.MovieEntry.COLUMN_IMAGE2,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_RATING,
            MovieContract.MovieEntry.COLUMN_DATE
    };

    public static final int COL_ID = 0;
    public static final int COL_MOVIE_ID = 1;
    public static final int COL_TITLE = 2;
    public static final int COL_IMAGE = 3;
    public static final int COL_IMAGE2 = 4;
    public static final int COL_OVERVIEW = 5;
    public static final int COL_RATING = 6;
    public static final int COL_DATE = 7;




}
